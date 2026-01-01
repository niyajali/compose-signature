package com.niyajali.compose.sign.sample

import androidx.compose.foundation.BorderStroke
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

enum class StrokeColorOption(val color: Color, val displayName: String) {
    BLACK(Color.Black, "Black"),
    BLUE(Color(0xFF1976D2), "Blue"),
    RED(Color(0xFFD32F2F), "Red"),
    GREEN(Color(0xFF388E3C), "Green"),
    PURPLE(Color(0xFF7B1FA2), "Purple")
}

enum class BackgroundOption(val color: Color, val displayName: String) {
    WHITE(Color.White, "White"),
    CREAM(Color(0xFFFFFDE7), "Cream"),
    LIGHT_GRAY(Color(0xFFF5F5F5), "Light Gray"),
    LIGHT_BLUE(Color(0xFFE3F2FD), "Light Blue")
}

@Composable
fun CustomStyleSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    var strokeWidth by remember { mutableFloatStateOf(3f) }
    var selectedStrokeColor by remember { mutableStateOf(StrokeColorOption.BLACK) }
    var selectedBackground by remember { mutableStateOf(BackgroundOption.WHITE) }
    var cornerRadius by remember { mutableFloatStateOf(8f) }

    val config = SignatureConfig(
        strokeColor = selectedStrokeColor.color,
        strokeWidth = strokeWidth.dp,
        backgroundColor = selectedBackground.color,
        borderStroke = BorderStroke(2.dp, selectedStrokeColor.color.copy(alpha = 0.5f)),
        cornerShape = RoundedCornerShape(cornerRadius.dp)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Gradients.Sample.customStyleSubtle)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Header with icon
        SectionHeader(
            title = Strings.styleTitle(),
            icon = IconMapper.getScreenIcon(SampleScreen.CUSTOM_STYLE)
        )

        // Description
        Text(
            text = Strings.styleDescription(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Signature Pad
        GradientCard(
            gradient = Gradients.Sample.customStyleSubtle,
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

        // Customization Options
        GradientCard(
            gradient = Gradients.Sample.customStyleSubtle,
            elevation = Elevation.sm
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                // Stroke Color
                SmallSectionHeader(title = Strings.strokeColor())

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StrokeColorOption.entries.forEach { option ->
                        FilterChip(
                            selected = selectedStrokeColor == option,
                            onClick = { selectedStrokeColor = option },
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

                // Stroke Width
                Text(
                    text = Strings.strokeWidthValue(strokeWidth.toInt()),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Slider(
                    value = strokeWidth,
                    onValueChange = { strokeWidth = it },
                    valueRange = 1f..10f,
                    steps = 8
                )

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Background Color
                SmallSectionHeader(title = Strings.backgroundColor())

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BackgroundOption.entries.forEach { option ->
                        FilterChip(
                            selected = selectedBackground == option,
                            onClick = { selectedBackground = option },
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

                // Corner Radius
                Text(
                    text = Strings.cornerRadiusValue(cornerRadius.toInt()),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Slider(
                    value = cornerRadius,
                    onValueChange = { cornerRadius = it },
                    valueRange = 0f..24f,
                    steps = 23
                )
            }
        }

        // Preview
        SignaturePreviewCard(
            title = Strings.preview(),
            bitmap = capturedSignature
        )
    }
}
