/**
 * Copyright 2026 Sk Niyaj Ali
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.niyajali.compose.sign.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Switch
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
import com.niyajali.compose.sign.isEmpty
import com.niyajali.compose.sign.rememberSignatureState
import com.niyajali.compose.sign.sample.components.buttons.GradientButton
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

enum class ExportSize(val width: Int, val height: Int, val displayName: String) {
    SMALL(200, 100, "Small (200×100)"),
    MEDIUM(400, 200, "Medium (400×200)"),
    LARGE(800, 400, "Large (800×400)"),
    SQUARE(300, 300, "Square (300×300)"),
}

enum class ExportBackground(val color: Color, val displayName: String) {
    WHITE(Color.White, "White"),
    TRANSPARENT(Color.Transparent, "Transparent"),
    LIGHT_GRAY(Color(0xFFF5F5F5), "Light Gray"),
    CREAM(Color(0xFFFFFDE7), "Cream"),
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
            .background(Gradients.Sample.exportSubtle)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        // Header with icon
        SectionHeader(
            title = Strings.exportTitle(),
            icon = IconMapper.getScreenIcon(SampleScreen.EXPORT),
        )

        // Description
        Text(
            text = Strings.exportDescription(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // Signature Pad
        GradientCard(
            gradient = Gradients.Sample.exportSubtle,
            elevation = Elevation.md,
        ) {
            ComposeSign(
                onSignatureUpdate = { bitmap ->
                    capturedSignature = bitmap
                },
                state = signatureState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size.signaturePadHeight),
            )
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
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
                        if (!signatureState.isEmpty()) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                    ),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(IconMapper.Actions.clear),
                        contentDescription = Strings.actionClear(),
                        tint = if (!signatureState.isEmpty()) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(Size.iconMD),
                    )
                    Text(
                        text = Strings.actionClear(),
                        color = if (!signatureState.isEmpty()) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
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
                        if (signatureState.canUndo) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                    ),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(IconMapper.Actions.undo),
                        contentDescription = Strings.actionUndo(),
                        tint = if (signatureState.canUndo) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(Size.iconMD),
                    )
                    Text(
                        text = Strings.actionUndo(),
                        color = if (signatureState.canUndo) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
            }
        }

        // Export Settings
        GradientCard(
            gradient = Gradients.Sample.exportSubtle,
            elevation = Elevation.sm,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                SmallSectionHeader(title = Strings.exportSettings())

                // Export Size
                Text(
                    text = Strings.exportSize(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ExportSize.entries.forEach { size ->
                        FilterChip(
                            selected = selectedSize == size,
                            onClick = { selectedSize = size },
                            label = {
                                Text(
                                    size.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Background Color
                Text(
                    text = Strings.backgroundColor(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ExportBackground.entries.forEach { bg ->
                        FilterChip(
                            selected = selectedBackground == bg,
                            onClick = { selectedBackground = bg },
                            label = {
                                Text(
                                    bg.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Maintain Aspect Ratio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = Strings.maintainAspectRatio(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Switch(
                        checked = maintainAspectRatio,
                        onCheckedChange = { maintainAspectRatio = it },
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Export Button
                GradientButton(
                    text = Strings.actionExport(),
                    onClick = {
                        exportedSignature = signatureState.exportScaledSignature(
                            targetWidth = selectedSize.width,
                            targetHeight = selectedSize.height,
                            backgroundColor = selectedBackground.color,
                            maintainAspectRatio = maintainAspectRatio,
                        )
                    },
                    gradient = Gradients.Sample.export,
                    enabled = !signatureState.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // Exported Signature Preview
        exportedSignature?.let { bitmap ->
            GradientCard(
                gradient = Gradients.cardTertiary,
                elevation = Elevation.sm,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(IconMapper.Actions.save),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Size.iconMD),
                        )
                        Text(
                            text = Strings.exportedSignature(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(CornerRadius.md))
                            .background(selectedBackground.color)
                            .border(
                                width = 2.dp,
                                brush = Gradients.Sample.export,
                                shape = RoundedCornerShape(CornerRadius.md),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = Strings.cdSignaturePreview(),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    Text(
                        text = Strings.dimensions(bitmap.width, bitmap.height),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
