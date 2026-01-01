package com.niyajali.compose.sign.sample

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.SignatureConfig
import com.niyajali.compose.sign.asDarkTheme
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.rememberSignatureState

@Composable
fun DarkThemeSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    val darkConfig = SignatureConfig(
        showGrid = true
    ).asDarkTheme()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Dark Theme",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Text(
            text = "Signature pad styled for dark theme using the asDarkTheme() extension.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            ComposeSign(
                onSignatureUpdate = { bitmap ->
                    capturedSignature = bitmap
                },
                config = darkConfig,
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
                Text("Clear", color = Color.White)
            }

            OutlinedButton(
                onClick = { signatureState.undo() },
                enabled = signatureState.canUndo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Undo", color = Color.White)
            }

            OutlinedButton(
                onClick = { signatureState.redo() },
                enabled = signatureState.canRedo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Redo", color = Color.White)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2D2D2D)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Dark Theme Configuration",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                DarkThemeInfoRow("Stroke Color", "White")
                DarkThemeInfoRow("Background Color", "Black")
                DarkThemeInfoRow("Grid Color", "White (30% opacity)")
                DarkThemeInfoRow("Border Color", "White")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Usage",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Text(
                    text = "val darkConfig = SignatureConfig().asDarkTheme()",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF81C784),
                    modifier = Modifier
                        .background(Color(0xFF263238), MaterialTheme.shapes.small)
                        .padding(8.dp)
                )

                Text(
                    text = "The asDarkTheme() extension automatically applies dark-friendly colors to your signature configuration.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        capturedSignature?.let { bitmap ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D2D)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Signature Info",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )

                    DarkThemeInfoRow("Paths", signatureState.paths.size.toString())
                    DarkThemeInfoRow("Bitmap Size", "${bitmap.width} x ${bitmap.height}")
                    DarkThemeInfoRow("Can Undo", signatureState.canUndo.toString())
                    DarkThemeInfoRow("Can Redo", signatureState.canRedo.toString())
                }
            }
        }
    }
}

@Composable
fun DarkThemeInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}
