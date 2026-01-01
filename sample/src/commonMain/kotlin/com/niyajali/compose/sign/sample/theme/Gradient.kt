/**
 * Copyright 2026 Sk Niyaj Ali
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.niyajali.compose.sign.sample.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Gradient definitions for the app
 *
 * Gradient Strategy:
 * - Cards: Subtle diagonal gradients (5-10% shift)
 * - Buttons: Bold horizontal gradients (30% shift)
 * - Backgrounds: Very subtle vertical gradients (5% shift)
 * - Text: Moderate horizontal gradients on titles only (20% shift)
 */
object Gradients {
    // Primary Gradients (Indigo → Purple)
    val primaryHorizontal = Brush.horizontalGradient(
        colors = listOf(Primary, PrimaryLight),
    )

    val primaryVertical = Brush.verticalGradient(
        colors = listOf(Primary, PrimaryLight),
    )

    val primaryDiagonal = Brush.linearGradient(
        colors = listOf(Primary, PrimaryLight),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    // Secondary Gradients (Pink → Orange)
    val secondaryHorizontal = Brush.horizontalGradient(
        colors = listOf(Secondary, SecondaryLight),
    )

    val secondaryVertical = Brush.verticalGradient(
        colors = listOf(Secondary, SecondaryLight),
    )

    val secondaryDiagonal = Brush.linearGradient(
        colors = listOf(Secondary, SecondaryLight),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    // Tertiary Gradients (Emerald → Teal)
    val tertiaryHorizontal = Brush.horizontalGradient(
        colors = listOf(Tertiary, TertiaryLight),
    )

    val tertiaryVertical = Brush.verticalGradient(
        colors = listOf(Tertiary, TertiaryLight),
    )

    val tertiaryDiagonal = Brush.linearGradient(
        colors = listOf(Tertiary, TertiaryLight),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    // Sample Screen Specific Gradients
    object Sample {
        // Basic - Blue gradient
        val basic = Brush.linearGradient(
            colors = listOf(SampleColors.BasicBlue, SampleColors.BasicCyan),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val basicSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.BasicBlue.copy(alpha = 0.1f),
                SampleColors.BasicCyan.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        // Custom Style - Pink gradient
        val customStyle = Brush.linearGradient(
            colors = listOf(SampleColors.CustomStylePink, SampleColors.CustomStyleOrange),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val customStyleSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.CustomStylePink.copy(alpha = 0.1f),
                SampleColors.CustomStyleOrange.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        // Grid - Green gradient
        val grid = Brush.linearGradient(
            colors = listOf(SampleColors.GridGreen, SampleColors.GridTeal),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val gridSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.GridGreen.copy(alpha = 0.1f),
                SampleColors.GridTeal.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        // Actions - Purple gradient
        val actions = Brush.linearGradient(
            colors = listOf(SampleColors.ActionsPurple, SampleColors.ActionsIndigo),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val actionsSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.ActionsPurple.copy(alpha = 0.1f),
                SampleColors.ActionsIndigo.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        // Export - Orange gradient
        val export = Brush.linearGradient(
            colors = listOf(SampleColors.ExportOrange, SampleColors.ExportGold),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val exportSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.ExportOrange.copy(alpha = 0.1f),
                SampleColors.ExportGold.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        // Validation - Teal gradient
        val validation = Brush.linearGradient(
            colors = listOf(SampleColors.ValidationTeal, SampleColors.ValidationCyan),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val validationSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.ValidationTeal.copy(alpha = 0.1f),
                SampleColors.ValidationCyan.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        // Dark Theme - Dark purple gradient
        val darkTheme = Brush.linearGradient(
            colors = listOf(SampleColors.DarkPurple, SampleColors.DarkIndigo),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val darkThemeSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.DarkPurple.copy(alpha = 0.1f),
                SampleColors.DarkIndigo.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        // Fullscreen - Rose gradient
        val fullscreen = Brush.linearGradient(
            colors = listOf(SampleColors.FullscreenRose, SampleColors.FullscreenPink),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )

        val fullscreenSubtle = Brush.linearGradient(
            colors = listOf(
                SampleColors.FullscreenRose.copy(alpha = 0.1f),
                SampleColors.FullscreenPink.copy(alpha = 0.1f),
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
    }

    // Card Gradients (Subtle for backgrounds)
    val cardPrimary = Brush.linearGradient(
        colors = listOf(
            PrimaryContainer,
            PrimaryContainer.copy(alpha = 0.7f),
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    val cardSecondary = Brush.linearGradient(
        colors = listOf(
            SecondaryContainer,
            SecondaryContainer.copy(alpha = 0.7f),
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    val cardTertiary = Brush.linearGradient(
        colors = listOf(
            TertiaryContainer,
            TertiaryContainer.copy(alpha = 0.7f),
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    // Background Gradients (Very subtle)
    val backgroundMesh = Brush.verticalGradient(
        colors = listOf(
            Background,
            SurfaceVariant,
        ),
    )

    val backgroundMeshDark = Brush.verticalGradient(
        colors = listOf(
            BackgroundDark,
            SurfaceVariantDark,
        ),
    )

    // Surface Glow (Radial gradient for focus areas)
    val surfaceGlow = Brush.radialGradient(
        colors = listOf(
            Primary.copy(alpha = 0.1f),
            Color.Transparent,
        ),
    )

    // Button Gradients (Bold)
    val buttonPrimary = Brush.horizontalGradient(
        colors = listOf(Primary, PrimaryLight),
    )

    val buttonSecondary = Brush.horizontalGradient(
        colors = listOf(Secondary, SecondaryLight),
    )

    val buttonTertiary = Brush.horizontalGradient(
        colors = listOf(Tertiary, TertiaryLight),
    )

    // Status Gradients
    val success = Brush.horizontalGradient(
        colors = listOf(Success, TertiaryLight),
    )

    val warning = Brush.horizontalGradient(
        colors = listOf(Warning, AccentOrange),
    )

    val error = Brush.horizontalGradient(
        colors = listOf(Error, AccentRose),
    )

    val info = Brush.horizontalGradient(
        colors = listOf(Info, AccentCyan),
    )
}

/**
 * Helper function to create a diagonal gradient brush
 */
fun diagonalGradient(colors: List<Color>): Brush {
    return Brush.linearGradient(
        colors = colors.toList(),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )
}

/**
 * Helper function to create a subtle gradient with reduced alpha
 */
fun subtleGradient(colors: List<Color>, alpha: Float = 0.1f): Brush {
    return Brush.linearGradient(
        colors = colors.map { it.copy(alpha = alpha) },
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )
}
