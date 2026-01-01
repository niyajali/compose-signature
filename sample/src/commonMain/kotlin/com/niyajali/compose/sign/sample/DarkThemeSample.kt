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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.SignatureConfig
import com.niyajali.compose.sign.asDarkTheme
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.rememberSignatureState
import com.niyajali.compose.sign.sample.components.cards.GradientCard
import com.niyajali.compose.sign.sample.components.icons.IconMapper
import com.niyajali.compose.sign.sample.theme.CornerRadius
import com.niyajali.compose.sign.sample.theme.Elevation
import com.niyajali.compose.sign.sample.theme.Gradients
import com.niyajali.compose.sign.sample.theme.Size
import com.niyajali.compose.sign.sample.theme.Spacing
import com.niyajali.compose.sign.sample.utils.Strings
import org.jetbrains.compose.resources.painterResource

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
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Header with icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.sm),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(IconMapper.getScreenIcon(SampleScreen.DARK_THEME)),
                contentDescription = Strings.darkTitle(),
                tint = Color(0xFFA855F7),
                modifier = Modifier.size(Size.iconLG)
            )
            Spacer(modifier = Modifier.width(Spacing.sm))
            Text(
                text = Strings.darkTitle(),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }

        // Description
        Text(
            text = Strings.darkDescription(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )

        // Signature Pad
        GradientCard(
            gradient = Gradients.Sample.darkThemeSubtle,
            backgroundColor = Color(0xFF1E1E1E),
            elevation = Elevation.lg
        ) {
            ComposeSign(
                onSignatureUpdate = { bitmap ->
                    capturedSignature = bitmap
                },
                config = darkConfig,
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
                            Color(0xFF7F1D1D)
                        else
                            Color(0xFF2D2D2D)
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
                            Color(0xFFFEE2E2)
                        else
                            Color(0xFF9CA3AF),
                        modifier = Modifier.size(Size.iconMD)
                    )
                    Text(
                        text = Strings.actionClear(),
                        color = if (!signatureState.isEmpty())
                            Color(0xFFFEE2E2)
                        else
                            Color(0xFF9CA3AF)
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
                            Color(0xFF312E81)
                        else
                            Color(0xFF2D2D2D)
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
                            Color(0xFFEEF2FF)
                        else
                            Color(0xFF9CA3AF),
                        modifier = Modifier.size(Size.iconMD)
                    )
                    Text(
                        text = Strings.actionUndo(),
                        color = if (signatureState.canUndo)
                            Color(0xFFEEF2FF)
                        else
                            Color(0xFF9CA3AF)
                    )
                }
            }
        }

        // Dark Theme Configuration Info
        GradientCard(
            backgroundColor = Color(0xFF1E1E1E),
            elevation = Elevation.md
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(IconMapper.getScreenIcon(SampleScreen.DARK_THEME)),
                        contentDescription = null,
                        tint = Color(0xFFA855F7),
                        modifier = Modifier.size(Size.iconMD)
                    )
                    Text(
                        text = Strings.darkConfig(),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                DarkThemeInfoRow(Strings.usage(), Strings.usageCode())

                Text(
                    text = Strings.usageDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Signature Info
        GradientCard(
            backgroundColor = Color(0xFF2D2D2D),
            elevation = Elevation.sm
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    text = Strings.signatureInfo(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(Spacing.xxs))

                DarkThemeInfoRow(Strings.paths(), signatureState.paths.size.toString())
                DarkThemeInfoRow(Strings.canUndo(), signatureState.canUndo.toString())
                DarkThemeInfoRow(Strings.canRedo(), signatureState.canRedo.toString())

                capturedSignature?.let { bitmap ->
                    DarkThemeInfoRow(
                        Strings.bitmapSize(),
                        "${bitmap.width} × ${bitmap.height} px"
                    )
                }
            }
        }

        // Config Details
        GradientCard(
            backgroundColor = Color(0xFF263238),
            elevation = Elevation.sm
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                Text(
                    text = "Configuration",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF81C784)
                )

                Text(
                    text = "Stroke Color: ${Strings.white30Opacity()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Text(
                    text = "Background: Dark (#1E1E1E)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Text(
                    text = "Grid Color: White (10% opacity)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun DarkThemeInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}
