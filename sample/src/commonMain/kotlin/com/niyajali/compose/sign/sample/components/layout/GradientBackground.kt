package com.niyajali.compose.sign.sample.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.niyajali.compose.sign.sample.theme.Gradients

/**
 * Full-screen gradient background component
 *
 * @param gradient Gradient brush for the background
 * @param modifier Modifier for the background
 * @param content Composable content to display on top of the gradient
 */
@Composable
fun GradientBackground(
    gradient: Brush = Gradients.backgroundMesh,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        content()
    }
}
