/*
 * Copyright 2024 Niyaj Ali.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.niyajali.compose.sign

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

/**
 * Extension functions for SignatureState to enhance usability and functionality
 */

/**
 * Check if the signature is empty (no paths drawn)
 *
 * @return True if no signature paths exist
 */
public fun SignatureState.isEmpty(): Boolean = paths.isEmpty()

/**
 * Check if the signature is not empty (has paths drawn)
 *
 * @return True if signature paths exist
 */
public fun SignatureState.isNotEmpty(): Boolean = paths.isNotEmpty()

/**
 * Get the bounds of the current signature
 *
 * @return SignatureBounds containing the bounding rectangle, or null if empty
 */
public fun SignatureState.getSignatureBounds(): SignatureBounds? =
    calculateSignatureBounds(paths)

/**
 * Get metadata about the current signature
 *
 * @return SignatureMetadata containing analysis information
 */
public fun SignatureState.getMetadata(): SignatureMetadata =
    generateSignatureMetadata(paths)

/**
 * Export the signature as an ImageBitmap with custom dimensions
 *
 * @param width Width of the exported bitmap
 * @param height Height of the exported bitmap
 * @param backgroundColor Background color for the export
 * @return ImageBitmap containing the signature, or null if empty
 */
public fun SignatureState.exportSignature(
    width: Int,
    height: Int,
    backgroundColor: Color = Color.White
): ImageBitmap? {
    return if (isNotEmpty()) {
        pathsToImageBitmap(width, height, paths, backgroundColor)
    } else null
}

/**
 * Export the signature with scaling to fit the signature bounds
 *
 * @param targetWidth Target width for the export
 * @param targetHeight Target height for the export
 * @param maintainAspectRatio Whether to maintain aspect ratio
 * @param backgroundColor Background color for the export
 * @return Scaled ImageBitmap, or null if empty
 */
public fun SignatureState.exportScaledSignature(
    targetWidth: Int,
    targetHeight: Int,
    maintainAspectRatio: Boolean = true,
    backgroundColor: Color = Color.White
): ImageBitmap? {
    if (isEmpty()) return null

    val scaledPaths = scaleSignaturePaths(
        paths = paths,
        targetWidth = targetWidth.toFloat(),
        targetHeight = targetHeight.toFloat(),
        maintainAspectRatio = maintainAspectRatio
    )

    return pathsToImageBitmap(targetWidth, targetHeight, scaledPaths, backgroundColor)
}

/**
 * Get the total length of all signature paths
 *
 * @return Total length of all drawn paths
 */
public fun SignatureState.getTotalLength(): Float =
    paths.sumOf { it.length().toDouble() }.toFloat()

/**
 * Get the complexity score of the signature (0-100)
 *
 * @return Complexity score where 0 is very simple and 100 is very complex
 */
public fun SignatureState.getComplexityScore(): Int =
    calculateSignatureComplexity(paths)

/**
 * Validate the signature against minimum requirements
 *
 * @param minPaths Minimum number of paths required
 * @param minLength Minimum total length required
 * @param minComplexity Minimum complexity score required
 * @return True if signature meets all requirements
 */
public fun SignatureState.isValid(
    minPaths: Int = 5,
    minLength: Float = 100f,
    minComplexity: Int = 10
): Boolean = validateSignature(paths, minPaths, minLength, minComplexity)

/**
 * Get a human-readable description of the signature
 *
 * @return String describing the signature characteristics
 */
public fun SignatureState.getDescription(): String {
    if (isEmpty()) return "No signature"

    val metadata = getMetadata()
    return buildString {
        append("Signature with ${metadata.pathCount} strokes, ")
        append("${metadata.complexityDescription().lowercase()} complexity")
        metadata.bounds?.let { bounds ->
            append(", ${bounds.width.toInt()}x${bounds.height.toInt()} area")
        }
    }
}

/**
 * Extension functions for SignatureConfig to create variants
 */

/**
 * Create a copy of the config with dark theme colors
 *
 * @return SignatureConfig optimized for dark themes
 */
public fun SignatureConfig.asDarkTheme(): SignatureConfig = copy(
    strokeColor = Color.White,
    backgroundColor = Color.Black,
    gridColor = Color.White.copy(alpha = 0.3f),
    borderStroke = borderStroke?.copy(brush = SolidColor(Color.White))
)

/**
 * Create a copy of the config with light theme colors
 *
 * @return SignatureConfig optimized for light themes
 */
public fun SignatureConfig.asLightTheme(): SignatureConfig = copy(
    strokeColor = Color.Black,
    backgroundColor = Color.White,
    gridColor = Color.Gray.copy(alpha = 0.3f),
    borderStroke = borderStroke?.copy(brush = SolidColor(Color.Gray))
)

/**
 * Create a copy of the config optimized for accessibility
 *
 * @param highContrast Whether to use high contrast colors
 * @return SignatureConfig with accessibility improvements
 */
public fun SignatureConfig.asAccessible(highContrast: Boolean = true): SignatureConfig = copy(
    strokeWidth = if (highContrast) strokeWidth * 1.5f else strokeWidth,
    strokeColor = if (highContrast) Color.Black else strokeColor,
    backgroundColor = if (highContrast) Color.White else backgroundColor,
    showGrid = true,
    gridSpacing = gridSpacing * 1.2f // Larger grid for better visibility
)

/**
 * Extension functions for SignaturePath
 */

/**
 * Get the angle of the path in degrees
 *
 * @return Angle in degrees (0-360)
 */
public fun SignaturePath.getAngle(): Float {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val angle = atan2(dy, dx) * 180 / PI
    return if (angle < 0) angle.toFloat() + 360f else angle.toFloat()
}

/**
 * Check if this path is approximately horizontal
 *
 * @param threshold Angle threshold in degrees
 * @return True if the path is within the threshold of horizontal
 */
public fun SignaturePath.isHorizontal(threshold: Float = 15f): Boolean {
    val angle = getAngle()
    return angle <= threshold || angle >= (360f - threshold) ||
            (angle >= (180f - threshold) && angle <= (180f + threshold))
}

/**
 * Check if this path is approximately vertical
 *
 * @param threshold Angle threshold in degrees
 * @return True if the path is within the threshold of vertical
 */
public fun SignaturePath.isVertical(threshold: Float = 15f): Boolean {
    val angle = getAngle()
    return (angle >= (90f - threshold) && angle <= (90f + threshold)) ||
            (angle >= (270f - threshold) && angle <= (270f + threshold))
}

/**
 * Extension functions for SignatureBounds
 */

/**
 * Check if the bounds represent a mostly square area
 *
 * @param tolerance Tolerance for aspect ratio (0.0 = perfect square, 1.0 = any ratio)
 * @return True if the bounds are approximately square
 */
public fun SignatureBounds.isSquare(tolerance: Float = 0.2f): Boolean {
    if (!isValid()) return false
    val aspectRatio = width / height
    return abs(aspectRatio - 1f) <= tolerance
}

/**
 * Check if the bounds represent a landscape orientation
 *
 * @return True if width is greater than height
 */
public fun SignatureBounds.isLandscape(): Boolean = isValid() && width > height

/**
 * Check if the bounds represent a portrait orientation
 *
 * @return True if height is greater than width
 */
public fun SignatureBounds.isPortrait(): Boolean = isValid() && height > width

/**
 * Get the aspect ratio of the bounds
 *
 * @return Aspect ratio (width/height), or 1.0 if invalid
 */
public fun SignatureBounds.getAspectRatio(): Float =
    if (isValid() && height > 0) width / height else 1f

/**
 * Extension functions for List<SignaturePath>
 */

/**
 * Get all unique colors used in the signature paths
 *
 * @return Set of unique colors
 */
public fun List<SignaturePath>.getUniqueColors(): Set<Color> =
    map { it.color }.toSet()

/**
 * Filter paths by color
 *
 * @param color Color to filter by
 * @return List of paths with the specified color
 */
public fun List<SignaturePath>.filterByColor(color: Color): List<SignaturePath> =
    filter { it.color == color }

/**
 * Get paths within a specific stroke width range
 *
 * @param minWidth Minimum stroke width
 * @param maxWidth Maximum stroke width
 * @return List of paths within the width range
 */
public fun List<SignaturePath>.filterByStrokeWidth(
    minWidth: Float,
    maxWidth: Float
): List<SignaturePath> = filter { it.strokeWidth in minWidth..maxWidth }

/**
 * Get the average stroke width of all paths
 *
 * @return Average stroke width, or 0 if empty
 */
public fun List<SignaturePath>.getAverageStrokeWidth(): Float =
    if (isEmpty()) 0f else map { it.strokeWidth }.average().toFloat()

/**
 * Utility extension for ImageBitmap
 */

/**
 * Get a description of the bitmap dimensions
 *
 * @return String describing the bitmap size
 */
public fun ImageBitmap.getDimensionDescription(): String = "${width}x${height} pixels"

/**
 * Check if the bitmap is in landscape orientation
 *
 * @return True if width > height
 */
public fun ImageBitmap.isLandscape(): Boolean = width > height

/**
 * Check if the bitmap is in portrait orientation
 *
 * @return True if height > width
 */
public fun ImageBitmap.isPortrait(): Boolean = height > width

/**
 * Get the aspect ratio of the bitmap
 *
 * @return Aspect ratio (width/height)
 */
public fun ImageBitmap.getAspectRatio(): Float =
    if (height > 0) width.toFloat() / height.toFloat() else 1f