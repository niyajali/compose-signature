package com.niyajali.compose.sign.sample.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

/**
 * Material 3 shape system with custom corner radii
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(CornerRadius.xs),
    small = RoundedCornerShape(CornerRadius.sm),
    medium = RoundedCornerShape(CornerRadius.md),
    large = RoundedCornerShape(CornerRadius.lg),
    extraLarge = RoundedCornerShape(CornerRadius.xl)
)
