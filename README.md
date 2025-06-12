# Compose Signature

[![Maven Central](https://img.shields.io/maven-central/v/io.github.niyajali/compose-signature.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.niyajali%22%20AND%20a:%22compose-signature%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform-orange.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-Multiplatform-blue.svg?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-mpp/)

A powerful and highly customizable **Compose Multiplatform signature library** that enables users to draw digital
signatures with advanced features including undo/redo functionality, grid display, fullscreen mode, and comprehensive
state management.

## ✨ Features

- 🎨 **Customizable Appearance**: Colors, stroke width, backgrounds, borders, and shapes
- 📱 **Multiplatform Support**: Android, iOS, Desktop (JVM), Web (JS/WASM)
- ↩️ **Undo/Redo**: Full undo/redo stack with configurable limits
- 🔲 **Grid Display**: Optional grid overlay with customizable spacing and colors
- 🖥️ **Fullscreen Mode**: Dedicated fullscreen signature experience
- 🎯 **Real-time Updates**: Live signature updates instead of completion-only callbacks
- 📊 **State Management**: Comprehensive state tracking with SOLID architecture
- 🔧 **Multiple Overloads**: Simple to advanced usage patterns
- 📋 **Built-in Actions**: Optional action buttons (Clear, Save, Undo, Redo)
- 📈 **Signature Analytics**: Complexity analysis, bounds calculation, metadata generation
- 🎨 **Export Options**: Multiple export formats and scaling options
- ♿ **Accessibility**: High contrast modes and accessibility-friendly options

## 🚀 Quick Start

### Installation

Add to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.niyajali:compose-signature:1.0.1")
}
```

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
                    // Handle save logic
                    signature?.let { /* Save to file */ }
                }
            }
        }
    )
}
```

## 📚 Documentation

### Core Components

#### ComposeSign

The main composable with multiple overloads for different use cases:

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

#### SignatureConfig

Comprehensive configuration object:

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

#### SignatureState

Enhanced state management with undo/redo:

```kotlin
val state = rememberSignatureState()

// State properties
state.paths // List of signature paths
state.inputState // Current input state (IDLE, DRAWING, COMPLETED)
state.signature // Generated ImageBitmap
state.canUndo // Whether undo is available
state.canRedo // Whether redo is available

// State operations
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

### Predefined Configurations

```kotlin
// Default minimal configuration
SignatureConfig.Default

// Fullscreen with actions
SignatureConfig.Fullscreen

// With grid enabled
SignatureConfig.WithGrid

// Thick strokes
SignatureConfig.ThickStroke

// Professional documents
SignatureConfig.Professional

// Creative/artistic
SignatureConfig.Creative
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

## 🎨 Customization Examples

### Dark Theme

```kotlin
ComposeSign(
    config = SignatureConfig.Default.asDarkTheme()
)
```

### High Contrast (Accessibility)

```kotlin
ComposeSign(
    config = SignatureConfig.Default.asAccessible(highContrast = true)
)
```

### Form Integration

```kotlin
ComposeSign(
    config = SignatureConfig.FormIntegration
)
```

### Multi-Color Signature

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

    // Color picker UI
    ColorPicker(
        selectedColor = currentColor,
        onColorSelected = { currentColor = it }
    )
}
```

## 📈 Analytics & Metadata

```kotlin
val state = rememberSignatureState()

// Get signature metadata
val metadata = state.getMetadata()
println("Paths: ${metadata.pathCount}")
println("Complexity: ${metadata.complexityDescription()}")
println("Total length: ${metadata.totalLength}")

// Get bounds information
val bounds = state.getSignatureBounds()
bounds?.let {
    println("Size: ${it.width} x ${it.height}")
    println("Area: ${it.area()}")
}

// Validation
val isValid = state.isValid(
    minPaths = 5,
    minLength = 100f,
    minComplexity = 20
)
```

## 🏗️ Architecture

ComposeSign follows SOLID principles:

- **Single Responsibility**: Each class has a single, well-defined purpose
- **Open/Closed**: Extensible through configuration without modification
- **Liskov Substitution**: Implementations can be substituted seamlessly
- **Interface Segregation**: Clean interfaces for different responsibilities
- **Dependency Inversion**: High-level modules don't depend on low-level details

## 🧪 Testing

```kotlin
class SignatureStateTest {
    @Test
    fun `should handle undo redo correctly`() {
        val state = SignatureState()
        state.addPath(createTestPath())
        state.undo()
        assertTrue(state.isEmpty())
        assertTrue(state.canRedo)
        state.redo()
        assertFalse(state.isEmpty())
    }
}
```

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

```
Copyright 2024 Niyaj Ali

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

## 🔗 Links

- [Documentation](https://github.com/niyajali/compose-signature/wiki)
- [API Reference](https://niyajali.github.io/compose-signature/)
- [Examples](https://github.com/niyajali/compose-signature/tree/main/examples)

## 🙏 Acknowledgments

- Thanks to the Compose Multiplatform team for the amazing framework
- Inspired by the need for better signature solutions in multiplatform apps
- Built with ❤️ for the Kotlin Multiplatform community

---

**ComposeSign** - Making digital signatures simple, powerful, and beautiful across all platforms.
