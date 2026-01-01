package com.niyajali.compose.sign.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.SignatureAction
import com.niyajali.compose.sign.SignatureConfig
import com.niyajali.compose.sign.rememberSignatureState

@Composable
fun BuiltInActionsSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }
    var lastAction by remember { mutableStateOf<SignatureAction?>(null) }

    val config = SignatureConfig(
        showActions = true,
        showGrid = true
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Built-in Actions",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "The signature pad includes built-in action buttons for Clear, Undo, Redo, and Save operations.",
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
                onActionClicked = { action ->
                    lastAction = action
                    when (action) {
                        SignatureAction.CLEAR -> signatureState.clear()
                        SignatureAction.UNDO -> signatureState.undo()
                        SignatureAction.REDO -> signatureState.redo()
                        SignatureAction.SAVE -> {
                            // Handle save action
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        lastAction?.let { action ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Last Action: ${action.getDisplayName()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = action.getDescription(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Available Actions",
                    style = MaterialTheme.typography.titleMedium
                )

                SignatureAction.entries.forEach { action ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = action.getDisplayName(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = action.getDescription(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (action.isDestructive()) {
                            Text(
                                text = "⚠️ Destructive action",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        SignaturePreviewCard(
            title = "Preview",
            bitmap = capturedSignature
        )
    }
}
