package com.niyajali.compose.sign

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Interface defining signature state operations (Interface Segregation Principle)
 */
public interface ISignatureState {
    /**
     * List of signature paths drawn by the user
     */
    public val paths: List<SignaturePath>

    /**
     * Current input state of the signature pad
     */
    public val inputState: SignatureInputState

    /**
     * Generated signature bitmap
     */
    public val signature: ImageBitmap?

    /**
     * Whether undo operation is available
     */
    public val canUndo: Boolean

    /**
     * Whether redo operation is available
     */
    public val canRedo: Boolean

    /**
     * Add a new signature path
     */
    public fun addPath(path: SignaturePath)

    /**
     * Clear all signature paths
     */
    public fun clear()

    /**
     * Undo the last operation
     */
    public fun undo()

    /**
     * Redo the last undone operation
     */
    public fun redo()

    /**
     * Update the signature bitmap
     */
    public fun updateSignature(bitmap: ImageBitmap)
}

/**
 * Enhanced signature state with comprehensive state management
 * Implements the Single Responsibility Principle
 */
@Stable
public class SignatureState internal constructor() : ISignatureState {

    // Internal mutable state
    private val _paths = mutableStateListOf<SignaturePath>()
    private val _undoStack = mutableStateListOf<List<SignaturePath>>()
    private val _redoStack = mutableStateListOf<List<SignaturePath>>()
    private val _inputState = mutableStateOf(SignatureInputState.IDLE)
    private val _signature = mutableStateOf<ImageBitmap?>(null)

    // Public immutable interfaces
    override val paths: List<SignaturePath> get() = _paths.toList()
    override val inputState: SignatureInputState get() = _inputState.value
    override val signature: ImageBitmap? get() = _signature.value
    override val canUndo: Boolean get() = _undoStack.isNotEmpty()
    override val canRedo: Boolean get() = _redoStack.isNotEmpty()

    /**
     * Maximum number of undo operations to keep in memory
     */
    private val maxUndoStackSize = 50

    override fun addPath(path: SignaturePath) {
        saveStateForUndo()
        _paths.add(path)
        _redoStack.clear() // Clear redo stack when new action is performed
        updateInputState(SignatureInputState.DRAWING)
    }

    override fun clear() {
        saveStateForUndo()
        _paths.clear()
        _signature.value = null
        _redoStack.clear()
        updateInputState(SignatureInputState.IDLE)
    }

    override fun undo() {
        if (canUndo) {
            // Save current state to redo stack
            _redoStack.add(_paths.toList())

            // Restore previous state
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

    override fun redo() {
        if (canRedo) {
            saveStateForUndo()

            // Restore next state
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

    override fun updateSignature(bitmap: ImageBitmap) {
        _signature.value = bitmap
        if (_paths.isNotEmpty()) {
            updateInputState(SignatureInputState.COMPLETED)
        }
    }

    /**
     * Update the current input state
     */
    private fun updateInputState(state: SignatureInputState) {
        _inputState.value = state
    }

    /**
     * Save current state for undo operation
     */
    private fun saveStateForUndo() {
        _undoStack.add(_paths.toList())

        // Limit undo stack size to prevent memory issues
        if (_undoStack.size > maxUndoStackSize) {
            _undoStack.removeFirstOrNull()
        }
    }

    /**
     * Internal method to restore state from saved data
     */
    internal fun restoreState(
        paths: List<SignaturePath>,
        signature: ImageBitmap?,
        inputState: SignatureInputState
    ) {
        _paths.clear()
        _paths.addAll(paths)
        _signature.value = signature
        _inputState.value = inputState
    }

    public companion object {
        /**
         * Saver for persisting SignatureState across configuration changes
         */
        public val Saver: Saver<SignatureState, *> = Saver(
            save = { state ->
                Triple(
                    state.paths,
                    state.signature,
                    state.inputState
                )
            },
            restore = { saved ->
                SignatureState().apply {
                    restoreState(
                        paths = saved.first,
                        signature = saved.second,
                        inputState = saved.third
                    )
                }
            }
        )
    }
}

/**
 * Remember signature state with automatic state saving
 *
 * @return A [SignatureState] instance that persists across configuration changes
 */
@Composable
public fun rememberSignatureState(): SignatureState = rememberSaveable(
    saver = SignatureState.Saver
) {
    SignatureState()
}