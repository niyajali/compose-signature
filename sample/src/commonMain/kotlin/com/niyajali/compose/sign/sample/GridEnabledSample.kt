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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.SignatureConfig
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.rememberSignatureState

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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Grid Enabled Signature",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Enable a grid overlay to help guide your signature.",
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
                config = config,
                state = signatureState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
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

            OutlinedButton(
                onClick = { signatureState.redo() },
                enabled = signatureState.canRedo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Redo")
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Grid Settings",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Show Grid",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = showGrid,
                        onCheckedChange = { showGrid = it }
                    )
                }

                if (showGrid) {
                    Text(
                        text = "Grid Color",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        GridColorOption.entries.forEach { option ->
                            FilterChip(
                                selected = selectedGridColor == option,
                                onClick = { selectedGridColor = option },
                                label = { Text(option.displayName) }
                            )
                        }
                    }

                    Text(
                        text = "Grid Spacing: ${gridSpacing.toInt()} dp",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Slider(
                        value = gridSpacing,
                        onValueChange = { gridSpacing = it },
                        valueRange = 10f..50f,
                        steps = 7
                    )
                }
            }
        }

        SignaturePreviewCard(
            title = "Preview",
            bitmap = capturedSignature
        )
    }
}
