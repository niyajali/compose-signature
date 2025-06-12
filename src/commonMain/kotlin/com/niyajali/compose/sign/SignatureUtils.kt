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
 * Converts signature paths to ImageBitmap
 *
 * @param width Width of the resulting bitmap
 * @param height Height of the resulting bitmap
 * @param paths List of signature paths to draw
 * @param backgroundColor Background color of the bitmap
 * @return Generated ImageBitmap containing the signature
 */
internal fun pathsToImageBitmap(
    width: Int,
    height: Int,
    paths: List<SignaturePath>,
    backgroundColor: Color = Color.White,
): ImageBitmap {
    if (width <= 0 || height <= 0) {
        throw IllegalArgumentException("Width and height must be positive")
    }

    val imageBitmap = ImageBitmap(width, height)
    Canvas(imageBitmap).apply {
        CanvasDrawScope().draw(
            density = Density(1f, 1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = this,
            size = Size(width.toFloat(), height.toFloat()),
        ) {
            // Draw background
            drawRect(backgroundColor)

            // Draw all signature paths
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
 * Draws grid lines on the canvas
 *
 * @param gridColor Color of the grid lines
 * @param gridSpacing Spacing between grid lines in pixels
 */
internal fun DrawScope.drawGrid(
    gridColor: Color,
    gridSpacing: Float,
) {
    if (gridSpacing <= 0) return

    val width = size.width
    val height = size.height

    // Draw vertical lines
    var x = gridSpacing
    while (x < width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1f,
        )
        x += gridSpacing
    }

    // Draw horizontal lines
    var y = gridSpacing
    while (y < height) {
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
 * Calculate the bounds of a list of signature paths
 *
 * @param paths List of signature paths
 * @return SignatureBounds containing the bounding rectangle, or null if paths is empty
 */
internal fun calculateSignatureBounds(paths: List<SignaturePath>): SignatureBounds? {
    if (paths.isEmpty()) return null

    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    paths.forEach { path ->
        // Check start point
        minX = minOf(minX, path.start.x)
        minY = minOf(minY, path.start.y)
        maxX = maxOf(maxX, path.start.x)
        maxY = maxOf(maxY, path.start.y)

        // Check end point
        minX = minOf(minX, path.end.x)
        minY = minOf(minY, path.end.y)
        maxX = maxOf(maxX, path.end.x)
        maxY = maxOf(maxY, path.end.y)
    }

    // Add some padding based on stroke width
    val maxStrokeWidth = paths.maxOfOrNull { it.strokeWidth } ?: 0f
    val padding = maxStrokeWidth / 2f

    return SignatureBounds(
        left = minX - padding,
        top = minY - padding,
        right = maxX + padding,
        bottom = maxY + padding,
    )
}

/**
 * Calculate complexity score for a signature (0-100)
 *
 * @param paths List of signature paths
 * @return Complexity score where 0 is very simple and 100 is very complex
 */
internal fun calculateSignatureComplexity(paths: List<SignaturePath>): Int {
    if (paths.isEmpty()) return 0

    val pathCount = paths.size
    val totalLength = paths.sumOf { it.length().toDouble() }.toFloat()
    val bounds = calculateSignatureBounds(paths)

    // Calculate various complexity factors
    val pathCountScore = minOf(pathCount * 2, 40) // Max 40 points for path count
    val lengthScore = minOf((totalLength / 100f).toInt(), 30) // Max 30 points for total length
    val boundsScore = bounds?.let {
        minOf(((it.width * it.height) / 10000f).toInt(), 30) // Max 30 points for coverage area
    } ?: 0

    return minOf(pathCountScore + lengthScore + boundsScore, 100)
}

/**
 * Generate metadata for a signature
 *
 * @param paths List of signature paths
 * @return SignatureMetadata containing analysis of the signature
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
 * Smooth a signature path using simple averaging
 *
 * @param paths Original paths
 * @param smoothingFactor Factor for smoothing (0.0 = no smoothing, 1.0 = maximum smoothing)
 * @return Smoothed paths
 */
internal fun smoothSignaturePaths(
    paths: List<SignaturePath>,
    smoothingFactor: Float = 0.3f,
): List<SignaturePath> {
    if (paths.size < 3 || smoothingFactor <= 0f) return paths

    val factor = smoothingFactor.coerceIn(0f, 1f)
    val smoothedPaths = mutableListOf<SignaturePath>()

    for (i in 1 until paths.size - 1) {
        val prevPath = paths[i - 1]
        val currentPath = paths[i]
        val nextPath = paths[i + 1]

        // Simple smoothing by averaging adjacent points
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

    // Add first and last paths unchanged
    if (paths.isNotEmpty()) {
        smoothedPaths.add(0, paths.first())
        smoothedPaths.add(paths.last())
    }

    return smoothedPaths
}

/**
 * Validate if a signature meets minimum requirements
 *
 * @param paths List of signature paths
 * @param minPaths Minimum number of paths required
 * @param minLength Minimum total length required
 * @param minComplexity Minimum complexity score required
 * @return True if signature meets all requirements
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
 * Optimize signature paths by removing very short or duplicate paths
 *
 * @param paths Original paths
 * @param minPathLength Minimum length for a path to be kept
 * @return Optimized list of paths
 */
internal fun optimizeSignaturePaths(
    paths: List<SignaturePath>,
    minPathLength: Float = 2f,
): List<SignaturePath> {
    return paths.filter { path ->
        path.length() == minPathLength
    }.distinctBy { path ->
        // Remove near-duplicate paths
        "${path.start.x.toInt()}-${path.start.y.toInt()}-${path.end.x.toInt()}-${path.end.y.toInt()}"
    }
}

/**
 * Convert signature to different color
 *
 * @param paths Original paths
 * @param newColor New color for all paths
 * @return Paths with updated color
 */
internal fun recolorSignature(
    paths: List<SignaturePath>,
    newColor: Color,
): List<SignaturePath> {
    return paths.map { it.copy(color = newColor) }
}

/**
 * Scale signature paths to fit within specified dimensions
 *
 * @param paths Original paths
 * @param targetWidth Target width
 * @param targetHeight Target height
 * @param maintainAspectRatio Whether to maintain aspect ratio
 * @return Scaled paths
 */
internal fun scaleSignaturePaths(
    paths: List<SignaturePath>,
    targetWidth: Float,
    targetHeight: Float,
    maintainAspectRatio: Boolean = true,
): List<SignaturePath> {
    val bounds = calculateSignatureBounds(paths) ?: return paths

    if (!bounds.isValid()) return paths

    val scaleX = targetWidth / bounds.width
    val scaleY = targetHeight / bounds.height

    val scale = if (maintainAspectRatio) {
        minOf(scaleX, scaleY)
    } else {
        1f // Use individual scales for X and Y
    }

    val finalScaleX = if (maintainAspectRatio) scale else scaleX
    val finalScaleY = if (maintainAspectRatio) scale else scaleY

    return paths.map { path ->
        path.copy(
            start = Offset(
                x = (path.start.x - bounds.left) * finalScaleX,
                y = (path.start.y - bounds.top) * finalScaleY,
            ),
            end = Offset(
                x = (path.end.x - bounds.left) * finalScaleX,
                y = (path.end.y - bounds.top) * finalScaleY,
            ),
            strokeWidth = path.strokeWidth * minOf(finalScaleX, finalScaleY),
        )
    }
}