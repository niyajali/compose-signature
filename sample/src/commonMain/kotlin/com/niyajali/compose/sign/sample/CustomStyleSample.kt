package com.niyajali.compose.sign.sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.SignatureConfig
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.rememberSignatureState

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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Custom Styling",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Customize the appearance of your signature pad.",
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Stroke Color",
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StrokeColorOption.entries.forEach { option ->
                        FilterChip(
                            selected = selectedStrokeColor == option,
                            onClick = { selectedStrokeColor = option },
                            label = { Text(option.displayName, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Text(
                    text = "Stroke Width: ${strokeWidth.toInt()} dp",
                    style = MaterialTheme.typography.titleSmall
                )

                Slider(
                    value = strokeWidth,
                    onValueChange = { strokeWidth = it },
                    valueRange = 1f..10f,
                    steps = 8
                )

                Text(
                    text = "Background Color",
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BackgroundOption.entries.forEach { option ->
                        FilterChip(
                            selected = selectedBackground == option,
                            onClick = { selectedBackground = option },
                            label = { Text(option.displayName, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Text(
                    text = "Corner Radius: ${cornerRadius.toInt()} dp",
                    style = MaterialTheme.typography.titleSmall
                )

                Slider(
                    value = cornerRadius,
                    onValueChange = { cornerRadius = it },
                    valueRange = 0f..24f,
                    steps = 23
                )
            }
        }

        SignaturePreviewCard(
            title = "Preview",
            bitmap = capturedSignature
        )
    }
}
