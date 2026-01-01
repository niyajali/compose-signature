package com.niyajali.compose.sign.sample.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight

/**
 * Custom top app bar with gradient background
 *
 * @param title Title text to display
 * @param modifier Modifier for the top bar
 * @param gradient Gradient brush for the background (null for solid color)
 * @param navigationIcon Navigation icon composable (typically a back button)
 * @param actions Action buttons to display on the right
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    gradient: Brush? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (gradient != null) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = if (gradient != null) {
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            TopAppBarDefaults.topAppBarColors()
        },
        modifier = if (gradient != null) {
            modifier.background(gradient)
        } else {
            modifier
        }
    )
}
