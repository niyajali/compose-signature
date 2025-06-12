package com.niyajali.compose.sign

/**
 * Defines actions that can be performed on a signature
 *
 * This enum represents the various operations that users can perform
 * on the signature pad to modify or finalize their signature.
 */
public enum class SignatureAction {
    /**
     * Clear all signature content from the pad
     *
     * This action removes all drawn paths and resets the signature
     * to an empty state. The undo/redo history is also cleared.
     */
    CLEAR,

    /**
     * Save/complete the current signature
     *
     * This action indicates that the user is satisfied with their
     * signature and wants to finalize it. The signature bitmap
     * will be provided to the completion callback.
     */
    SAVE,

    /**
     * Undo the last drawing operation
     *
     * This action reverts the signature to its previous state,
     * removing the most recent drawing operations. The undone
     * operations can be restored using REDO.
     */
    UNDO,

    /**
     * Redo the last undone operation
     *
     * This action restores the most recently undone drawing
     * operations. This is only available if UNDO has been
     * performed previously.
     */
    REDO;

    /**
     * Get a human-readable description of the action
     */
    public fun getDescription(): String = when (this) {
        CLEAR -> "Clear signature and start over"
        SAVE -> "Save and complete signature"
        UNDO -> "Undo last drawing operation"
        REDO -> "Redo last undone operation"
    }

    /**
     * Get the display name for UI elements
     */
    public fun getDisplayName(): String = when (this) {
        CLEAR -> "Clear"
        SAVE -> "Save"
        UNDO -> "Undo"
        REDO -> "Redo"
    }

    /**
     * Check if this action requires confirmation before execution
     */
    public fun requiresConfirmation(): Boolean = when (this) {
        CLEAR -> true // Destructive action
        SAVE -> false
        UNDO -> false
        REDO -> false
    }

    /**
     * Check if this action is destructive (cannot be easily reversed)
     */
    public fun isDestructive(): Boolean = when (this) {
        CLEAR -> true
        SAVE -> false
        UNDO -> false
        REDO -> false
    }
}

/**
 * Result of executing a signature action
 *
 * @property success Whether the action was executed successfully
 * @property message Optional message describing the result
 * @property action The action that was executed
 */
public data class SignatureActionResult(
    val success: Boolean,
    val message: String? = null,
    val action: SignatureAction,
) {
    public companion object {
        /**
         * Create a successful action result
         */
        public fun success(
            action: SignatureAction,
            message: String? = null,
        ): SignatureActionResult = SignatureActionResult(
            success = true,
            message = message,
            action = action,
        )

        /**
         * Create a failed action result
         */
        public fun failure(
            action: SignatureAction,
            message: String,
        ): SignatureActionResult = SignatureActionResult(
            success = false,
            message = message,
            action = action,
        )
    }
}

/**
 * Interface for handling signature actions with validation
 */
public interface SignatureActionHandler {
    /**
     * Execute a signature action with validation
     *
     * @param action The action to execute
     * @param state Current signature state
     * @return Result of the action execution
     */
    public fun executeAction(
        action: SignatureAction,
        state: SignatureState,
    ): SignatureActionResult

    /**
     * Check if an action can be executed in the current state
     *
     * @param action The action to validate
     * @param state Current signature state
     * @return True if the action can be executed
     */
    public fun canExecuteAction(
        action: SignatureAction,
        state: SignatureState,
    ): Boolean
}

/**
 * Default implementation of SignatureActionHandler
 */
public class DefaultSignatureActionHandler : SignatureActionHandler {

    override fun executeAction(
        action: SignatureAction,
        state: SignatureState,
    ): SignatureActionResult {
        if (!canExecuteAction(action, state)) {
            return SignatureActionResult.failure(
                action = action,
                message = "Action ${action.getDisplayName()} cannot be executed in current state",
            )
        }

        return try {
            when (action) {
                SignatureAction.CLEAR -> {
                    state.clear()
                    SignatureActionResult.success(
                        action = action,
                        message = "Signature cleared successfully",
                    )
                }

                SignatureAction.SAVE -> {
                    // Note: Actual save logic would be handled by the caller
                    SignatureActionResult.success(
                        action = action,
                        message = "Signature ready for saving",
                    )
                }

                SignatureAction.UNDO -> {
                    state.undo()
                    SignatureActionResult.success(
                        action = action,
                        message = "Last operation undone",
                    )
                }

                SignatureAction.REDO -> {
                    state.redo()
                    SignatureActionResult.success(
                        action = action,
                        message = "Operation redone",
                    )
                }
            }
        } catch (e: Exception) {
            SignatureActionResult.failure(
                action = action,
                message = "Failed to execute ${action.getDisplayName()}: ${e.message}",
            )
        }
    }

    override fun canExecuteAction(
        action: SignatureAction,
        state: SignatureState,
    ): Boolean = when (action) {
        SignatureAction.CLEAR -> !state.paths.isEmpty()
        SignatureAction.SAVE -> !state.paths.isEmpty()
        SignatureAction.UNDO -> state.canUndo
        SignatureAction.REDO -> state.canRedo
    }
}

/**
 * Action configuration for customizing action behavior
 *
 * @property showClear Whether to show the clear action
 * @property showSave Whether to show the save action
 * @property showUndo Whether to show the undo action
 * @property showRedo Whether to show the redo action
 * @property confirmDestructive Whether to confirm destructive actions
 * @property customLabels Custom labels for actions
 */
public data class SignatureActionConfig(
    val showClear: Boolean = true,
    val showSave: Boolean = true,
    val showUndo: Boolean = true,
    val showRedo: Boolean = true,
    val confirmDestructive: Boolean = true,
    val customLabels: Map<SignatureAction, String> = emptyMap(),
) {
    /**
     * Get the label for an action, using custom label if available
     */
    public fun getLabelForAction(action: SignatureAction): String {
        return customLabels[action] ?: action.getDisplayName()
    }

    /**
     * Check if an action should be shown
     */
    public fun shouldShowAction(action: SignatureAction): Boolean = when (action) {
        SignatureAction.CLEAR -> showClear
        SignatureAction.SAVE -> showSave
        SignatureAction.UNDO -> showUndo
        SignatureAction.REDO -> showRedo
    }

    /**
     * Get all actions that should be shown
     */
    public fun getVisibleActions(): List<SignatureAction> {
        return SignatureAction.entries.filter { shouldShowAction(it) }
    }

    public companion object {
        /**
         * Configuration showing only essential actions
         */
        public val Essential: SignatureActionConfig = SignatureActionConfig(
            showClear = true,
            showSave = true,
            showUndo = false,
            showRedo = false,
        )

        /**
         * Configuration showing all actions
         */
        public val Complete: SignatureActionConfig = SignatureActionConfig()

        /**
         * Configuration for read-only mode (only save)
         */
        public val ReadOnly: SignatureActionConfig = SignatureActionConfig(
            showClear = false,
            showSave = true,
            showUndo = false,
            showRedo = false,
        )

        /**
         * Configuration for editing mode (no save)
         */
        public val EditOnly: SignatureActionConfig = SignatureActionConfig(
            showClear = true,
            showSave = false,
            showUndo = true,
            showRedo = true,
        )
    }
}