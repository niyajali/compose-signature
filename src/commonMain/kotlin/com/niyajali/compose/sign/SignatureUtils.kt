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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Converts a collection of signature paths to an [ImageBitmap].
 *
 * This function renders the provided paths onto a new bitmap with the specified dimensions
 * and background color. Each path is drawn as a line with round stroke caps for smooth appearance.
 *
 * @param width The width of the output bitmap in pixels. Must be greater than zero.
 * @param height The height of the output bitmap in pixels. Must be greater than zero.
 * @param paths The list of [SignaturePath] segments to render onto the bitmap.
 * @param backgroundColor The fill color for the bitmap background before drawing paths.
 * @param density The display density factor used for rendering calculations.
 * @return A new [ImageBitmap] containing the rendered signature.
 * @throws IllegalArgumentException If width or height is not positive.
 */
internal fun pathsToImageBitmap(
    width: Int,
    height: Int,
    paths: List<SignaturePath>,
    backgroundColor: Color = Color.White,
    density: Float = 1f
): ImageBitmap {
    if (width <= 0 || height <= 0) {
        throw IllegalArgumentException("Width and height must be positive")
    }

    val imageBitmap = ImageBitmap(width, height)
    Canvas(imageBitmap).apply {
        CanvasDrawScope().draw(
            density = Density(density, density),
            layoutDirection = LayoutDirection.Ltr,
            canvas = this,
            size = Size(width.toFloat(), height.toFloat()),
        ) {
            drawRect(backgroundColor)

            paths.forEach { path ->
                drawLine(
                    color = path.color,
                    start = path.start,
                    end = path.end,
                    strokeWidth = path.strokeWidth,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
    return imageBitmap
}

/**
 * Draws a grid pattern within the current draw scope.
 *
 * This function renders evenly spaced horizontal and vertical lines across the entire
 * drawing area. The grid serves as a visual guide for signature placement and alignment.
 *
 * @receiver The [DrawScope] in which to draw the grid.
 * @param gridColor The color to use for grid lines.
 * @param gridSpacing The distance in pixels between adjacent grid lines.
 *                    If zero or negative, no grid is drawn.
 */
internal fun DrawScope.drawGrid(
    gridColor: Color,
    gridSpacing: Float,
) {
    if (gridSpacing <= 0) return

    val width = size.width
    val height = size.height

    var x = 0f
    while (x <= width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1f,
        )
        x += gridSpacing
    }

    var y = 0f
    while (y <= height) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1f,
        )
        y += gridSpacing
    }
}

/**
 * Calculates the bounding rectangle that encompasses all provided signature paths.
 *
 * This function iterates through all path points to find the minimum and maximum
 * coordinates, then applies padding based on the maximum stroke width to ensure
 * no stroke content is clipped at the boundaries.
 *
 * @param paths The list of [SignaturePath] segments to analyze.
 * @return A [SignatureBounds] representing the enclosing rectangle, or null if the list is empty.
 */
internal fun calculateSignatureBounds(paths: List<SignaturePath>): SignatureBounds? {
    if (paths.isEmpty()) return null

    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = -Float.MAX_VALUE
    var maxY = -Float.MAX_VALUE

    paths.forEach { path ->
        minX = minOf(minX, path.start.x, path.end.x)
        minY = minOf(minY, path.start.y, path.end.y)
        maxX = maxOf(maxX, path.start.x, path.end.x)
        maxY = maxOf(maxY, path.start.y, path.end.y)
    }

    val maxStrokeWidth = paths.maxOfOrNull { it.strokeWidth } ?: 0f
    val padding = maxStrokeWidth / 2f

    val left = (minX - padding).coerceAtLeast(0f)
    val top = (minY - padding).coerceAtLeast(0f)
    val right = maxX + padding
    val bottom = maxY + padding

    return SignatureBounds(
        left = left,
        top = top,
        right = right,
        bottom = bottom,
    )
}

/**
 * Computes a normalized complexity score for a signature based on multiple factors.
 *
 * The complexity score considers the number of paths, total stroke length, and the
 * area covered by the signature bounds. Each factor contributes to the final score
 * which is capped at 100 to provide a normalized measure suitable for comparison.
 *
 * Scoring breakdown:
 * - Path count: Up to 40 points based on number of stroke segments
 * - Total length: Up to 30 points based on cumulative path length
 * - Bounds area: Up to 30 points based on the area covered
 *
 * @param paths The list of [SignaturePath] segments to analyze.
 * @return An integer complexity score from 0 to 100, where higher values indicate
 *         more complex signatures.
 */
internal fun calculateSignatureComplexity(paths: List<SignaturePath>): Int {
    if (paths.isEmpty()) return 0

    val pathCount = paths.size
    val totalLength = paths.sumOf { it.length().toDouble() }.toFloat()
    val bounds = calculateSignatureBounds(paths)

    val pathCountScore = minOf(pathCount * 2, 40)
    val lengthScore = minOf((totalLength / 100f).toInt(), 30)
    val boundsScore = bounds?.let {
        if (it.isValid()) {
            minOf(((it.width * it.height) / 10000f).toInt(), 30)
        } else 0
    } ?: 0

    return minOf(pathCountScore + lengthScore + boundsScore, 100)
}

/**
 * Generates comprehensive metadata about a signature from its path data.
 *
 * This function aggregates various analytical metrics including path count,
 * total stroke length, bounding dimensions, and complexity score into a
 * single metadata object for convenient access and storage.
 *
 * @param paths The list of [SignaturePath] segments to analyze.
 * @return A [SignatureMetadata] object containing the computed metrics.
 */
internal fun generateSignatureMetadata(paths: List<SignaturePath>): SignatureMetadata {
    val bounds = calculateSignatureBounds(paths)
    val totalLength = paths.sumOf { it.length().toDouble() }.toFloat()
    val complexity = calculateSignatureComplexity(paths)

    return SignatureMetadata(
        pathCount = paths.size,
        totalLength = totalLength,
        bounds = bounds,
        complexity = complexity,
    )
}

/**
 * Applies smoothing to a collection of signature paths to reduce jaggedness.
 *
 * This function uses a weighted averaging algorithm to smooth the transition
 * between adjacent path segments, resulting in more fluid and natural-looking
 * signature strokes. The smoothing factor controls the balance between original
 * positions and averaged positions.
 *
 * The first and last paths are preserved unchanged to maintain the signature's
 * start and end points.
 *
 * @param paths The list of [SignaturePath] segments to smooth.
 * @param smoothingFactor The blending weight between original and smoothed positions.
 *                        Values closer to 0 preserve more original detail, while values
 *                        closer to 1 produce smoother results. Clamped to range 0 to 1.
 * @return A new list of [SignaturePath] with smoothed coordinates, or the original
 *         list if it contains fewer than 3 paths or smoothing factor is zero or negative.
 */
internal fun smoothSignaturePaths(
    paths: List<SignaturePath>,
    smoothingFactor: Float = 0.3f,
): List<SignaturePath> {
    if (paths.size < 3 || smoothingFactor <= 0f) return paths

    val factor = smoothingFactor.coerceIn(0f, 1f)
    val smoothedPaths = mutableListOf<SignaturePath>()

    smoothedPaths.add(paths.first())

    for (i in 1 until paths.size - 1) {
        val prevPath = paths[i - 1]
        val currentPath = paths[i]
        val nextPath = paths[i + 1]

        val smoothedStart = Offset(
            x = currentPath.start.x * (1f - factor) +
                    (prevPath.end.x + nextPath.start.x) * factor / 2f,
            y = currentPath.start.y * (1f - factor) +
                    (prevPath.end.y + nextPath.start.y) * factor / 2f,
        )

        val smoothedEnd = Offset(
            x = currentPath.end.x * (1f - factor) +
                    (prevPath.start.x + nextPath.end.x) * factor / 2f,
            y = currentPath.end.y * (1f - factor) +
                    (prevPath.start.y + nextPath.end.y) * factor / 2f,
        )

        smoothedPaths.add(
            currentPath.copy(
                start = smoothedStart,
                end = smoothedEnd,
            ),
        )
    }

    if (paths.size >= 2) {
        smoothedPaths.add(paths.last())
    }

    return smoothedPaths
}

/**
 * Validates whether a signature meets minimum requirements for authenticity.
 *
 * This function checks multiple criteria to determine if the provided paths
 * represent a valid signature attempt rather than a simple mark, accidental
 * input, or insufficient drawing.
 *
 * @param paths The list of [SignaturePath] segments to validate.
 * @param minPaths The minimum number of path segments required for validity.
 * @param minLength The minimum cumulative stroke length in pixels required.
 * @param minComplexity The minimum complexity score required.
 * @return True if all validation criteria are satisfied, false otherwise.
 */
internal fun validateSignature(
    paths: List<SignaturePath>,
    minPaths: Int = 5,
    minLength: Float = 100f,
    minComplexity: Int = 10,
): Boolean {
    if (paths.size < minPaths) return false

    val totalLength = paths.sumOf { it.length().toDouble() }.toFloat()
    if (totalLength < minLength) return false

    val complexity = calculateSignatureComplexity(paths)
    return complexity >= minComplexity
}

/**
 * Optimizes a collection of signature paths by removing insignificant segments.
 *
 * This function filters out very short path segments that may be the result of
 * input noise or accidental touches, and removes duplicate paths that occupy
 * the same effective position. This optimization can improve rendering performance
 * and reduce storage requirements.
 *
 * @param paths The list of [SignaturePath] segments to optimize.
 * @param minPathLength The minimum length a path must have to be retained.
 *                      Paths shorter than this value are removed.
 * @return A filtered list of [SignaturePath] with short and duplicate paths removed.
 */
internal fun optimizeSignaturePaths(
    paths: List<SignaturePath>,
    minPathLength: Float = 2f,
): List<SignaturePath> {
    return paths.filter { path ->
        path.length() >= minPathLength
    }.distinctBy { path ->
        "${path.start.x.toInt()}-${path.start.y.toInt()}-${path.end.x.toInt()}-${path.end.y.toInt()}"
    }
}

/**
 * Creates a copy of the signature paths with a different stroke color.
 *
 * This utility function is useful for creating alternate color versions of
 * a signature for display in different contexts or for accessibility purposes.
 *
 * @param paths The list of [SignaturePath] segments to recolor.
 * @param newColor The new color to apply to all paths.
 * @return A new list of [SignaturePath] with the updated color.
 */
internal fun recolorSignature(
    paths: List<SignaturePath>,
    newColor: Color,
): List<SignaturePath> {
    return paths.map { it.copy(color = newColor) }
}

/**
 * Scales signature paths to fit within target dimensions.
 *
 * This function transforms the coordinate space of all paths to fit within
 * the specified target dimensions. It supports both uniform scaling that
 * preserves aspect ratio and non-uniform scaling that fills the entire
 * target area.
 *
 * @param paths The list of [SignaturePath] segments to scale.
 * @param targetWidth The desired width of the scaled signature.
 * @param targetHeight The desired height of the scaled signature.
 * @param maintainAspectRatio If true, scales uniformly to fit within target bounds
 *                            while preserving the original proportions. If false,
 *                            stretches to fill the entire target area.
 * @param centerInTarget If true, centers the scaled signature within the target
 *                       dimensions. If false, aligns to the top-left corner.
 * @return A new list of [SignaturePath] with transformed coordinates and scaled
 *         stroke widths, or the original list if it is empty or has invalid bounds.
 */
internal fun scaleSignaturePaths(
    paths: List<SignaturePath>,
    targetWidth: Float,
    targetHeight: Float,
    maintainAspectRatio: Boolean = true,
    centerInTarget: Boolean = true,
): List<SignaturePath> {
    if (paths.isEmpty()) return paths

    val bounds = calculateSignatureBounds(paths) ?: return paths

    if (!bounds.isValid() || bounds.width <= 0f || bounds.height <= 0f) {
        return paths
    }

    val scaleX = targetWidth / bounds.width
    val scaleY = targetHeight / bounds.height

    val finalScaleX: Float
    val finalScaleY: Float

    if (maintainAspectRatio) {
        val scale = minOf(scaleX, scaleY)
        finalScaleX = scale
        finalScaleY = scale
    } else {
        finalScaleX = scaleX
        finalScaleY = scaleY
    }

    val scaledWidth = bounds.width * finalScaleX
    val scaledHeight = bounds.height * finalScaleY

    val offsetX = if (centerInTarget) (targetWidth - scaledWidth) / 2f else 0f
    val offsetY = if (centerInTarget) (targetHeight - scaledHeight) / 2f else 0f

    return paths.map { path ->
        path.copy(
            start = Offset(
                x = (path.start.x - bounds.left) * finalScaleX + offsetX,
                y = (path.start.y - bounds.top) * finalScaleY + offsetY,
            ),
            end = Offset(
                x = (path.end.x - bounds.left) * finalScaleX + offsetX,
                y = (path.end.y - bounds.top) * finalScaleY + offsetY,
            ),
            strokeWidth = path.strokeWidth * minOf(finalScaleX, finalScaleY),
        )
    }
}
