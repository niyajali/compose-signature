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

import androidx.compose.ui.graphics.Color

// Primary Colors - Indigo to Purple gradient
val Primary = Color(0xFF6366F1)
val PrimaryDark = Color(0xFF4F46E5)
val PrimaryLight = Color(0xFF8B5CF6)
val PrimaryContainer = Color(0xFFEEF2FF)
val PrimaryContainerDark = Color(0xFF312E81)
val OnPrimary = Color(0xFFFFFFFF)
val OnPrimaryContainer = Color(0xFF1E1B4B)

// Secondary Colors - Pink to Orange gradient
val Secondary = Color(0xFFEC4899)
val SecondaryDark = Color(0xFFDB2777)
val SecondaryLight = Color(0xFFF97316)
val SecondaryContainer = Color(0xFFFDF2F8)
val SecondaryContainerDark = Color(0xFF831843)
val OnSecondary = Color(0xFFFFFFFF)
val OnSecondaryContainer = Color(0xFF831843)

// Tertiary Colors - Emerald to Teal gradient
val Tertiary = Color(0xFF10B981)
val TertiaryDark = Color(0xFF059669)
val TertiaryLight = Color(0xFF14B8A6)
val TertiaryContainer = Color(0xFFECFDF5)
val TertiaryContainerDark = Color(0xFF064E3B)
val OnTertiary = Color(0xFFFFFFFF)
val OnTertiaryContainer = Color(0xFF064E3B)

// Accent Colors
val AccentBlue = Color(0xFF3B82F6)
val AccentCyan = Color(0xFF06B6D4)
val AccentOrange = Color(0xFFF97316)
val AccentGold = Color(0xFFEAB308)
val AccentRose = Color(0xFFF43F5E)
val AccentPink = Color(0xFFFB7185)

// Neutral Colors
val Neutral50 = Color(0xFFFAFAFA)
val Neutral100 = Color(0xFFF5F5F5)
val Neutral200 = Color(0xFFE5E5E5)
val Neutral300 = Color(0xFFD4D4D4)
val Neutral400 = Color(0xFFA3A3A3)
val Neutral500 = Color(0xFF737373)
val Neutral600 = Color(0xFF525252)
val Neutral700 = Color(0xFF404040)
val Neutral800 = Color(0xFF262626)
val Neutral900 = Color(0xFF171717)

// Surface Colors
val Surface = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFF8FAFC)
val SurfaceDim = Color(0xFFF1F5F9)
val SurfaceBright = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF121212)
val SurfaceVariantDark = Color(0xFF1E1E1E)
val SurfaceDimDark = Color(0xFF2D2D2D)

// Background Colors
val Background = Color(0xFFFAFBFC)
val BackgroundDark = Color(0xFF0A0A0A)

// On Surface Colors
val OnSurface = Color(0xFF1F2937)
val OnSurfaceVariant = Color(0xFF6B7280)
val OnBackground = Color(0xFF1F2937)
val OnSurfaceDark = Color(0xFFE5E7EB)
val OnSurfaceVariantDark = Color(0xFF9CA3AF)
val OnBackgroundDark = Color(0xFFE5E7EB)

// Status Colors
val Success = Color(0xFF10B981)
val SuccessContainer = Color(0xFFD1FAE5)
val OnSuccess = Color(0xFFFFFFFF)
val OnSuccessContainer = Color(0xFF064E3B)

val Warning = Color(0xFFF59E0B)
val WarningContainer = Color(0xFFFEF3C7)
val OnWarning = Color(0xFFFFFFFF)
val OnWarningContainer = Color(0xFF78350F)

val Error = Color(0xFFEF4444)
val ErrorContainer = Color(0xFFFEE2E2)
val OnError = Color(0xFFFFFFFF)
val OnErrorContainer = Color(0xFF7F1D1D)

val Info = Color(0xFF3B82F6)
val InfoContainer = Color(0xFFDBEAFE)
val OnInfo = Color(0xFFFFFFFF)
val OnInfoContainer = Color(0xFF1E3A8A)

// Outline Colors
val Outline = Color(0xFFD1D5DB)
val OutlineVariant = Color(0xFFE5E7EB)
val OutlineDark = Color(0xFF374151)
val OutlineVariantDark = Color(0xFF4B5563)

// Sample Screen Colors (matching plan)
object SampleColors {
    val BasicBlue = AccentBlue
    val BasicCyan = AccentCyan

    val CustomStylePink = Secondary
    val CustomStyleOrange = AccentOrange

    val GridGreen = Tertiary
    val GridTeal = TertiaryLight

    val ActionsPurple = PrimaryLight
    val ActionsIndigo = Primary

    val ExportOrange = AccentOrange
    val ExportGold = AccentGold

    val ValidationTeal = TertiaryLight
    val ValidationCyan = AccentCyan

    val DarkPurple = Color(0xFF7C3AED)
    val DarkIndigo = PrimaryDark

    val FullscreenRose = AccentRose
    val FullscreenPink = AccentPink
}
