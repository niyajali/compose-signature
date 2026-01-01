package com.niyajali.compose.sign.sample.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing system for consistent padding, margin, and gap values
 */
object Spacing {
    val xxxs: Dp = 2.dp
    val xxs: Dp = 4.dp
    val xs: Dp = 8.dp
    val sm: Dp = 12.dp
    val md: Dp = 16.dp
    val lg: Dp = 24.dp
    val xl: Dp = 32.dp
    val xxl: Dp = 48.dp
    val xxxl: Dp = 64.dp
}

/**
 * Size definitions for components
 */
object Size {
    // Icon sizes
    val iconXS: Dp = 16.dp
    val iconSM: Dp = 20.dp
    val iconMD: Dp = 24.dp
    val iconLG: Dp = 32.dp
    val iconXL: Dp = 48.dp
    val iconXXL: Dp = 96.dp

    // Component sizes
    val buttonHeight: Dp = 48.dp
    val buttonHeightSmall: Dp = 36.dp
    val cardHeight: Dp = 120.dp
    val signaturePadHeight: Dp = 250.dp
    val topBarHeight: Dp = 64.dp

    // Touch targets
    val minTouchTarget: Dp = 48.dp
}

/**
 * Corner radius definitions for shapes
 */
object CornerRadius {
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
    val circular: Dp = 50.dp
}

/**
 * Elevation levels for Material Design
 */
object Elevation {
    val none: Dp = 0.dp
    val xs: Dp = 1.dp
    val sm: Dp = 2.dp
    val md: Dp = 4.dp
    val lg: Dp = 8.dp
    val xl: Dp = 16.dp
}

/**
 * Border widths
 */
object BorderWidth {
    val thin: Dp = 1.dp
    val medium: Dp = 2.dp
    val thick: Dp = 3.dp
}
