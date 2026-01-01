# Compose Signature

[![Maven Central](https://img.shields.io/maven-central/v/io.github.niyajali/compose-signature.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.niyajali%22%20AND%20a:%22compose-signature%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform-orange.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-Multiplatform-blue.svg?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-mpp/)

A customizable Compose Multiplatform library for capturing digital signatures. The library provides
comprehensive state management, undo/redo functionality, and support for Android, iOS, Desktop (
JVM), and Web (JS/WASM) platforms.

## Features

- **Customizable Appearance**: Configure colors, stroke width, backgrounds, borders, and corner
  shapes
- **Multiplatform Support**: Works across Android, iOS, Desktop, and Web platforms
- **Undo/Redo**: Full history management with configurable stack limits
- **Grid Display**: Optional grid overlay with customizable spacing and colors
- **Fullscreen Mode**: Dedicated fullscreen signature capture interface
- **Real-time Updates**: Live signature updates as the user draws
- **State Management**: Comprehensive state tracking with proper lifecycle handling
- **Multiple API Levels**: Simple to advanced usage patterns through different overloads
- **Built-in Actions**: Optional action buttons for common operations (Clear, Save, Undo, Redo)
- **Signature Analytics**: Complexity analysis, bounds calculation, and metadata generation
- **Export Options**: Multiple export formats with configurable scaling
- **Accessibility**: High contrast modes and accessibility-friendly configurations

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.niyajali:compose-signature:1.0.2")
}
```

## Quick Start

### Basic Usage

```kotlin
@Composable
fun SimpleSignature() {
    var signature by remember { mutableStateOf<ImageBitmap?>(null) }

    ComposeSign(
        onSignatureUpdate = { signature = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
```

### Advanced Usage

```kotlin
@Composable
fun AdvancedSignature() {
    val signatureState = rememberSignatureState()
    var signature by remember { mutableStateOf<ImageBitmap?>(null) }

    ComposeSign(
        onSignatureUpdate = { signature = it },
        config = SignatureConfig(
            strokeColor = Color.Blue,
            strokeWidth = 4.dp,
            backgroundColor = Color.White,
            showGrid = true,
            gridColor = Color.Gray.copy(alpha = 0.3f),
            showActions = true,
            borderStroke = BorderStroke(2.dp, Color.Gray),
            cornerShape = RoundedCornerShape(12.dp)
        ),
        state = signatureState,
        onActionClicked = { action ->
            when (action) {
                SignatureAction.CLEAR -> signatureState.clear()
                SignatureAction.UNDO -> signatureState.undo()
                SignatureAction.REDO -> signatureState.redo()
                SignatureAction.SAVE -> {
                    signature?.let { /* Save to file */ }
                }
            }
        }
    )
}
```

## Core Components

### ComposeSign

The main composable provides multiple overloads for different levels of customization:

```kotlin
// Simple usage
ComposeSign(onSignatureUpdate = { signature = it })

// Basic customization
ComposeSign(
    onSignatureUpdate = { signature = it },
    strokeColor = Color.Blue,
    strokeWidth = 4.dp,
    showGrid = true
)

// Full configuration
ComposeSign(
    onSignatureUpdate = { signature = it },
    config = SignatureConfig(/* ... */),
    state = rememberSignatureState(),
    onActionClicked = { action -> /* handle action */ }
)
```

### SignatureConfig

Configuration object for controlling visual appearance and behavior:

```kotlin
data class SignatureConfig(
    val strokeColor: Color = Color.Black,
    val strokeWidth: Dp = 3.dp,
    val backgroundColor: Color = Color.White,
    val borderStroke: BorderStroke? = BorderStroke(1.dp, Color.Gray),
    val cornerShape: CornerBasedShape = RoundedCornerShape(8.dp),
    val showGrid: Boolean = false,
    val gridColor: Color = Color.Gray.copy(alpha = 0.3f),
    val gridSpacing: Dp = 20.dp,
    val isFullScreen: Boolean = false,
    val minHeight: Dp = 200.dp,
    val maxHeight: Dp = 400.dp,
    val showActions: Boolean = false,
    val enableSmoothDrawing: Boolean = true
)
```

Predefined configurations are available:

- `SignatureConfig.Default` - Standard configuration
- `SignatureConfig.Fullscreen` - Fullscreen mode with actions
- `SignatureConfig.WithGrid` - Includes grid overlay
- `SignatureConfig.ThickStroke` - Thicker stroke width
- `SignatureConfig.Professional` - Professional document styling
- `SignatureConfig.Creative` - Creative color scheme
- `SignatureConfig.FormIntegration` - Compact form styling

### SignatureState

Manages signature paths, input state, and history:

```kotlin
val state = rememberSignatureState()

// Available properties
state.paths           // List of signature paths
state.inputState      // Current input state (IDLE, DRAWING, COMPLETED)
state.signature       // Generated ImageBitmap
state.canUndo         // Whether undo is available
state.canRedo         // Whether redo is available

// Operations
state.addPath(path)
state.clear()
state.undo()
state.redo()

// Extension functions
state.isEmpty()
state.getSignatureBounds()
state.getMetadata()
state.exportSignature(width, height)
state.isValid()
```

### Fullscreen Mode

```kotlin
@Composable
fun FullscreenExample() {
    var showFullscreen by remember { mutableStateOf(false) }
    var signature by remember { mutableStateOf<ImageBitmap?>(null) }

    if (showFullscreen) {
        ComposeSignFullscreen(
            onSignatureUpdate = { signature = it },
            onDismiss = { showFullscreen = false },
            config = SignatureConfig.Fullscreen
        )
    }
}
```

## Customization

### Theme Variants

```kotlin
// Dark theme
ComposeSign(config = SignatureConfig.Default.asDarkTheme())

// High contrast for accessibility
ComposeSign(config = SignatureConfig.Default.asAccessible(highContrast = true))

// Form integration
ComposeSign(config = SignatureConfig.FormIntegration)
```

### Custom Stroke Colors

```kotlin
@Composable
fun MultiColorSignature() {
    var currentColor by remember { mutableStateOf(Color.Black) }

    ComposeSign(
        onSignatureUpdate = { signature = it },
        strokeColor = currentColor,
        strokeWidth = 4.dp,
        showGrid = true
    )

    ColorPicker(
        selectedColor = currentColor,
        onColorSelected = { currentColor = it }
    )
}
```

## Analytics and Validation

### Signature Metadata

```kotlin
val state = rememberSignatureState()

val metadata = state.getMetadata()
println("Path count: ${metadata.pathCount}")
println("Complexity: ${metadata.complexityDescription()}")
println("Total length: ${metadata.totalLength}")

val bounds = state.getSignatureBounds()
bounds?.let {
    println("Dimensions: ${it.width} x ${it.height}")
    println("Area: ${it.area()}")
}
```

### Validation

```kotlin
val isValid = state.isValid(
    minPaths = 5,
    minLength = 100f,
    minComplexity = 20
)
```

## Architecture

The library follows SOLID principles with clear separation of concerns:

- **ComposeSign.kt**: Main composable component with gesture handling and rendering
- **SignatureConfig.kt**: Configuration management with builder methods
- **SignatureState.kt**: State management with undo/redo functionality
- **SignatureModels.kt**: Core data models (SignaturePath, SignatureBounds, SignatureMetadata)
- **SignatureUtils.kt**: Utility functions for rendering, analysis, and optimization
- **SignatureActions.kt**: Action handling system for built-in operations
- **SignatureExtensions.kt**: Extension functions for convenience APIs

## License

```
Copyright 2026 Niyaj Ali

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
