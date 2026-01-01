package com.niyajali.compose.sign.sample.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.niyajali.compose.sign.sample.components.animations.IconAnimation
import com.niyajali.compose.sign.sample.components.icons.AnimatedIcon
import com.niyajali.compose.sign.sample.theme.Size
import com.niyajali.compose.sign.sample.theme.Spacing
import org.jetbrains.compose.resources.DrawableResource

/**
 * Section header with optional icon
 *
 * @param title Header title text
 * @param modifier Modifier for the header
 * @param icon Optional icon to display before the title
 * @param subtitle Optional subtitle text
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    subtitle: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.sm),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon if provided
        if (icon != null) {
            AnimatedIcon(
                icon = icon,
                contentDescription = title,
                size = Size.iconLG,
                tint = MaterialTheme.colorScheme.primary,
                animationType = IconAnimation.Scale
            )
            Spacer(modifier = Modifier.width(Spacing.sm))
        }

        // Title and subtitle
        if (subtitle != null) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Small section header variant
 *
 * @param title Header title text
 * @param modifier Modifier for the header
 * @param icon Optional icon to display before the title
 */
@Composable
fun SmallSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.xs),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon if provided
        if (icon != null) {
            AnimatedIcon(
                icon = icon,
                contentDescription = title,
                size = Size.iconMD,
                tint = MaterialTheme.colorScheme.primary,
                animationType = IconAnimation.Scale
            )
            Spacer(modifier = Modifier.width(Spacing.xs))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
