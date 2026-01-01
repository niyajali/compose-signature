package com.niyajali.compose.sign.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.SignatureConfig
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.rememberSignatureState
import com.niyajali.compose.sign.sample.components.cards.GradientCard
import com.niyajali.compose.sign.sample.components.icons.IconMapper
import com.niyajali.compose.sign.sample.components.layout.SectionHeader
import com.niyajali.compose.sign.sample.components.layout.SmallSectionHeader
import com.niyajali.compose.sign.sample.theme.CornerRadius
import com.niyajali.compose.sign.sample.theme.Elevation
import com.niyajali.compose.sign.sample.theme.Gradients
import com.niyajali.compose.sign.sample.theme.Size
import com.niyajali.compose.sign.sample.theme.Spacing
import com.niyajali.compose.sign.sample.utils.Strings
import org.jetbrains.compose.resources.painterResource

enum class GridColorOption(val color: Color, val displayName: String) {
    GRAY(Color.Gray.copy(alpha = 0.3f), "Gray"),
    BLUE(Color.Blue.copy(alpha = 0.2f), "Blue"),
    GREEN(Color.Green.copy(alpha = 0.2f), "Green"),
    RED(Color.Red.copy(alpha = 0.2f), "Red")
}

@Composable
fun GridEnabledSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    var showGrid by remember { mutableStateOf(true) }
    var gridSpacing by remember { mutableFloatStateOf(20f) }
    var selectedGridColor by remember { mutableStateOf(GridColorOption.GRAY) }

    val config = SignatureConfig(
        showGrid = showGrid,
        gridColor = selectedGridColor.color,
        gridSpacing = gridSpacing.dp
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Gradients.Sample.gridSubtle)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Header with icon
        SectionHeader(
            title = Strings.gridTitle(),
            icon = IconMapper.getScreenIcon(SampleScreen.WITH_GRID)
        )

        // Description
        Text(
            text = Strings.gridDescription(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Signature Pad
        GradientCard(
            gradient = Gradients.Sample.gridSubtle,
            elevation = Elevation.md
        ) {
            ComposeSign(
                onSignatureUpdate = { bitmap ->
                    capturedSignature = bitmap
                },
                config = config,
                state = signatureState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size.signaturePadHeight)
            )
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            // Clear Button
            IconButton(
                onClick = { signatureState.clear() },
                enabled = !signatureState.isEmpty(),
                modifier = Modifier
                    .weight(1f)
                    .height(Size.buttonHeight)
                    .clip(RoundedCornerShape(CornerRadius.md))
                    .background(
                        if (!signatureState.isEmpty())
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(IconMapper.Actions.clear),
                        contentDescription = Strings.actionClear(),
                        tint = if (!signatureState.isEmpty())
                            MaterialTheme.colorScheme.onErrorContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(Size.iconMD)
                    )
                    Text(
                        text = Strings.actionClear(),
                        color = if (!signatureState.isEmpty())
                            MaterialTheme.colorScheme.onErrorContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Undo Button
            IconButton(
                onClick = { signatureState.undo() },
                enabled = signatureState.canUndo,
                modifier = Modifier
                    .weight(1f)
                    .height(Size.buttonHeight)
                    .clip(RoundedCornerShape(CornerRadius.md))
                    .background(
                        if (signatureState.canUndo)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(IconMapper.Actions.undo),
                        contentDescription = Strings.actionUndo(),
                        tint = if (signatureState.canUndo)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(Size.iconMD)
                    )
                    Text(
                        text = Strings.actionUndo(),
                        color = if (signatureState.canUndo)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Grid Settings
        GradientCard(
            gradient = Gradients.Sample.gridSubtle,
            elevation = Elevation.sm
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                SmallSectionHeader(
                    title = Strings.gridSettings(),
                    icon = IconMapper.getScreenIcon(SampleScreen.WITH_GRID)
                )

                // Show Grid Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Strings.showGrid(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Switch(
                        checked = showGrid,
                        onCheckedChange = { showGrid = it }
                    )
                }

                if (showGrid) {
                    Spacer(modifier = Modifier.height(Spacing.xs))

                    // Grid Color
                    SmallSectionHeader(title = Strings.gridColor())

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        GridColorOption.entries.forEach { option ->
                            FilterChip(
                                selected = selectedGridColor == option,
                                onClick = { selectedGridColor = option },
                                label = {
                                    Text(
                                        option.displayName,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.xs))

                    // Grid Spacing
                    Text(
                        text = Strings.gridSpacingValue(gridSpacing.toInt()),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Slider(
                        value = gridSpacing,
                        onValueChange = { gridSpacing = it },
                        valueRange = 10f..50f,
                        steps = 39
                    )
                }
            }
        }

        // Preview
        SignaturePreviewCard(
            title = Strings.preview(),
            bitmap = capturedSignature
        )
    }
}
