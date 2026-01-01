package com.niyajali.compose.sign.sample.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.niyajali.compose.sign.sample.theme.CornerRadius
import com.niyajali.compose.sign.sample.theme.Elevation
import com.niyajali.compose.sign.sample.theme.Spacing

/**
 * Base card component with gradient background and elevation
 *
 * @param modifier Modifier for the card
 * @param gradient Gradient brush for the background
 * @param backgroundColor Solid background color (overrides gradient if set)
 * @param elevation Elevation for the card
 * @param cornerRadius Corner radius for the card
 * @param onClick Optional click handler for the card
 * @param content Composable content for the card
 */
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradient: Brush? = null,
    backgroundColor: Color? = null,
    elevation: Dp = Elevation.sm,
    cornerRadius: Dp = CornerRadius.lg,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor ?: MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (gradient != null && backgroundColor == null) {
                        Modifier
                            .clip(shape)
                            .background(gradient)
                    } else {
                        Modifier
                    }
                )
                .padding(Spacing.md)
        ) {
            content()
        }
    }
}
