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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb

/**
 * Interface defining the contract for signature state management.
 *
 * This interface provides the foundation for managing signature drawing operations,
 * including path storage, undo/redo functionality, and signature bitmap generation.
 * Implementations must handle state mutations in a way compatible with Compose's
 * reactive update system.
 *
 * @see SignatureState
 */
public interface ISignatureState {
    /**
     * The collection of all drawn path segments that comprise the current signature.
     *
     * This list is observable and will trigger recomposition when modified.
     */
    public val paths: List<SignaturePath>

    /**
     * The current input state indicating whether drawing is idle, active, or completed.
     */
    public val inputState: SignatureInputState

    /**
     * The most recently generated bitmap representation of the signature.
     *
     * This value is null when the signature is empty or has not been rendered.
     */
    public val signature: ImageBitmap?

    /**
     * Indicates whether there are operations available to undo.
     */
    public val canUndo: Boolean

    /**
     * Indicates whether there are previously undone operations available to redo.
     */
    public val canRedo: Boolean

    /**
     * Adds a new path segment to the signature.
     *
     * @param path The [SignaturePath] to add to the current signature.
     */
    public fun addPath(path: SignaturePath)

    /**
     * Removes all paths and resets the signature to an empty state.
     */
    public fun clear()

    /**
     * Reverts the most recent drawing operation.
     *
     * This method restores the signature to its state before the last stroke was drawn.
     * The undone operation is preserved in the redo stack.
     */
    public fun undo()

    /**
     * Reapplies the most recently undone drawing operation.
     *
     * This method restores a stroke that was previously removed via [undo].
     */
    public fun redo()

    /**
     * Updates the cached signature bitmap representation.
     *
     * @param bitmap The new [ImageBitmap] to cache, or null to clear the cache.
     */
    public fun updateSignature(bitmap: ImageBitmap?)
}

/**
 * Concrete implementation of [ISignatureState] providing full signature state management.
 *
 * This class manages all aspects of signature state including path storage, undo/redo history,
 * input state tracking, and bitmap caching. It is designed to work seamlessly with Compose's
 * state management system, triggering recomposition when signature data changes.
 *
 * The class maintains separate stacks for undo and redo operations, with a configurable
 * maximum stack size to prevent excessive memory usage during extended drawing sessions.
 *
 * Instances should be created using [rememberSignatureState] to ensure proper state
 * preservation across configuration changes.
 *
 * @see rememberSignatureState
 * @see ISignatureState
 */
@Stable
public class SignatureState internal constructor() : ISignatureState {

    /**
     * Internal mutable list storing all signature path segments.
     */
    private val _paths = mutableStateListOf<SignaturePath>()

    /**
     * Stack of previous states for undo functionality.
     */
    private val _undoStack = mutableStateListOf<List<SignaturePath>>()

    /**
     * Stack of undone states for redo functionality.
     */
    private val _redoStack = mutableStateListOf<List<SignaturePath>>()

    /**
     * Mutable state holder for the current input state.
     */
    private val _inputState = mutableStateOf(SignatureInputState.IDLE)

    /**
     * Mutable state holder for the cached signature bitmap.
     */
    private val _signature = mutableStateOf<ImageBitmap?>(null)

    /**
     * Flag tracking whether a stroke is currently being drawn.
     */
    private var _isStrokeInProgress = false

    /**
     * The collection of all drawn path segments that comprise the current signature.
     *
     * Returns an immutable copy of the internal path list.
     */
    override val paths: List<SignaturePath> get() = _paths.toList()

    /**
     * The current input state indicating whether drawing is idle, active, or completed.
     */
    override val inputState: SignatureInputState get() = _inputState.value

    /**
     * The most recently generated bitmap representation of the signature.
     */
    override val signature: ImageBitmap? get() = _signature.value

    /**
     * Indicates whether there are operations available to undo.
     */
    override val canUndo: Boolean get() = _undoStack.isNotEmpty()

    /**
     * Indicates whether there are previously undone operations available to redo.
     */
    override val canRedo: Boolean get() = _redoStack.isNotEmpty()

    /**
     * Maximum number of states to retain in the undo stack.
     *
     * Older states are discarded when this limit is exceeded to prevent
     * unbounded memory growth during extended drawing sessions.
     */
    private val maxUndoStackSize = 50

    /**
     * Marks the beginning of a new stroke for undo tracking purposes.
     *
     * This method should be called when the user starts drawing a new stroke.
     * It saves the current state to enable undo of the entire stroke as a single operation.
     */
    internal fun beginStroke() {
        if (!_isStrokeInProgress) {
            _isStrokeInProgress = true
            saveStateForUndo()
        }
    }

    /**
     * Marks the end of the current stroke.
     *
     * This method should be called when the user completes a stroke, indicating
     * that subsequent paths belong to a new drawing operation.
     */
    internal fun endStroke() {
        _isStrokeInProgress = false
    }

    /**
     * Adds a new path segment to the signature.
     *
     * Adding a path clears the redo stack since new drawing operations invalidate
     * the redo history. The input state is updated to [SignatureInputState.DRAWING].
     *
     * @param path The [SignaturePath] to add to the current signature.
     */
    override fun addPath(path: SignaturePath) {
        _paths.add(path)
        _redoStack.clear()
        updateInputState(SignatureInputState.DRAWING)
    }

    /**
     * Removes all paths and resets the signature to an empty state.
     *
     * The current state is saved to the undo stack before clearing, allowing
     * the clear operation to be undone. The redo stack is cleared since
     * clearing invalidates previous redo history.
     */
    override fun clear() {
        if (_paths.isNotEmpty()) {
            saveStateForUndo()
        }
        _paths.clear()
        _signature.value = null
        _redoStack.clear()
        updateInputState(SignatureInputState.IDLE)
    }

    /**
     * Reverts the most recent drawing operation.
     *
     * The current state is pushed to the redo stack before restoration,
     * and the most recent state from the undo stack becomes the current state.
     * The input state is updated based on whether any paths remain.
     */
    override fun undo() {
        if (canUndo) {
            _redoStack.add(_paths.toList())

            val previousState = _undoStack.removeLastOrNull()
            if (previousState != null) {
                _paths.clear()
                _paths.addAll(previousState)
            }

            updateInputState(
                if (_paths.isEmpty()) SignatureInputState.IDLE
                else SignatureInputState.DRAWING
            )
        }
    }

    /**
     * Reapplies the most recently undone drawing operation.
     *
     * The current state is pushed to the undo stack, and the most recent
     * state from the redo stack becomes the current state.
     * The input state is updated based on whether any paths exist.
     */
    override fun redo() {
        if (canRedo) {
            _undoStack.add(_paths.toList())

            val nextState = _redoStack.removeLastOrNull()
            if (nextState != null) {
                _paths.clear()
                _paths.addAll(nextState)
            }

            updateInputState(
                if (_paths.isEmpty()) SignatureInputState.IDLE
                else SignatureInputState.DRAWING
            )
        }
    }

    /**
     * Updates the cached signature bitmap representation.
     *
     * When a valid bitmap is provided and paths exist, the input state is
     * updated to [SignatureInputState.COMPLETED] to indicate the signature
     * is ready for export.
     *
     * @param bitmap The new [ImageBitmap] to cache, or null to clear the cache.
     */
    override fun updateSignature(bitmap: ImageBitmap?) {
        _signature.value = bitmap
        if (_paths.isNotEmpty() && bitmap != null) {
            updateInputState(SignatureInputState.COMPLETED)
        }
    }

    /**
     * Updates the current input state.
     *
     * @param state The new [SignatureInputState] to set.
     */
    private fun updateInputState(state: SignatureInputState) {
        _inputState.value = state
    }

    /**
     * Saves the current path state to the undo stack.
     *
     * If the undo stack exceeds [maxUndoStackSize], the oldest state is removed
     * to maintain bounded memory usage.
     */
    private fun saveStateForUndo() {
        _undoStack.add(_paths.toList())

        if (_undoStack.size > maxUndoStackSize) {
            _undoStack.removeFirstOrNull()
        }
    }

    /**
     * Restores a previously saved state during state restoration.
     *
     * This internal method is used by the [Saver] to restore state after
     * process recreation or configuration changes.
     *
     * @param paths The list of [SignaturePath] to restore.
     * @param inputState The [SignatureInputState] to restore.
     */
    internal fun restoreState(
        paths: List<SignaturePath>,
        inputState: SignatureInputState
    ) {
        _paths.clear()
        _paths.addAll(paths)
        _signature.value = null
        _inputState.value = inputState
    }

    /**
     * Companion object containing the state saver for configuration change survival.
     */
    public companion object {
        /**
         * Saver implementation for preserving [SignatureState] across configuration changes.
         *
         * This saver serializes the essential state data (paths and input state) to a
         * format that can be stored in a Bundle. The signature bitmap is not saved
         * as it can be regenerated from the paths.
         *
         * Path data is serialized as a list of coordinate values, stroke widths,
         * and color values to ensure compatibility with Bundle storage.
         */
        public val Saver: Saver<SignatureState, *> = Saver(
            save = { state ->
                val pathData = state.paths.map { path ->
                    listOf(
                        path.start.x,
                        path.start.y,
                        path.end.x,
                        path.end.y,
                        path.strokeWidth,
                        path.color.toArgb().toFloat()
                    )
                }
                listOf(pathData, state.inputState.ordinal)
            },
            restore = { saved ->
                @Suppress("UNCHECKED_CAST")
                val data = saved as List<Any>
                @Suppress("UNCHECKED_CAST")
                val pathData = data[0] as List<List<Float>>
                val inputStateOrdinal = (data[1] as Number).toInt()

                val paths = pathData.map { values ->
                    SignaturePath(
                        start = Offset(values[0], values[1]),
                        end = Offset(values[2], values[3]),
                        strokeWidth = values[4],
                        color = Color(values[5].toInt())
                    )
                }

                SignatureState().apply {
                    restoreState(
                        paths = paths,
                        inputState = SignatureInputState.entries[inputStateOrdinal]
                    )
                }
            }
        )
    }
}

/**
 * Creates and remembers a [SignatureState] instance that survives configuration changes.
 *
 * This composable function creates a [SignatureState] that is automatically saved
 * and restored across configuration changes such as screen rotation. The state
 * includes all drawn paths and the current input state, but not the generated
 * bitmap which is regenerated as needed.
 *
 * @return A [SignatureState] instance that persists across configuration changes.
 *
 * @see SignatureState
 */
@Composable
public fun rememberSignatureState(): SignatureState = rememberSaveable(
    saver = SignatureState.Saver
) {
    SignatureState()
}
