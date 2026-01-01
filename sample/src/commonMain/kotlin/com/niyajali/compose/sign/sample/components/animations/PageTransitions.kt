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
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION)
        ) + fadeIn(
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION)
        ) togetherWith slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION)
        ) + fadeOut(
            animationSpec = tween(AnimationDurations.PAGE_TRANSITION)
        )
    }

    /**
     * Backward navigation transition (slide out to right)
     */
    fun <S> AnimatedContentTransitionScope<S>.slideOutToRight(): ContentTransform {
        return slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(AnimationDurations.NORMAL)
        ) + fadeIn(
            animationSpec = tween(AnimationDurations.NORMAL)
        ) togetherWith slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(AnimationDurations.NORMAL)
        ) + fadeOut(
            animationSpec = tween(AnimationDurations.NORMAL)
        )
    }

    /**
     * Fade transition (for simple transitions)
     */
    fun <S> AnimatedContentTransitionScope<S>.fadeTransition(): ContentTransform {
        return fadeIn(
            animationSpec = tween(AnimationDurations.NORMAL)
        ) togetherWith fadeOut(
            animationSpec = tween(AnimationDurations.NORMAL)
        )
    }
}
