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
    FULLSCREEN("Fullscreen Mode")
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
                            onClick = { currentScreen = SampleScreen.HOME }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = currentScreen,
            modifier = Modifier.padding(paddingValues),
            transitionSpec = { slideInFromRight() }
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
