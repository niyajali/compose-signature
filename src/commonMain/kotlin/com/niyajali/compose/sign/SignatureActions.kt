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

import androidx.compose.runtime.Immutable

/**
 * Enumeration of available actions that can be performed on a signature pad.
 *
 * This enum defines the standard set of operations users can perform when interacting
 * with a signature component, including clearing the canvas, saving the signature,
 * and undo/redo functionality for editing support.
 *
 * Each action provides metadata methods to describe its behavior, display properties,
 * and characteristics such as whether it requires user confirmation or is destructive.
 */
public enum class SignatureAction {
    /**
     * Clears all drawn content from the signature pad, resetting it to an empty state.
     * This is a destructive action that removes all signature paths.
     */
    CLEAR,

    /**
     * Saves the current signature, typically triggering export or completion workflows.
     * This action is only meaningful when the signature pad contains content.
     */
    SAVE,

    /**
     * Reverts the most recent drawing operation, restoring the previous signature state.
     * This action requires undo history to be available.
     */
    UNDO,

    /**
     * Reapplies a previously undone drawing operation.
     * This action requires redo history to be available.
     */
    REDO;

    /**
     * Returns a human-readable description of the action's purpose and behavior.
     *
     * @return A descriptive string explaining what the action does when executed.
     */
    public fun getDescription(): String = when (this) {
        CLEAR -> "Clear signature and start over"
        SAVE -> "Save and complete signature"
        UNDO -> "Undo last drawing operation"
        REDO -> "Redo last undone operation"
    }

    /**
     * Returns the display name suitable for use in user interface elements such as buttons.
     *
     * @return A short, user-friendly label for the action.
     */
    public fun getDisplayName(): String = when (this) {
        CLEAR -> "Clear"
        SAVE -> "Save"
        UNDO -> "Undo"
        REDO -> "Redo"
    }

    /**
     * Indicates whether this action should prompt for user confirmation before execution.
     *
     * Destructive actions that cannot be easily reversed typically require confirmation
     * to prevent accidental data loss.
     *
     * @return True if the action should display a confirmation dialog before executing.
     */
    public fun requiresConfirmation(): Boolean = when (this) {
        CLEAR -> true
        SAVE -> false
        UNDO -> false
        REDO -> false
    }

    /**
     * Indicates whether this action is destructive and may result in data loss.
     *
     * Destructive actions permanently remove or modify data in ways that may not be
     * fully recoverable. User interfaces may use this information to apply special
     * styling or warnings.
     *
     * @return True if the action may cause irreversible data loss.
     */
    public fun isDestructive(): Boolean = when (this) {
        CLEAR -> true
        SAVE -> false
        UNDO -> false
        REDO -> false
    }
}

/**
 * Represents the result of executing a signature action.
 *
 * This immutable data class encapsulates the outcome of a [SignatureAction] execution,
 * including success status, optional message, and the action that was attempted.
 * It is used by [SignatureActionHandler] implementations to communicate action results.
 *
 * @property success Indicates whether the action was executed successfully.
 * @property message Optional human-readable message providing additional context about
 *                   the result, such as error details or confirmation text.
 * @property action The [SignatureAction] that was attempted.
 */
@Immutable
public data class SignatureActionResult(
    val success: Boolean,
    val message: String? = null,
    val action: SignatureAction,
) {
    /**
     * Factory methods for creating [SignatureActionResult] instances.
     */
    public companion object {
        /**
         * Creates a successful action result.
         *
         * @param action The action that was executed successfully.
         * @param message Optional message describing the successful outcome.
         * @return A [SignatureActionResult] indicating success.
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
         * Creates a failed action result.
         *
         * @param action The action that failed to execute.
         * @param message A message describing why the action failed.
         * @return A [SignatureActionResult] indicating failure.
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
 * Interface defining the contract for handling signature actions.
 *
 * Implementations of this interface are responsible for executing [SignatureAction] operations
 * on a [SignatureState] and determining whether actions can be executed in the current state.
 * This abstraction allows for custom action handling logic, such as adding confirmation dialogs,
 * logging, or additional validation.
 *
 * @see DefaultSignatureActionHandler
 */
public interface SignatureActionHandler {
    /**
     * Executes the specified action on the given signature state.
     *
     * Implementations should perform the action and return a result indicating success or failure.
     * The action should only be executed if [canExecuteAction] returns true for the same parameters.
     *
     * @param action The action to execute.
     * @param state The signature state on which to perform the action.
     * @return A [SignatureActionResult] indicating the outcome of the action execution.
     */
    public fun executeAction(
        action: SignatureAction,
        state: SignatureState,
    ): SignatureActionResult

    /**
     * Determines whether the specified action can be executed in the current state.
     *
     * This method should be used to enable or disable action controls in the user interface.
     * It checks preconditions such as undo/redo availability or signature presence.
     *
     * @param action The action to check.
     * @param state The current signature state.
     * @return True if the action can be executed, false otherwise.
     */
    public fun canExecuteAction(
        action: SignatureAction,
        state: SignatureState,
    ): Boolean
}

/**
 * Default implementation of [SignatureActionHandler] providing standard action execution logic.
 *
 * This handler implements the expected behavior for all [SignatureAction] types:
 * - [SignatureAction.CLEAR]: Clears all paths from the signature state
 * - [SignatureAction.SAVE]: Marks the signature as ready for saving (no state modification)
 * - [SignatureAction.UNDO]: Reverts the most recent stroke
 * - [SignatureAction.REDO]: Reapplies the most recently undone stroke
 *
 * Action execution includes validation via [canExecuteAction] and proper error handling
 * to ensure robust operation.
 */
public class DefaultSignatureActionHandler : SignatureActionHandler {

    /**
     * Executes the specified action on the given signature state.
     *
     * This implementation first validates that the action can be executed using [canExecuteAction].
     * If validation fails, a failure result is returned without modifying the state.
     * Otherwise, the action is performed and an appropriate result is returned.
     *
     * @param action The action to execute.
     * @param state The signature state on which to perform the action.
     * @return A [SignatureActionResult] indicating success or failure with an appropriate message.
     */
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

    /**
     * Determines whether the specified action can be executed in the current state.
     *
     * Validation rules:
     * - [SignatureAction.CLEAR]: Requires at least one path in the signature
     * - [SignatureAction.SAVE]: Requires at least one path in the signature
     * - [SignatureAction.UNDO]: Requires undo history to be available
     * - [SignatureAction.REDO]: Requires redo history to be available
     *
     * @param action The action to check.
     * @param state The current signature state.
     * @return True if the action can be executed, false otherwise.
     */
    override fun canExecuteAction(
        action: SignatureAction,
        state: SignatureState,
    ): Boolean = when (action) {
        SignatureAction.CLEAR -> state.paths.isNotEmpty()
        SignatureAction.SAVE -> state.paths.isNotEmpty()
        SignatureAction.UNDO -> state.canUndo
        SignatureAction.REDO -> state.canRedo
    }
}

/**
 * Configuration for controlling which signature actions are available and how they are displayed.
 *
 * This immutable data class allows fine-grained control over the visibility and behavior
 * of action buttons in the signature component. It also supports custom labels for
 * localization or branding purposes.
 *
 * @property showClear Whether the clear action button should be displayed.
 * @property showSave Whether the save action button should be displayed.
 * @property showUndo Whether the undo action button should be displayed.
 * @property showRedo Whether the redo action button should be displayed.
 * @property confirmDestructive Whether destructive actions should prompt for confirmation.
 * @property customLabels Map of custom labels for action buttons, keyed by [SignatureAction].
 *                        Actions not present in this map will use their default display names.
 */
@Immutable
public data class SignatureActionConfig(
    val showClear: Boolean = true,
    val showSave: Boolean = true,
    val showUndo: Boolean = true,
    val showRedo: Boolean = true,
    val confirmDestructive: Boolean = true,
    val customLabels: Map<SignatureAction, String> = emptyMap(),
) {
    /**
     * Returns the label to display for the specified action.
     *
     * If a custom label has been configured for the action, it is returned.
     * Otherwise, the action's default display name is used.
     *
     * @param action The action for which to retrieve the label.
     * @return The configured custom label or the action's default display name.
     */
    public fun getLabelForAction(action: SignatureAction): String {
        return customLabels[action] ?: action.getDisplayName()
    }

    /**
     * Determines whether the specified action should be shown in the user interface.
     *
     * @param action The action to check.
     * @return True if the action should be displayed, false if it should be hidden.
     */
    public fun shouldShowAction(action: SignatureAction): Boolean = when (action) {
        SignatureAction.CLEAR -> showClear
        SignatureAction.SAVE -> showSave
        SignatureAction.UNDO -> showUndo
        SignatureAction.REDO -> showRedo
    }

    /**
     * Returns a list of all actions that should be visible based on the current configuration.
     *
     * @return A filtered list of [SignatureAction] values that are configured to be shown.
     */
    public fun getVisibleActions(): List<SignatureAction> {
        return SignatureAction.entries.filter { shouldShowAction(it) }
    }

    /**
     * Predefined action configurations for common use cases.
     */
    public companion object {
        /**
         * Configuration showing only essential actions: Clear and Save.
         *
         * This configuration is suitable for simple signature capture scenarios
         * where undo/redo functionality is not needed.
         */
        public val Essential: SignatureActionConfig = SignatureActionConfig(
            showClear = true,
            showSave = true,
            showUndo = false,
            showRedo = false,
        )

        /**
         * Configuration showing all available actions.
         *
         * This is the default configuration providing full functionality including
         * Clear, Save, Undo, and Redo actions.
         */
        public val Complete: SignatureActionConfig = SignatureActionConfig()

        /**
         * Configuration for read-only or review scenarios.
         *
         * Shows only the Save button, preventing modification of the signature
         * while still allowing the user to confirm and save.
         */
        public val ReadOnly: SignatureActionConfig = SignatureActionConfig(
            showClear = false,
            showSave = true,
            showUndo = false,
            showRedo = false,
        )

        /**
         * Configuration for editing scenarios without save functionality.
         *
         * Shows Clear, Undo, and Redo actions but hides Save, suitable for
         * workflows where saving is handled separately.
         */
        public val EditOnly: SignatureActionConfig = SignatureActionConfig(
            showClear = true,
            showSave = false,
            showUndo = true,
            showRedo = true,
        )
    }
}
