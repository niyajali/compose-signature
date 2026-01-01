package com.niyajali.compose.sign.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.niyajali.compose.sign.exportScaledSignature
import com.niyajali.compose.sign.exportSignature
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.rememberSignatureState

enum class ExportSize(val width: Int, val height: Int, val displayName: String) {
    SMALL(200, 100, "Small (200x100)"),
    MEDIUM(400, 200, "Medium (400x200)"),
    LARGE(800, 400, "Large (800x400)"),
    SQUARE(300, 300, "Square (300x300)")
}

enum class ExportBackground(val color: Color, val displayName: String) {
    WHITE(Color.White, "White"),
    TRANSPARENT(Color.Transparent, "Transparent"),
    LIGHT_GRAY(Color(0xFFF5F5F5), "Light Gray"),
    CREAM(Color(0xFFFFFDE7), "Cream")
}

@Composable
fun ExportSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }
    var exportedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    var selectedSize by remember { mutableStateOf(ExportSize.MEDIUM) }
    var selectedBackground by remember { mutableStateOf(ExportBackground.WHITE) }
    var maintainAspectRatio by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Export Signature",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Export your signature with custom dimensions and background colors.",
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
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Export Settings",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Export Size",
                    style = MaterialTheme.typography.titleSmall
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ExportSize.entries.take(2).forEach { size ->
                            FilterChip(
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                label = { Text(size.displayName, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ExportSize.entries.drop(2).forEach { size ->
                            FilterChip(
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                label = { Text(size.displayName, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }

                Text(
                    text = "Background Color",
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExportBackground.entries.take(2).forEach { bg ->
                        FilterChip(
                            selected = selectedBackground == bg,
                            onClick = { selectedBackground = bg },
                            label = { Text(bg.displayName, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExportBackground.entries.drop(2).forEach { bg ->
                        FilterChip(
                            selected = selectedBackground == bg,
                            onClick = { selectedBackground = bg },
                            label = { Text(bg.displayName, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = maintainAspectRatio,
                        onClick = { maintainAspectRatio = !maintainAspectRatio },
                        label = { Text("Maintain Aspect Ratio") }
                    )
                }

                Button(
                    onClick = {
                        exportedSignature = signatureState.exportScaledSignature(
                            targetWidth = selectedSize.width,
                            targetHeight = selectedSize.height,
                            maintainAspectRatio = maintainAspectRatio,
                            backgroundColor = selectedBackground.color
                        )
                    },
                    enabled = !signatureState.isEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Export Signature")
                }
            }
        }

        exportedSignature?.let { bitmap ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Exported Signature",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (selectedBackground == ExportBackground.TRANSPARENT)
                                    Color.LightGray
                                else
                                    selectedBackground.color
                            )
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Exported Signature",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = "Dimensions: ${bitmap.width} x ${bitmap.height} px",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
