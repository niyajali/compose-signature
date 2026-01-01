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

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

/**
 * Page transition animations for navigation
 */
object PageTransitions {
    /**
     * Forward navigation transition (slide in from right)
     */
    fun <S> AnimatedContentTransitionScope<S>.slideInFromRight(): ContentTransform {
        return slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION),
        ) + fadeIn(
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION),
        ) togetherWith slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION),
        ) + fadeOut(
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION),
        )
    }

    /**
     * Backward navigation transition (slide out to right)
     */
    fun <S> AnimatedContentTransitionScope<S>.slideOutToRight(): ContentTransform {
        return slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(AnimationDurations.NORMAL),
        ) + fadeIn(
            animationSpec = tween(AnimationDurations.NORMAL),
        ) togetherWith slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(AnimationDurations.NORMAL),
        ) + fadeOut(
            animationSpec = tween(AnimationDurations.NORMAL),
        )
    }

    /**
     * Fade transition (for simple transitions)
     */
    fun <S> AnimatedContentTransitionScope<S>.fadeTransition(): ContentTransform {
        return fadeIn(
            animationSpec = tween(AnimationDurations.NORMAL),
        ) togetherWith fadeOut(
            animationSpec = tween(AnimationDurations.NORMAL),
        )
    }
}
