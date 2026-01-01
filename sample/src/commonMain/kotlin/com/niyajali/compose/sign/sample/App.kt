package com.niyajali.compose.sign.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.niyajali.compose.sign.sample.theme.ComposeSignTheme

@Composable
fun App(
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    ComposeSignTheme(
       darkTheme = darkTheme,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SignatureSampleApp()
        }
    }
}
