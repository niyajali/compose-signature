package com.niyajali.compose.sign.sample

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
                        IconButton(onClick = { currentScreen = SampleScreen.HOME }) {
                            Text("←", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = currentScreen,
            modifier = Modifier.padding(paddingValues)
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

@Composable
fun HomeScreen(
    onNavigate: (SampleScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Welcome to Compose Signature Library",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explore different features and configurations:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        SampleScreen.entries
            .filter { it != SampleScreen.HOME }
            .forEach { screen ->
                SampleButton(
                    title = screen.title,
                    description = getScreenDescription(screen),
                    onClick = { onNavigate(screen) }
                )
            }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Library Features:",
            style = MaterialTheme.typography.titleMedium
        )

        FeatureList()
    }
}

@Composable
fun SampleButton(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FeatureList() {
    val features = listOf(
        "Cross-platform support (Android, iOS, Desktop, Web)",
        "Customizable stroke color and width",
        "Background customization",
        "Grid overlay support",
        "Undo/Redo functionality",
        "Export to ImageBitmap",
        "Signature validation",
        "Dark theme support",
        "Fullscreen mode",
        "State persistence across configuration changes"
    )

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        features.forEach { feature ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("•", color = MaterialTheme.colorScheme.primary)
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

fun getScreenDescription(screen: SampleScreen): String = when (screen) {
    SampleScreen.HOME -> ""
    SampleScreen.BASIC -> "Simple signature pad with default settings"
    SampleScreen.CUSTOM_STYLE -> "Custom colors, stroke width, and shapes"
    SampleScreen.WITH_GRID -> "Signature pad with grid overlay"
    SampleScreen.WITH_ACTIONS -> "Built-in Clear, Undo, Redo, Save buttons"
    SampleScreen.EXPORT -> "Export signature as bitmap with different sizes"
    SampleScreen.VALIDATION -> "Validate signature complexity and requirements"
    SampleScreen.DARK_THEME -> "Dark theme styling example"
    SampleScreen.FULLSCREEN -> "Full screen signature mode"
}
