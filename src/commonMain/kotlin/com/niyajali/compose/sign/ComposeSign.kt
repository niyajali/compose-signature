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

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Simple signature composable with minimal configuration
 *
 * @param onSignatureUpdate Callback invoked when signature changes
 * @param modifier Modifier to be applied to the signature pad
 * @param state Signature state holder
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
 * Basic signature composable with essential customization
 *
 * @param onSignatureUpdate Callback invoked when signature changes
 * @param modifier Modifier to be applied to the signature pad
 * @param strokeColor Color of the signature strokes
 * @param strokeWidth Width of the signature strokes
 * @param backgroundColor Background color of the signature pad
 * @param showGrid Whether to show grid lines
 * @param state Signature state holder
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
 * Full-featured signature composable with complete configuration
 *
 * @param onSignatureUpdate Callback invoked when signature changes
 * @param modifier Modifier to be applied to the signature pad
 * @param config Configuration object containing all appearance and behavior settings
 * @param state Signature state holder
 * @param onActionClicked Optional callback for signature actions
 */
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

    Column(modifier = actualModifier) {
        // Main signature drawing area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(config.backgroundColor)
                .run {
                    config.borderStroke?.let { border ->
                        border(border, config.cornerShape)
                    } ?: this
                }
                .pointerInput(config) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            // Optional: Add haptic feedback or visual indication
                        },
                        onDragEnd = {
                            // Optional: Add completion callback or haptic feedback
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
                .drawWithContent {
                    // Draw background
                    drawRect(config.backgroundColor)

                    // Draw signature
                    drawContent()

                    // Draw grid if enabled
                    if (config.showGrid) {
                        drawGrid(config.gridColor, config.gridSpacing.toPx())
                    }

                    // Update signature bitmap
                    val bitmap = pathsToImageBitmap(
                        width = size.width.toInt(),
                        height = size.height.toInt(),
                        paths = state.paths,
                        backgroundColor = config.backgroundColor
                    )
                    state.updateSignature(bitmap)
                }
        ) {
            // Display current signature
            state.signature?.let { signature ->
                Image(
                    bitmap = signature,
                    contentDescription = "Digital Signature",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Built-in actions (if enabled)
        if (config.showActions && onActionClicked != null) {
            SignatureActionsRow(
                state = state,
                onActionClicked = onActionClicked,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    // Trigger callback when signature changes
    LaunchedEffect(state.signature) {
        onSignatureUpdate(state.signature)
    }
}

/**
 * Fullscreen signature overlay composable
 *
 * @param onSignatureUpdate Callback invoked when signature changes
 * @param onDismiss Callback to dismiss the fullscreen signature
 * @param modifier Modifier to be applied to the signature pad
 * @param config Configuration object (defaults to fullscreen with actions)
 * @param state Signature state holder
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
 * Built-in actions row for signature controls
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
        // Clear button
        OutlinedButton(
            onClick = { onActionClicked(SignatureAction.CLEAR) },
            enabled = !state.isEmpty()
        ) {
            Text("Clear")
        }

        // Undo button
        OutlinedButton(
            onClick = { onActionClicked(SignatureAction.UNDO) },
            enabled = state.canUndo
        ) {
            Text("Undo")
        }

        // Redo button
        OutlinedButton(
            onClick = { onActionClicked(SignatureAction.REDO) },
            enabled = state.canRedo
        ) {
            Text("Redo")
        }

        // Save button
        Button(
            onClick = { onActionClicked(SignatureAction.SAVE) },
            enabled = !state.isEmpty()
        ) {
            Text("Save")
        }
    }
}
