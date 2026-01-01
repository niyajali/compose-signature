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
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.isEmpty
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
fun BasicSignatureSample(modifier: Modifier = Modifier) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Gradients.Sample.basicSubtle)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        // Header with icon
        SectionHeader(
            title = Strings.basicTitle(),
            icon = IconMapper.getScreenIcon(SampleScreen.BASIC),
        )

        // Description
        Text(
            text = Strings.basicDescription(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // Signature Pad Card
        GradientCard(
            gradient = Gradients.Sample.basicSubtle,
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

        // Action Buttons Row
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

            // Redo Button
            IconButton(
                onClick = { signatureState.redo() },
                enabled = signatureState.canRedo,
                modifier = Modifier
                    .weight(1f)
                    .height(Size.buttonHeight)
                    .clip(RoundedCornerShape(CornerRadius.md))
                    .background(
                        if (signatureState.canRedo) {
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
                        painter = painterResource(IconMapper.Actions.redo),
                        contentDescription = Strings.actionRedo(),
                        tint = if (signatureState.canRedo) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(Size.iconMD),
                    )
                    Text(
                        text = Strings.actionRedo(),
                        color = if (signatureState.canRedo) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
            }
        }

        // Preview Section
        SignaturePreviewCard(
            title = Strings.capturedSignature(),
            bitmap = capturedSignature,
        )

        // Info Section
        SignatureInfoCard(
            pathCount = signatureState.paths.size,
            isEmpty = signatureState.isEmpty(),
            canUndo = signatureState.canUndo,
            canRedo = signatureState.canRedo,
        )
    }
}

@Composable
fun SignaturePreviewCard(
    title: String,
    bitmap: ImageBitmap?,
    modifier: Modifier = Modifier,
) {
    GradientCard(
        modifier = modifier,
        gradient = Gradients.cardPrimary,
        elevation = Elevation.sm,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (bitmap != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(CornerRadius.md))
                        .background(Color.White)
                        .border(
                            width = 2.dp,
                            brush = Gradients.Sample.basic,
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
                    text = Strings.signatureSize(bitmap.width, bitmap.height),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(CornerRadius.md))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(CornerRadius.md),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = Strings.noSignatureYet(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun SignatureInfoCard(
    pathCount: Int,
    isEmpty: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    modifier: Modifier = Modifier,
) {
    GradientCard(
        modifier = modifier,
        gradient = Gradients.Sample.basicSubtle,
        elevation = Elevation.sm,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = Strings.signatureState(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(Spacing.xxs))

            InfoRow(Strings.pathCount(), pathCount.toString())
            InfoRow(Strings.isEmpty(), isEmpty.toString())
            InfoRow(Strings.canUndo(), canUndo.toString())
            InfoRow(Strings.canRedo(), canRedo.toString())
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
