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
 * Checks whether the signature state contains no drawn paths.
 *
 * @receiver The [SignatureState] to check.
 * @return True if the signature state has no paths, false otherwise.
 */
public fun SignatureState.isEmpty(): Boolean = paths.isEmpty()

/**
 * Checks whether the signature state contains at least one drawn path.
 *
 * @receiver The [SignatureState] to check.
 * @return True if the signature state has one or more paths, false otherwise.
 */
public fun SignatureState.isNotEmpty(): Boolean = paths.isNotEmpty()

/**
 * Calculates the bounding rectangle that encompasses all signature paths.
 *
 * The bounds represent the smallest rectangle that contains all drawn strokes,
 * accounting for stroke width to ensure no content is clipped.
 *
 * @receiver The [SignatureState] from which to calculate bounds.
 * @return The [SignatureBounds] containing the signature, or null if the signature is empty.
 */
public fun SignatureState.getSignatureBounds(): SignatureBounds? =
    calculateSignatureBounds(paths)

/**
 * Generates comprehensive metadata about the current signature.
 *
 * The metadata includes path count, total stroke length, bounding dimensions,
 * complexity score, and timestamp information useful for analysis and validation.
 *
 * @receiver The [SignatureState] from which to generate metadata.
 * @return A [SignatureMetadata] object containing signature analysis data.
 */
public fun SignatureState.getMetadata(): SignatureMetadata =
    generateSignatureMetadata(paths)

/**
 * Exports the current signature as an [ImageBitmap] with specified dimensions.
 *
 * This method renders all signature paths to a bitmap image suitable for saving,
 * displaying, or further processing. The signature is rendered at the exact
 * dimensions specified without scaling.
 *
 * @receiver The [SignatureState] containing the signature to export.
 * @param width The width of the output bitmap in pixels. Must be greater than zero.
 * @param height The height of the output bitmap in pixels. Must be greater than zero.
 * @param backgroundColor The background color to fill the bitmap with before rendering strokes.
 * @return The rendered [ImageBitmap], or null if the signature is empty or dimensions are invalid.
 */
public fun SignatureState.exportSignature(
    width: Int,
    height: Int,
    backgroundColor: Color = Color.White
): ImageBitmap? {
    if (isEmpty() || width <= 0 || height <= 0) return null
    return pathsToImageBitmap(width, height, paths, backgroundColor)
}

/**
 * Exports the signature as a scaled [ImageBitmap] to fit target dimensions.
 *
 * This method scales the signature paths to fit within the specified target dimensions,
 * optionally maintaining the original aspect ratio and centering the result.
 * Useful for generating thumbnails or fitting signatures into predefined spaces.
 *
 * @receiver The [SignatureState] containing the signature to export.
 * @param targetWidth The desired width of the output bitmap in pixels.
 * @param targetHeight The desired height of the output bitmap in pixels.
 * @param maintainAspectRatio Whether to preserve the signature's original proportions.
 *                            If true, the signature is scaled uniformly to fit within bounds.
 * @param backgroundColor The background color for the output bitmap.
 * @return The scaled [ImageBitmap], or null if the signature is empty or dimensions are invalid.
 */
public fun SignatureState.exportScaledSignature(
    targetWidth: Int,
    targetHeight: Int,
    maintainAspectRatio: Boolean = true,
    backgroundColor: Color = Color.White
): ImageBitmap? {
    if (isEmpty() || targetWidth <= 0 || targetHeight <= 0) return null

    val scaledPaths = scaleSignaturePaths(
        paths = paths,
        targetWidth = targetWidth.toFloat(),
        targetHeight = targetHeight.toFloat(),
        maintainAspectRatio = maintainAspectRatio,
        centerInTarget = true
    )

    return pathsToImageBitmap(targetWidth, targetHeight, scaledPaths, backgroundColor)
}

/**
 * Calculates the total length of all signature strokes combined.
 *
 * This measurement represents the cumulative distance of all drawn paths,
 * useful for signature complexity analysis and validation.
 *
 * @receiver The [SignatureState] to measure.
 * @return The total stroke length in pixels.
 */
public fun SignatureState.getTotalLength(): Float =
    paths.sumOf { it.length().toDouble() }.toFloat()

/**
 * Computes a complexity score for the signature based on multiple factors.
 *
 * The score considers path count, total stroke length, and bounding area
 * to produce a normalized value between 0 and 100. Higher scores indicate
 * more complex signatures with more detail.
 *
 * @receiver The [SignatureState] to analyze.
 * @return An integer complexity score from 0 to 100.
 */
public fun SignatureState.getComplexityScore(): Int =
    calculateSignatureComplexity(paths)

/**
 * Validates whether the signature meets minimum requirements for authenticity.
 *
 * This method checks multiple criteria to determine if the signature appears
 * to be a genuine attempt rather than a simple mark or accidental input.
 *
 * @receiver The [SignatureState] to validate.
 * @param minPaths The minimum number of paths required for validity.
 * @param minLength The minimum total stroke length in pixels required.
 * @param minComplexity The minimum complexity score required.
 * @return True if all validation criteria are met, false otherwise.
 */
public fun SignatureState.isValid(
    minPaths: Int = 5,
    minLength: Float = 100f,
    minComplexity: Int = 10
): Boolean = validateSignature(paths, minPaths, minLength, minComplexity)

/**
 * Generates a human-readable description of the signature state.
 *
 * The description includes stroke count, complexity assessment, and dimensional
 * information when available. Useful for accessibility features or status displays.
 *
 * @receiver The [SignatureState] to describe.
 * @return A descriptive string summarizing the signature characteristics.
 */
public fun SignatureState.getDescription(): String {
    if (isEmpty()) return "No signature"

    val metadata = getMetadata()
    return buildString {
        append("Signature with ${metadata.pathCount} strokes, ")
        append("${metadata.complexityDescription().lowercase()} complexity")
        metadata.bounds?.let { bounds ->
            if (bounds.isValid()) {
                append(", ${bounds.width.toInt()}x${bounds.height.toInt()} area")
            }
        }
    }
}

/**
 * Creates a dark theme variant of this configuration.
 *
 * This method inverts the color scheme to use light strokes on a dark background,
 * suitable for dark mode user interfaces.
 *
 * @receiver The [SignatureConfig] to convert.
 * @return A new [SignatureConfig] with dark theme colors applied.
 */
public fun SignatureConfig.asDarkTheme(): SignatureConfig = copy(
    strokeColor = Color.White,
    backgroundColor = Color.Black,
    gridColor = Color.White.copy(alpha = 0.3f),
    borderStroke = borderStroke?.copy(brush = SolidColor(Color.White))
)

/**
 * Creates a light theme variant of this configuration.
 *
 * This method applies standard light theme colors with dark strokes on a light background,
 * suitable for light mode user interfaces.
 *
 * @receiver The [SignatureConfig] to convert.
 * @return A new [SignatureConfig] with light theme colors applied.
 */
public fun SignatureConfig.asLightTheme(): SignatureConfig = copy(
    strokeColor = Color.Black,
    backgroundColor = Color.White,
    gridColor = Color.Gray.copy(alpha = 0.3f),
    borderStroke = borderStroke?.copy(brush = SolidColor(Color.Gray))
)

/**
 * Creates an accessibility-enhanced variant of this configuration.
 *
 * This method adjusts visual properties to improve usability for users with
 * visual impairments, including increased stroke width and high contrast colors.
 *
 * @receiver The [SignatureConfig] to enhance.
 * @param highContrast Whether to apply maximum contrast colors for better visibility.
 * @return A new [SignatureConfig] with accessibility enhancements applied.
 */
public fun SignatureConfig.asAccessible(highContrast: Boolean = true): SignatureConfig = copy(
    strokeWidth = if (highContrast) strokeWidth * 1.5f else strokeWidth,
    strokeColor = if (highContrast) Color.Black else strokeColor,
    backgroundColor = if (highContrast) Color.White else backgroundColor,
    showGrid = true,
    gridSpacing = gridSpacing * 1.2f
)

/**
 * Calculates the angle of this path segment in degrees.
 *
 * The angle is measured from the positive x-axis to the direction from start to end,
 * normalized to a range of 0 to 360 degrees.
 *
 * @receiver The [SignaturePath] to measure.
 * @return The angle in degrees, where 0 represents horizontal right direction.
 */
public fun SignaturePath.getAngle(): Float {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val angle = atan2(dy, dx) * 180 / PI
    return if (angle < 0) angle.toFloat() + 360f else angle.toFloat()
}

/**
 * Determines whether this path segment is approximately horizontal.
 *
 * A path is considered horizontal if its angle falls within the specified threshold
 * of the horizontal axis (0, 180, or 360 degrees).
 *
 * @receiver The [SignaturePath] to evaluate.
 * @param threshold The maximum angular deviation from horizontal in degrees.
 * @return True if the path is within the threshold of horizontal orientation.
 */
public fun SignaturePath.isHorizontal(threshold: Float = 15f): Boolean {
    val angle = getAngle()
    return angle <= threshold || angle >= (360f - threshold) ||
            (angle >= (180f - threshold) && angle <= (180f + threshold))
}

/**
 * Determines whether this path segment is approximately vertical.
 *
 * A path is considered vertical if its angle falls within the specified threshold
 * of the vertical axis (90 or 270 degrees).
 *
 * @receiver The [SignaturePath] to evaluate.
 * @param threshold The maximum angular deviation from vertical in degrees.
 * @return True if the path is within the threshold of vertical orientation.
 */
public fun SignaturePath.isVertical(threshold: Float = 15f): Boolean {
    val angle = getAngle()
    return (angle >= (90f - threshold) && angle <= (90f + threshold)) ||
            (angle >= (270f - threshold) && angle <= (270f + threshold))
}

/**
 * Determines whether the bounds represent an approximately square region.
 *
 * This method compares the width and height to check if their ratio
 * falls within an acceptable tolerance of 1:1.
 *
 * @receiver The [SignatureBounds] to evaluate.
 * @param tolerance The maximum acceptable deviation from a 1:1 aspect ratio.
 * @return True if the bounds are approximately square within the given tolerance.
 */
public fun SignatureBounds.isSquare(tolerance: Float = 0.2f): Boolean {
    if (!isValid()) return false
    val aspectRatio = width / height
    return abs(aspectRatio - 1f) <= tolerance
}

/**
 * Determines whether the bounds have a landscape orientation.
 *
 * @receiver The [SignatureBounds] to evaluate.
 * @return True if width is greater than height and bounds are valid.
 */
public fun SignatureBounds.isLandscape(): Boolean = isValid() && width > height

/**
 * Determines whether the bounds have a portrait orientation.
 *
 * @receiver The [SignatureBounds] to evaluate.
 * @return True if height is greater than width and bounds are valid.
 */
public fun SignatureBounds.isPortrait(): Boolean = isValid() && height > width

/**
 * Calculates the aspect ratio of the bounds as width divided by height.
 *
 * @receiver The [SignatureBounds] to measure.
 * @return The aspect ratio, or 1.0 if bounds are invalid or height is zero.
 */
public fun SignatureBounds.getAspectRatio(): Float =
    if (isValid() && height > 0) width / height else 1f

/**
 * Extracts all unique stroke colors used in the path collection.
 *
 * @receiver The list of [SignaturePath] to analyze.
 * @return A set containing all distinct colors used across all paths.
 */
public fun List<SignaturePath>.getUniqueColors(): Set<Color> =
    map { it.color }.toSet()

/**
 * Filters paths to include only those with the specified stroke color.
 *
 * @receiver The list of [SignaturePath] to filter.
 * @param color The color to match against.
 * @return A new list containing only paths with the matching color.
 */
public fun List<SignaturePath>.filterByColor(color: Color): List<SignaturePath> =
    filter { it.color == color }

/**
 * Filters paths to include only those with stroke width within the specified range.
 *
 * @receiver The list of [SignaturePath] to filter.
 * @param minWidth The minimum stroke width to include.
 * @param maxWidth The maximum stroke width to include.
 * @return A new list containing only paths within the stroke width range.
 */
public fun List<SignaturePath>.filterByStrokeWidth(
    minWidth: Float,
    maxWidth: Float
): List<SignaturePath> = filter { it.strokeWidth in minWidth..maxWidth }

/**
 * Calculates the average stroke width across all paths in the collection.
 *
 * @receiver The list of [SignaturePath] to analyze.
 * @return The average stroke width, or 0.0 if the list is empty.
 */
public fun List<SignaturePath>.getAverageStrokeWidth(): Float =
    if (isEmpty()) 0f else map { it.strokeWidth }.average().toFloat()

/**
 * Returns a human-readable string describing the bitmap dimensions.
 *
 * @receiver The [ImageBitmap] to describe.
 * @return A formatted string in the format "WIDTHxHEIGHT pixels".
 */
public fun ImageBitmap.getDimensionDescription(): String = "${width}x${height} pixels"

/**
 * Determines whether the bitmap has a landscape orientation.
 *
 * @receiver The [ImageBitmap] to evaluate.
 * @return True if width is greater than height.
 */
public fun ImageBitmap.isLandscape(): Boolean = width > height

/**
 * Determines whether the bitmap has a portrait orientation.
 *
 * @receiver The [ImageBitmap] to evaluate.
 * @return True if height is greater than width.
 */
public fun ImageBitmap.isPortrait(): Boolean = height > width

/**
 * Calculates the aspect ratio of the bitmap as width divided by height.
 *
 * @receiver The [ImageBitmap] to measure.
 * @return The aspect ratio, or 1.0 if height is zero.
 */
public fun ImageBitmap.getAspectRatio(): Float =
    if (height > 0) width.toFloat() / height.toFloat() else 1f
