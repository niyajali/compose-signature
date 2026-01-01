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

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.niyajali.compose.sign.sample.components.animations.PageTransitions.slideInFromRight
import com.niyajali.compose.sign.sample.components.navigation.BackButton
import com.niyajali.compose.sign.sample.screens.home.HomeScreen

enum class SampleScreen(val title: String) {
    HOME("Compose Signature Samples"),
    BASIC("Basic Signature"),
    CUSTOM_STYLE("Custom Styling"),
    WITH_GRID("Grid Enabled"),
    WITH_ACTIONS("Built-in Actions"),
    EXPORT("Export & Save"),
    VALIDATION("Validation"),
    DARK_THEME("Dark Theme"),
    FULLSCREEN("Fullscreen Mode"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignatureSampleApp() {
    var currentScreen by remember { mutableStateOf(SampleScreen.HOME) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreen.title) },
                navigationIcon = {
                    if (currentScreen != SampleScreen.HOME) {
                        BackButton(
                            onClick = { currentScreen = SampleScreen.HOME },
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        AnimatedContent(
            targetState = currentScreen,
            modifier = Modifier.padding(paddingValues),
            transitionSpec = { slideInFromRight() },
        ) { screen ->
            when (screen) {
                SampleScreen.HOME -> HomeScreen(onNavigate = { currentScreen = it })
                SampleScreen.BASIC -> BasicSignatureSample()
                SampleScreen.CUSTOM_STYLE -> CustomStyleSample()
                SampleScreen.WITH_GRID -> GridEnabledSample()
                SampleScreen.WITH_ACTIONS -> BuiltInActionsSample()
                SampleScreen.EXPORT -> ExportSample()
                SampleScreen.VALIDATION -> ValidationSample()
                SampleScreen.DARK_THEME -> DarkThemeSample()
                SampleScreen.FULLSCREEN -> FullscreenSample(onBack = { currentScreen = SampleScreen.HOME })
            }
        }
    }
}

// Old home screen and helper functions removed - now using redesigned HomeScreen from screens.home package
