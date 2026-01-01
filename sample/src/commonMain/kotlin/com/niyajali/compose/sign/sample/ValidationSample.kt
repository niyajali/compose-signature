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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.getComplexityScore
import com.niyajali.compose.sign.getDescription
import com.niyajali.compose.sign.getMetadata
import com.niyajali.compose.sign.getSignatureBounds
import com.niyajali.compose.sign.getTotalLength
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.isValid
import com.niyajali.compose.sign.rememberSignatureState
import com.niyajali.compose.sign.sample.components.cards.GradientCard
import com.niyajali.compose.sign.sample.components.icons.IconMapper
import com.niyajali.compose.sign.sample.components.layout.SectionHeader
import com.niyajali.compose.sign.sample.theme.CornerRadius
import com.niyajali.compose.sign.sample.theme.Elevation
import com.niyajali.compose.sign.sample.theme.Gradients
import com.niyajali.compose.sign.sample.theme.Size
import com.niyajali.compose.sign.sample.theme.Spacing
import com.niyajali.compose.sign.sample.utils.Strings
import org.jetbrains.compose.resources.painterResource

@Composable
fun ValidationSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    val minPaths = 10
    val minLength = 200f
    val minComplexity = 15

    val metadata by remember(signatureState.paths) {
        derivedStateOf { signatureState.getMetadata() }
    }

    val isValidSignature by remember(signatureState.paths) {
        derivedStateOf {
            signatureState.isValid(
                minPaths = minPaths,
                minLength = minLength,
                minComplexity = minComplexity
            )
        }
    }

    val bounds by remember(signatureState.paths) {
        derivedStateOf { signatureState.getSignatureBounds() }
    }

    val totalLength = signatureState.getTotalLength()
    val complexityScore = signatureState.getComplexityScore()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Gradients.Sample.validationSubtle)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Header with icon
        SectionHeader(
            title = Strings.validationTitle(),
            icon = IconMapper.getScreenIcon(SampleScreen.VALIDATION)
        )

        // Description
        Text(
            text = Strings.validationDescription(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Signature Pad
        GradientCard(
            gradient = Gradients.Sample.validationSubtle,
            elevation = Elevation.md
        ) {
            ComposeSign(
                onSignatureUpdate = { bitmap ->
                    capturedSignature = bitmap
                },
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

        // Validation Status
        GradientCard(
            gradient = if (isValidSignature) Gradients.success else Gradients.error,
            elevation = Elevation.md
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        if (isValidSignature) IconMapper.Icons.check else IconMapper.Icons.block
                    ),
                    contentDescription = null,
                    tint = if (isValidSignature)
                        MaterialTheme.colorScheme.onTertiaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(Size.iconLG)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Strings.validationStatus(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isValidSignature)
                            MaterialTheme.colorScheme.onTertiaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = if (isValidSignature) Strings.statusValid() else Strings.statusInvalid(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isValidSignature)
                            MaterialTheme.colorScheme.onTertiaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Requirements
        GradientCard(
            gradient = Gradients.Sample.validationSubtle,
            elevation = Elevation.sm
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                Text(
                    text = Strings.validationRequirements(minPaths, minLength.toInt(), minComplexity),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Signature Metrics
        GradientCard(
            gradient = Gradients.Sample.validationSubtle,
            elevation = Elevation.sm
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(IconMapper.getScreenIcon(SampleScreen.VALIDATION)),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Size.iconMD)
                    )
                    Text(
                        text = Strings.signatureMetrics(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Path Count
                MetricRow(
                    label = Strings.pathCount(),
                    value = metadata.pathCount.toString(),
                    requirement = minPaths,
                    currentValue = metadata.pathCount,
                    isValid = metadata.pathCount >= minPaths
                )

                // Total Length
                MetricRow(
                    label = Strings.totalLength(),
                    value = Strings.pixels(totalLength.toInt()),
                    requirement = minLength.toInt(),
                    currentValue = totalLength.toInt(),
                    isValid = totalLength >= minLength
                )

                // Complexity Score
                MetricRow(
                    label = Strings.complexityScore(),
                    value = complexityScore.toString(),
                    requirement = minComplexity,
                    currentValue = complexityScore,
                    isValid = complexityScore >= minComplexity
                )

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Complexity Progress
                Text(
                    text = Strings.complexityProgress(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                val progress = (complexityScore.toFloat() / minComplexity.toFloat()).coerceIn(0f, 1f)

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(CornerRadius.sm)),
                    color = when {
                        progress >= 1f -> MaterialTheme.colorScheme.tertiary
                        progress >= 0.7f -> MaterialTheme.colorScheme.primary
                        progress >= 0.4f -> Color(0xFFF59E0B)
                        else -> MaterialTheme.colorScheme.error
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Text(
                    text = signatureState.getDescription(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Signature Bounds
        if (bounds != null) {
            GradientCard(
                gradient = Gradients.cardPrimary,
                elevation = Elevation.sm
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    Text(
                        text = Strings.signatureBounds(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(Spacing.xxs))

                    bounds?.let { b ->
                        InfoRow(Strings.width(), Strings.pixels(b.width.toInt()))
                        InfoRow(Strings.height(), Strings.pixels(b.height.toInt()))
                        InfoRow(Strings.area(), Strings.squarePixels((b.width * b.height).toInt()))
                        InfoRow(Strings.center(), Strings.coordinates(b.center.x.toInt(), b.center.y.toInt()))
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricRow(
    label: String,
    value: String,
    requirement: Int,
    currentValue: Int,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    if (isValid) IconMapper.Icons.check else IconMapper.Icons.block
                ),
                contentDescription = null,
                tint = if (isValid)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = Strings.requirement("≥ $requirement"),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = if (isValid)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.error
        )
    }
}
