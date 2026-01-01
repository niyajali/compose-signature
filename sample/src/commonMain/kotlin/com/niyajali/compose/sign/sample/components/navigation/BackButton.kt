package com.niyajali.compose.sign.sample.components.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.niyajali.compose.sign.sample.components.icons.IconMapper
import com.niyajali.compose.sign.sample.theme.Size
import com.niyajali.compose.sign.sample.utils.Strings
import org.jetbrains.compose.resources.painterResource

/**
 * Animated back button with icon
 *
 * @param onClick Click handler for back navigation
 * @param modifier Modifier for the button
 * @param tint Color tint for the icon
 */
@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        label = "back_button_scale"
    )

    IconButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .scale(scale)
                .size(Size.iconMD),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(IconMapper.Icons.arrowBack),
                contentDescription = Strings.cdBackButton(),
                tint = tint
            )
        }
    }
}
