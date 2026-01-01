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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration class that defines the visual appearance and behavioral properties of a signature pad.
 *
 * This immutable data class encapsulates all configurable aspects of the [ComposeSign] component,
 * including stroke styling, canvas appearance, grid overlay settings, and dimensional constraints.
 * The class provides builder-style methods for creating modified copies and includes several
 * predefined configurations for common use cases.
 *
 * Usage example:
 * ```kotlin
 * val config = SignatureConfig(
 *     strokeColor = Color.Blue,
 *     strokeWidth = 4.dp,
 *     showGrid = true
 * )
 * ComposeSign(
 *     onSignatureUpdate = { bitmap -> /* handle signature */ },
 *     config = config
 * )
 * ```
 *
 * @property strokeColor The color applied to signature strokes when drawing.
 *                       Defaults to [Color.Black].
 * @property strokeWidth The thickness of signature strokes in density-independent pixels.
 *                       Defaults to 3.dp.
 * @property backgroundColor The fill color of the signature pad canvas.
 *                           Defaults to [Color.White].
 * @property borderStroke Optional border definition for the signature pad container.
 *                        Set to null to remove the border. Defaults to a 1.dp gray border.
 * @property cornerShape The shape applied to the corners of the signature pad container.
 *                       Defaults to rounded corners with an 8.dp radius.
 * @property showGrid Whether to display a grid overlay on the canvas for visual guidance.
 *                    Defaults to false.
 * @property gridColor The color of grid lines when [showGrid] is enabled.
 *                     Defaults to semi-transparent gray.
 * @property gridSpacing The spacing between grid lines in density-independent pixels.
 *                       Defaults to 20.dp.
 * @property isFullScreen Whether the signature pad should expand to fill all available space.
 *                        When true, [minHeight] and [maxHeight] constraints are ignored.
 *                        Defaults to false.
 * @property minHeight The minimum height constraint for the signature pad when not in fullscreen mode.
 *                     Defaults to 200.dp.
 * @property maxHeight The maximum height constraint for the signature pad when not in fullscreen mode.
 *                     Defaults to 400.dp.
 * @property showActions Whether to display built-in action buttons below the signature pad.
 *                       Defaults to false.
 * @property enableSmoothDrawing Whether to apply smoothing algorithms to the drawn paths.
 *                               Defaults to true.
 *
 * @see ComposeSign
 * @see SignatureState
 */
@Immutable
public data class SignatureConfig(
    val strokeColor: Color = Color.Black,
    val strokeWidth: Dp = 3.dp,
    val backgroundColor: Color = Color.White,
    val borderStroke: BorderStroke? = BorderStroke(1.dp, Color.Gray),
    val cornerShape: CornerBasedShape = RoundedCornerShape(8.dp),
    val showGrid: Boolean = false,
    val gridColor: Color = Color.Gray.copy(alpha = 0.3f),
    val gridSpacing: Dp = 20.dp,
    val isFullScreen: Boolean = false,
    val minHeight: Dp = 200.dp,
    val maxHeight: Dp = 400.dp,
    val showActions: Boolean = false,
    val enableSmoothDrawing: Boolean = true
) {

    /**
     * Creates a copy of this configuration with fullscreen mode enabled.
     *
     * This method is a convenience builder for creating fullscreen signature pad configurations.
     * When fullscreen is enabled, the signature pad expands to fill all available space,
     * ignoring the [minHeight] and [maxHeight] constraints.
     *
     * @param showActions Whether to display action buttons in fullscreen mode. Defaults to true.
     * @return A new [SignatureConfig] with fullscreen mode enabled.
     */
    public fun asFullScreen(
        showActions: Boolean = true
    ): SignatureConfig = copy(
        isFullScreen = true,
        showActions = showActions
    )

    /**
     * Creates a copy of this configuration with modified grid settings.
     *
     * This method provides a convenient way to enable or customize the grid overlay
     * without manually specifying all grid-related properties.
     *
     * @param enabled Whether the grid should be displayed. Defaults to true.
     * @param color The color to use for grid lines. Defaults to semi-transparent gray.
     * @param spacing The spacing between grid lines. Defaults to 20.dp.
     * @return A new [SignatureConfig] with the specified grid settings.
     */
    public fun withGrid(
        enabled: Boolean = true,
        color: Color = Color.Gray.copy(alpha = 0.3f),
        spacing: Dp = 20.dp
    ): SignatureConfig = copy(
        showGrid = enabled,
        gridColor = color,
        gridSpacing = spacing
    )

    /**
     * Creates a copy of this configuration with modified stroke settings.
     *
     * This method allows convenient customization of the signature stroke appearance
     * without affecting other configuration properties.
     *
     * @param color The color for signature strokes. Defaults to the current [strokeColor].
     * @param width The thickness of signature strokes. Defaults to the current [strokeWidth].
     * @return A new [SignatureConfig] with the specified stroke settings.
     */
    public fun withStroke(
        color: Color = strokeColor,
        width: Dp = strokeWidth
    ): SignatureConfig = copy(
        strokeColor = color,
        strokeWidth = width
    )

    /**
     * Creates a copy of this configuration with modified visual appearance settings.
     *
     * This method enables bulk modification of the signature pad's visual properties
     * including background, border, and corner shape.
     *
     * @param backgroundColor The canvas background color. Defaults to the current [backgroundColor].
     * @param borderStroke The border definition, or null for no border. Defaults to the current [borderStroke].
     * @param cornerShape The corner shape for the container. Defaults to the current [cornerShape].
     * @return A new [SignatureConfig] with the specified appearance settings.
     */
    public fun withAppearance(
        backgroundColor: Color = this.backgroundColor,
        borderStroke: BorderStroke? = this.borderStroke,
        cornerShape: CornerBasedShape = this.cornerShape
    ): SignatureConfig = copy(
        backgroundColor = backgroundColor,
        borderStroke = borderStroke,
        cornerShape = cornerShape
    )

    /**
     * Predefined configuration instances for common use cases.
     */
    public companion object {

        /**
         * Default configuration providing a standard signature pad appearance.
         *
         * This configuration uses black strokes on a white background with a gray border
         * and no grid overlay. Suitable for most general-purpose signature capture scenarios.
         */
        public val Default: SignatureConfig = SignatureConfig()

        /**
         * Configuration optimized for fullscreen signature capture.
         *
         * This configuration enables fullscreen mode, displays action buttons,
         * and shows a grid overlay for visual guidance during signing.
         */
        public val Fullscreen: SignatureConfig = SignatureConfig(
            isFullScreen = true,
            showActions = true,
            showGrid = true
        )

        /**
         * Configuration with grid overlay enabled.
         *
         * This configuration displays a subtle grid pattern to help users align
         * and position their signature. Uses light gray grid lines with 25.dp spacing.
         */
        public val WithGrid: SignatureConfig = SignatureConfig(
            showGrid = true,
            gridColor = Color.Gray.copy(alpha = 0.2f),
            gridSpacing = 25.dp
        )

        /**
         * Configuration with increased stroke thickness.
         *
         * This configuration uses a 5.dp stroke width for more prominent,
         * bolder signatures. Suitable for applications requiring high visibility.
         */
        public val ThickStroke: SignatureConfig = SignatureConfig(
            strokeWidth = 5.dp,
            strokeColor = Color.Black
        )

        /**
         * Configuration optimized for integration within forms.
         *
         * This configuration uses subtle styling with a light gray background,
         * minimal corner rounding, and compact height constraints suitable for
         * embedding within form layouts.
         */
        public val FormIntegration: SignatureConfig = SignatureConfig(
            strokeWidth = 2.dp,
            backgroundColor = Color.Gray.copy(alpha = 0.05f),
            borderStroke = BorderStroke(1.dp, Color.Gray),
            cornerShape = RoundedCornerShape(4.dp),
            minHeight = 150.dp,
            maxHeight = 250.dp
        )

        /**
         * Configuration for professional document signing.
         *
         * This configuration uses a clean, formal appearance with sharp corners,
         * a prominent black border, and compact dimensions suitable for
         * legal or business document contexts.
         */
        public val Professional: SignatureConfig = SignatureConfig(
            strokeColor = Color.Black,
            strokeWidth = 2.dp,
            backgroundColor = Color.White,
            borderStroke = BorderStroke(2.dp, Color.Black),
            cornerShape = RoundedCornerShape(0.dp),
            showGrid = false,
            minHeight = 120.dp,
            maxHeight = 200.dp
        )

        /**
         * Configuration for creative or artistic signature capture.
         *
         * This configuration uses a distinctive blue color scheme, larger corner
         * radius, thicker strokes, and a coordinated grid overlay for a more
         * expressive signing experience.
         */
        public val Creative: SignatureConfig = SignatureConfig(
            strokeColor = Color.Blue,
            strokeWidth = 4.dp,
            backgroundColor = Color.Gray.copy(alpha = 0.1f),
            borderStroke = BorderStroke(2.dp, Color.Blue),
            cornerShape = RoundedCornerShape(16.dp),
            showGrid = true,
            gridColor = Color.Blue.copy(alpha = 0.2f),
            gridSpacing = 15.dp
        )
    }
}
