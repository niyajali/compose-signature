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
package com.niyajali.compose.sign.sample.components.icons

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.niyajali.compose.sign.sample.components.animations.AnimationSpecs
import com.niyajali.compose.sign.sample.components.animations.IconAnimation
import com.niyajali.compose.sign.sample.theme.Size
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Animated icon wrapper with support for various animation types
 *
 * @param icon The drawable resource for the icon
 * @param contentDescription Content description for accessibility
 * @param modifier Modifier for the icon
 * @param size Size of the icon
 * @param tint Tint color for the icon (null for no tinting)
 * @param animationType Type of animation to apply
 * @param isVisible Whether the icon is visible (used for visibility animations)
 * @param onClick Optional click handler
 */
@Composable
fun AnimatedIcon(
    icon: DrawableResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = Size.iconMD,
    tint: Color? = null,
    animationType: IconAnimation = IconAnimation.Scale,
    isVisible: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val iconColor = tint ?: LocalContentColor.current

    // Animation values based on type
    val scale by animateFloatAsState(
        targetValue = when {
            !isVisible -> 0.8f
            animationType == IconAnimation.Scale -> 1f
            animationType == IconAnimation.Bounce -> 1f
            else -> 1f
        },
        animationSpec = when (animationType) {
            IconAnimation.Bounce -> AnimationSpecs.bounce
            IconAnimation.Scale -> AnimationSpecs.mediumBounce
            else -> AnimationSpecs.spring
        },
        label = "icon_scale",
    )

    val rotation by animateFloatAsState(
        targetValue = when {
            !isVisible -> -15f
            animationType == IconAnimation.Rotation -> 0f
            else -> 0f
        },
        animationSpec = AnimationSpecs.fastOutSlowIn,
        label = "icon_rotation",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = AnimationSpecs.fastOutSlowIn,
        label = "icon_alpha",
    )

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .scale(scale)
            .rotate(rotation)
            .alpha(alpha),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(size),
        )
    }
}
