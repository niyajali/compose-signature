package com.niyajali.compose.sign.sample

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

@Composable
fun FullscreenSample(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val signatureState = rememberSignatureState()
    var capturedSignature by remember { mutableStateOf<ImageBitmap?>(null) }

    val config = SignatureConfig(
        isFullScreen = true,
        showActions = true,
        showGrid = true
    )

    Box(modifier = modifier.fillMaxSize()) {
        ComposeSignFullscreen(
            onSignatureUpdate = { bitmap ->
                capturedSignature = bitmap
            },
            onDismiss = onBack,
            config = config,
            state = signatureState
        )
    }
}
