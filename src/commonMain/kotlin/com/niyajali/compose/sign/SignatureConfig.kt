package com.niyajali.compose.sign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration object for signature appearance and behavior
 *
 * @property strokeColor Color of the signature strokes
 * @property strokeWidth Width of the signature strokes in Dp
 * @property backgroundColor Background color of the signature pad
 * @property borderStroke Optional border stroke around the signature pad
 * @property cornerShape Shape of the signature pad corners
 * @property showGrid Whether to display grid lines over the signature pad
 * @property gridColor Color of the grid lines (when showGrid is true)
 * @property gridSpacing Spacing between grid lines in Dp
 * @property isFullScreen Whether the signature pad should fill the entire screen
 * @property minHeight Minimum height of the signature pad (when not fullscreen)
 * @property maxHeight Maximum height of the signature pad (when not fullscreen)
 * @property showActions Whether to show built-in action buttons
 * @property enableSmoothDrawing Whether to enable smooth drawing optimizations
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
     * Creates a copy of this configuration with modified fullscreen settings
     */
    public fun asFullScreen(
        showActions: Boolean = true
    ): SignatureConfig = copy(
        isFullScreen = true,
        showActions = showActions
    )

    /**
     * Creates a copy of this configuration with modified grid settings
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
     * Creates a copy of this configuration with modified stroke settings
     */
    public fun withStroke(
        color: Color = strokeColor,
        width: Dp = strokeWidth
    ): SignatureConfig = copy(
        strokeColor = color,
        strokeWidth = width
    )

    /**
     * Creates a copy of this configuration with modified appearance settings
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

    public companion object {

        /**
         * Default configuration for minimal signature pad
         */
        public val Default: SignatureConfig = SignatureConfig()

        /**
         * Configuration for fullscreen signature with actions
         */
        public val Fullscreen: SignatureConfig = SignatureConfig(
            isFullScreen = true,
            showActions = true,
            showGrid = true
        )

        /**
         * Configuration with grid enabled
         */
        public val WithGrid: SignatureConfig = SignatureConfig(
            showGrid = true,
            gridColor = Color.Gray.copy(alpha = 0.2f),
            gridSpacing = 25.dp
        )

        /**
         * Configuration for thick signature strokes
         */
        public val ThickStroke: SignatureConfig = SignatureConfig(
            strokeWidth = 5.dp,
            strokeColor = Color.Black
        )

        /**
         * Configuration for form integration
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
         * Configuration for professional documents
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
         * Configuration for creative/artistic signatures
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