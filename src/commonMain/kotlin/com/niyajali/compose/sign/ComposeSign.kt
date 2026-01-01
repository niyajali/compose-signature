/*
 * Copyright 2024 Niyaj Ali.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.niyajali.compose.sign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

/**
 * A composable signature pad component that allows users to draw signatures using touch or mouse input.
 *
 * This is the simplest overload that uses default configuration settings. The signature is captured
 * as an [ImageBitmap] and provided through the [onSignatureUpdate] callback whenever the signature
 * content changes.
 *
 * @param onSignatureUpdate Callback invoked when the signature content changes. Receives the current
 *                          signature as an [ImageBitmap], or null if the signature pad is empty.
 * @param modifier The modifier to be applied to the signature pad container.
 * @param state The state object that manages the signature drawing data and undo/redo history.
 *              Defaults to a new instance created by [rememberSignatureState].
 *
 * @see SignatureState
 * @see SignatureConfig
 */
@Composable
public fun ComposeSign(
    onSignatureUpdate: (ImageBitmap?) -> Unit,
    modifier: Modifier = Modifier,
    state: SignatureState = rememberSignatureState()
) {
    ComposeSign(
        onSignatureUpdate = onSignatureUpdate,
        modifier = modifier,
        config = SignatureConfig(),
        state = state,
        onActionClicked = null
    )
}

/**
 * A composable signature pad component with customizable stroke and background properties.
 *
 * This overload provides direct access to common visual customization options without requiring
 * a full [SignatureConfig] object. For more advanced configuration options, use the overload
 * that accepts a [SignatureConfig] parameter.
 *
 * @param onSignatureUpdate Callback invoked when the signature content changes. Receives the current
 *                          signature as an [ImageBitmap], or null if the signature pad is empty.
 * @param modifier The modifier to be applied to the signature pad container.
 * @param strokeColor The color used for drawing signature strokes.
 * @param strokeWidth The width of signature strokes in density-independent pixels.
 * @param backgroundColor The background color of the signature pad canvas.
 * @param showGrid Whether to display a grid overlay on the signature pad for visual guidance.
 * @param state The state object that manages the signature drawing data and undo/redo history.
 *              Defaults to a new instance created by [rememberSignatureState].
 *
 * @see SignatureConfig
 * @see SignatureState
 */
@Composable
public fun ComposeSign(
    onSignatureUpdate: (ImageBitmap?) -> Unit,
    modifier: Modifier = Modifier,
    strokeColor: Color = Color.Black,
    strokeWidth: Dp = 3.dp,
    backgroundColor: Color = Color.White,
    showGrid: Boolean = false,
    state: SignatureState = rememberSignatureState()
) {
    ComposeSign(
        onSignatureUpdate = onSignatureUpdate,
        modifier = modifier,
        config = SignatureConfig(
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            backgroundColor = backgroundColor,
            showGrid = showGrid
        ),
        state = state,
        onActionClicked = null
    )
}

/**
 * A fully configurable composable signature pad component with action button support.
 *
 * This is the most flexible overload, providing complete control over the signature pad's
 * appearance, behavior, and action handling. The component supports touch and mouse input
 * for drawing signatures, with optional undo/redo functionality and built-in action buttons.
 *
 * The signature is automatically converted to an [ImageBitmap] and provided through the
 * [onSignatureUpdate] callback with a debounced mechanism to optimize performance.
 *
 * @param onSignatureUpdate Callback invoked when the signature content changes. Receives the current
 *                          signature as an [ImageBitmap], or null if the signature pad is empty.
 *                          This callback is debounced to prevent excessive updates during drawing.
 * @param modifier The modifier to be applied to the signature pad container.
 * @param config Configuration object controlling the visual appearance and behavior of the
 *               signature pad, including stroke properties, colors, dimensions, and grid settings.
 * @param state The state object that manages the signature drawing data, undo/redo history,
 *              and current input state. Defaults to a new instance created by [rememberSignatureState].
 * @param onActionClicked Optional callback invoked when a built-in action button is clicked.
 *                        When provided and [SignatureConfig.showActions] is true, action buttons
 *                        are displayed below the signature pad. Pass null to hide action buttons.
 *
 * @see SignatureConfig
 * @see SignatureState
 * @see SignatureAction
 */
@OptIn(FlowPreview::class)
@Composable
public fun ComposeSign(
    onSignatureUpdate: (ImageBitmap?) -> Unit,
    modifier: Modifier = Modifier,
    config: SignatureConfig = SignatureConfig(),
    state: SignatureState = rememberSignatureState(),
    onActionClicked: ((SignatureAction) -> Unit)? = null
) {
    val actualModifier = if (config.isFullScreen) {
        modifier.fillMaxSize()
    } else {
        modifier
            .fillMaxWidth()
            .heightIn(min = config.minHeight, max = config.maxHeight)
    }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    Column(modifier = actualModifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onSizeChanged { size ->
                    canvasSize = size
                }
                .background(config.backgroundColor, config.cornerShape)
                .run {
                    config.borderStroke?.let { border ->
                        border(border, config.cornerShape)
                    } ?: this
                }
                .clipToBounds()
                .pointerInput(config) {
                    detectDragGestures(
                        onDragStart = { _ ->
                            state.beginStroke()
                        },
                        onDragEnd = {
                            state.endStroke()
                        }
                    ) { change, dragAmount ->
                        change.consume()

                        val path = SignaturePath(
                            start = change.position - dragAmount,
                            end = change.position,
                            strokeWidth = config.strokeWidth.toPx(),
                            color = config.strokeColor
                        )

                        state.addPath(path)
                    }
                }
                .drawBehind {
                    drawRect(config.backgroundColor)

                    if (config.showGrid) {
                        drawGrid(config.gridColor, config.gridSpacing.toPx())
                    }

                    state.paths.forEach { path ->
                        drawLine(
                            color = path.color,
                            start = path.start,
                            end = path.end,
                            strokeWidth = path.strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
        )

        if (config.showActions && onActionClicked != null) {
            SignatureActionsRow(
                state = state,
                onActionClicked = onActionClicked,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    LaunchedEffect(state, canvasSize) {
        snapshotFlow { state.paths.toList() to canvasSize }
            .debounce(16L)
            .collectLatest { (paths, size) ->
                if (size.width > 0 && size.height > 0) {
                    val bitmap = if (paths.isNotEmpty()) {
                        pathsToImageBitmap(
                            width = size.width,
                            height = size.height,
                            paths = paths,
                            backgroundColor = config.backgroundColor
                        )
                    } else {
                        null
                    }
                    state.updateSignature(bitmap)
                    onSignatureUpdate(bitmap)
                }
            }
    }
}

/**
 * A fullscreen composable signature pad designed for dedicated signature capture screens.
 *
 * This composable provides a signature pad that fills the entire available screen space
 * with built-in action handling for common operations such as clear, undo, redo, and save.
 * It is particularly useful for modal signature capture flows where the user's full attention
 * should be on signing.
 *
 * The component automatically handles action button clicks internally, delegating to the
 * [SignatureState] for state management operations and invoking [onDismiss] when the
 * save action is triggered.
 *
 * @param onSignatureUpdate Callback invoked when the signature content changes. Receives the current
 *                          signature as an [ImageBitmap], or null if the signature pad is empty.
 * @param onDismiss Callback invoked when the user completes the signature by clicking the save button.
 *                  Typically used to close the fullscreen signature view and process the captured signature.
 * @param modifier The modifier to be applied to the signature pad container.
 * @param config Configuration object controlling the visual appearance and behavior. Defaults to
 *               fullscreen mode with action buttons enabled. The [SignatureConfig.isFullScreen]
 *               property is automatically set to true regardless of the provided configuration.
 * @param state The state object that manages the signature drawing data and undo/redo history.
 *              Defaults to a new instance created by [rememberSignatureState].
 *
 * @see ComposeSign
 * @see SignatureConfig
 * @see SignatureState
 */
@Composable
public fun ComposeSignFullscreen(
    onSignatureUpdate: (ImageBitmap?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    config: SignatureConfig = SignatureConfig(
        isFullScreen = true,
        showActions = true
    ),
    state: SignatureState = rememberSignatureState()
) {
    ComposeSign(
        onSignatureUpdate = onSignatureUpdate,
        modifier = modifier,
        config = config.copy(isFullScreen = true),
        state = state,
        onActionClicked = { action ->
            when (action) {
                SignatureAction.CLEAR -> state.clear()
                SignatureAction.SAVE -> onDismiss()
                SignatureAction.UNDO -> state.undo()
                SignatureAction.REDO -> state.redo()
            }
        }
    )
}

/**
 * Internal composable that displays a row of action buttons for signature operations.
 *
 * This component renders Clear, Undo, Redo, and Save buttons in a horizontal arrangement,
 * with each button's enabled state determined by the current [SignatureState]. The Clear
 * and Save buttons are enabled when the signature is not empty, while Undo and Redo buttons
 * are enabled based on the availability of undo/redo history.
 *
 * @param state The signature state used to determine button enabled states.
 * @param onActionClicked Callback invoked when any action button is clicked, providing
 *                        the corresponding [SignatureAction].
 * @param modifier The modifier to be applied to the row container.
 */
@Composable
internal fun SignatureActionsRow(
    state: SignatureState,
    onActionClicked: (SignatureAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            onClick = { onActionClicked(SignatureAction.CLEAR) },
            enabled = !state.isEmpty()
        ) {
            Text(SignatureAction.CLEAR.getDisplayName())
        }

        OutlinedButton(
            onClick = { onActionClicked(SignatureAction.UNDO) },
            enabled = state.canUndo
        ) {
            Text(SignatureAction.UNDO.getDisplayName())
        }

        OutlinedButton(
            onClick = { onActionClicked(SignatureAction.REDO) },
            enabled = state.canRedo
        ) {
            Text(SignatureAction.REDO.getDisplayName())
        }

        Button(
            onClick = { onActionClicked(SignatureAction.SAVE) },
            enabled = !state.isEmpty()
        ) {
            Text(SignatureAction.SAVE.getDisplayName())
        }
    }
}
