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
package com.niyajali.compose.sign.sample.components.animations

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/**
 * Animation timing constants
 */
object AnimationDurations {
    const val FAST = 150
    const val NORMAL = 300
    const val SLOW = 400
    const val VERY_SLOW = 500
    const val PAGE_TRANSITION = 400
}

/**
 * Stagger delays for sequential animations
 */
object AnimationDelays {
    const val STAGGER_SHORT = 50L
    const val STAGGER_MEDIUM = 100L
    const val STAGGER_LONG = 150L
}

/**
 * Predefined animation specs for common use cases
 */
object AnimationSpecs {
    // Fast out slow in - Good for standard UI transitions
    val fastOutSlowIn = tween<Float>(
        durationMillis = AnimationDurations.NORMAL,
        easing = FastOutSlowInEasing,
    )

    // Fast out linear in - Good for exit animations
    val fastOutLinearIn = tween<Float>(
        durationMillis = AnimationDurations.FAST,
        easing = FastOutLinearInEasing,
    )

    // Spring animation - Good for bouncy, organic feel
    val bounce = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow,
    )

    val mediumBounce = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium,
    )

    val lowBounce = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )

    // Standard spring - Good for most interactive elements
    val spring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium,
    )
}

/**
 * Icon animation types
 */
enum class IconAnimation {
    None, // No animation
    Scale, // Scale from 0.8 to 1.0
    Rotation, // Rotate from -15° to 0°
    Fade, // Fade from 0.0 to 1.0
    Bounce, // Bouncy scale animation
    Pulse, // Continuous pulsing animation
}
