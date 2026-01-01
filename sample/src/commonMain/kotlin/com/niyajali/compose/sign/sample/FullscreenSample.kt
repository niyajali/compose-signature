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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.niyajali.compose.sign.ComposeSignFullscreen
import com.niyajali.compose.sign.SignatureConfig
import com.niyajali.compose.sign.rememberSignatureState
import com.niyajali.compose.sign.sample.theme.Gradients

@Composable
fun FullscreenSample(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    val config = SignatureConfig(
        isFullScreen = true,
        showActions = true,
        showGrid = true,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gradients.Sample.fullscreenSubtle),
    ) {
        ComposeSignFullscreen(
            onSignatureUpdate = { bitmap ->
                capturedSignature = bitmap
            },
            onDismiss = onBack,
            config = config,
            state = signatureState,
        )
    }
}
