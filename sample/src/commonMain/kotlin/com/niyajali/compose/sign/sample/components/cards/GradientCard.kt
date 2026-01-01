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
    content: @Composable ColumnScope.() -> Unit,
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
                },
            ),
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation,
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor ?: MaterialTheme.colorScheme.surface,
        ),
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
                    },
                )
                .padding(Spacing.md),
        ) {
            content()
        }
    }
}
