package com.niyajali.compose.sign.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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

@Composable
fun ValidationSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    val metadata by remember(signatureState.paths) {
        derivedStateOf { signatureState.getMetadata() }
    }

    val isValidSignature by remember(signatureState.paths) {
        derivedStateOf { signatureState.isValid(minPaths = 10, minLength = 200f, minComplexity = 15) }
    }

    val bounds by remember(signatureState.paths) {
        derivedStateOf { signatureState.getSignatureBounds() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Signature Validation",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Validate signatures based on complexity, length, and path count requirements.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            ComposeSign(
                onSignatureUpdate = { bitmap ->
                    capturedSignature = bitmap
                },
                state = signatureState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { signatureState.clear() },
                enabled = !signatureState.isEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }

            OutlinedButton(
                onClick = { signatureState.undo() },
                enabled = signatureState.canUndo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Undo")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isValidSignature)
                    Color(0xFFE8F5E9)
                else
                    Color(0xFFFFEBEE)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Validation Status",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (isValidSignature) "✓ Valid" else "✗ Invalid",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isValidSignature) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }

                Text(
                    text = "Requirements: min 10 paths, min 200px length, min 15 complexity",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Signature Metrics",
                    style = MaterialTheme.typography.titleMedium
                )

                MetricRow(
                    label = "Path Count",
                    value = metadata.pathCount.toString(),
                    requirement = "≥ 10",
                    isMet = metadata.pathCount >= 10
                )

                MetricRow(
                    label = "Total Length",
                    value = "${signatureState.getTotalLength().toInt()} px",
                    requirement = "≥ 200 px",
                    isMet = signatureState.getTotalLength() >= 200f
                )

                MetricRow(
                    label = "Complexity Score",
                    value = "${signatureState.getComplexityScore()}/100",
                    requirement = "≥ 15",
                    isMet = signatureState.getComplexityScore() >= 15
                )

                Text(
                    text = "Complexity Progress",
                    style = MaterialTheme.typography.titleSmall
                )

                LinearProgressIndicator(
                    progress = { (signatureState.getComplexityScore() / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                    color = when {
                        signatureState.getComplexityScore() < 20 -> Color(0xFFF44336)
                        signatureState.getComplexityScore() < 40 -> Color(0xFFFF9800)
                        signatureState.getComplexityScore() < 60 -> Color(0xFFFFEB3B)
                        else -> Color(0xFF4CAF50)
                    }
                )

                Text(
                    text = metadata.complexityDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        bounds?.let { b ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Signature Bounds",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )

                    InfoRow("Width", "${b.width.toInt()} px")
                    InfoRow("Height", "${b.height.toInt()} px")
                    InfoRow("Area", "${b.area().toInt()} sq px")
                    InfoRow("Center", "(${b.center.x.toInt()}, ${b.center.y.toInt()})")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = signatureState.getDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun MetricRow(
    label: String,
    value: String,
    requirement: String,
    isMet: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Requirement: $requirement",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (isMet) "✓" else "✗",
                color = if (isMet) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}
