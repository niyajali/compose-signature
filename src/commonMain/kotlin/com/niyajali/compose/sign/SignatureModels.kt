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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Clock
import kotlin.math.sqrt

/**
 * Represents a single line segment in a signature drawing.
 *
 * Each path is an immutable data structure containing the geometric information
 * and visual properties needed to render one stroke segment. Multiple [SignaturePath]
 * instances are combined to form a complete signature.
 *
 * @property start The starting point coordinates of the line segment.
 * @property end The ending point coordinates of the line segment.
 * @property strokeWidth The thickness of the stroke in pixels.
 * @property color The color used to render this path segment.
 */
@Immutable
public data class SignaturePath(
    val start: Offset,
    val end: Offset,
    val strokeWidth: Float = 5f,
    val color: Color = Color.Black
) {
    /**
     * Calculates the Euclidean length of this path segment.
     *
     * @return The distance in pixels between the start and end points.
     */
    public fun length(): Float {
        val dx = end.x - start.x
        val dy = end.y - start.y
        return sqrt(dx * dx + dy * dy)
    }

    /**
     * Calculates the center point of this path segment.
     *
     * @return An [Offset] representing the midpoint between start and end.
     */
    public fun midpoint(): Offset {
        return Offset(
            x = (start.x + end.x) / 2f,
            y = (start.y + end.y) / 2f
        )
    }

    /**
     * Determines whether this path segment potentially intersects with another.
     *
     * This method uses a simplified proximity-based intersection test that checks
     * if the midpoints of the two paths are close enough relative to their lengths.
     * It is useful for detecting overlapping stroke areas but may produce false
     * positives for paths that are near but do not actually cross.
     *
     * @param other The other [SignaturePath] to test for intersection.
     * @return True if the paths are likely to intersect based on proximity.
     */
    public fun intersectsWith(other: SignaturePath): Boolean {
        val thisLength = length()
        val otherLength = other.length()
        val distance = sqrt(
            (midpoint().x - other.midpoint().x).let { it * it } +
                    (midpoint().y - other.midpoint().y).let { it * it }
        )
        return distance < (thisLength + otherLength) / 4f
    }
}

/**
 * Enumeration of possible input states for signature drawing.
 *
 * This enum tracks the current interaction state of the signature pad,
 * enabling appropriate UI feedback and state management throughout the signing process.
 */
public enum class SignatureInputState {
    /**
     * No drawing activity is occurring. The signature pad is waiting for user input.
     */
    IDLE,

    /**
     * The user is actively drawing on the signature pad.
     */
    DRAWING,

    /**
     * Drawing has completed and the signature is ready for processing or export.
     */
    COMPLETED
}

/**
 * Represents the rectangular bounding area that contains a signature.
 *
 * This immutable data class defines the minimum axis-aligned rectangle that
 * encompasses all signature paths. It is useful for cropping, scaling, and
 * positioning signatures within other layouts.
 *
 * @property left The x-coordinate of the left edge.
 * @property top The y-coordinate of the top edge.
 * @property right The x-coordinate of the right edge.
 * @property bottom The y-coordinate of the bottom edge.
 */
@Immutable
public data class SignatureBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    /**
     * The horizontal extent of the bounds.
     *
     * Returns 0 if the bounds are inverted (right less than left).
     */
    public val width: Float get() = (right - left).coerceAtLeast(0f)

    /**
     * The vertical extent of the bounds.
     *
     * Returns 0 if the bounds are inverted (bottom less than top).
     */
    public val height: Float get() = (bottom - top).coerceAtLeast(0f)

    /**
     * The geometric center point of the bounds.
     */
    public val center: Offset get() = Offset(
        x = left + width / 2f,
        y = top + height / 2f
    )

    /**
     * The top-left corner coordinates.
     */
    public val topLeft: Offset get() = Offset(left, top)

    /**
     * The bottom-right corner coordinates.
     */
    public val bottomRight: Offset get() = Offset(right, bottom)

    /**
     * Validates whether the bounds represent a meaningful rectangular area.
     *
     * Bounds are considered valid if they have positive width and height,
     * and the top-left corner has non-negative coordinates.
     *
     * @return True if the bounds define a valid rectangular region.
     */
    public fun isValid(): Boolean = width > 0 && height > 0 && left >= 0 && top >= 0

    /**
     * Calculates the total area enclosed by the bounds.
     *
     * @return The area in square pixels.
     */
    public fun area(): Float = width * height

    /**
     * Determines whether a given point lies within the bounds.
     *
     * Points exactly on the boundary are considered inside.
     *
     * @param point The [Offset] to test.
     * @return True if the point is within or on the bounds.
     */
    public fun contains(point: Offset): Boolean {
        return point.x >= left && point.x <= right &&
                point.y >= top && point.y <= bottom
    }
}

/**
 * Contains analytical data about a signature for validation and characterization.
 *
 * This immutable data class aggregates various metrics about a signature,
 * including structural information, dimensional data, and complexity measures.
 * It is generated from signature path data and can be used for signature
 * verification, quality assessment, and user feedback.
 *
 * @property pathCount The total number of individual path segments in the signature.
 * @property totalLength The cumulative length of all path segments in pixels.
 * @property bounds The bounding rectangle containing the signature, or null if empty.
 * @property complexity A normalized score from 0 to 100 indicating signature complexity.
 * @property timestamp The epoch milliseconds when this metadata was generated.
 */
@Immutable
public data class SignatureMetadata(
    val pathCount: Int,
    val totalLength: Float,
    val bounds: SignatureBounds?,
    val complexity: Int,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
) {
    /**
     * Determines if the signature has low complexity.
     *
     * Simple signatures typically have few strokes or minimal detail,
     * which may indicate an incomplete or invalid signature.
     *
     * @return True if the complexity score is below 30.
     */
    public fun isSimple(): Boolean = complexity < 30

    /**
     * Determines if the signature has high complexity.
     *
     * Complex signatures typically have many strokes and significant detail,
     * indicating a more elaborate signing style.
     *
     * @return True if the complexity score is above 70.
     */
    public fun isComplex(): Boolean = complexity > 70

    /**
     * Returns a human-readable description of the signature complexity level.
     *
     * The description categorizes the complexity into five tiers based on
     * the numerical score.
     *
     * @return A descriptive string such as "Simple", "Moderate", or "Complex".
     */
    public fun complexityDescription(): String = when {
        complexity < 20 -> "Very Simple"
        complexity < 40 -> "Simple"
        complexity < 60 -> "Moderate"
        complexity < 80 -> "Complex"
        else -> "Very Complex"
    }
}

/**
 * Enumeration of supported image formats for signature export.
 *
 * These formats represent the available output options when converting
 * a signature to a file for storage or transmission.
 */
public enum class SignatureExportFormat {
    /**
     * Portable Network Graphics format with lossless compression.
     * Recommended for signatures requiring transparency or precise reproduction.
     */
    PNG,

    /**
     * Joint Photographic Experts Group format with lossy compression.
     * Suitable for signatures where file size is a priority over quality.
     */
    JPEG,

    /**
     * Scalable Vector Graphics format preserving path data.
     * Ideal for signatures that need to be scaled without quality loss.
     */
    SVG,

    /**
     * Portable Document Format for document integration.
     * Useful for embedding signatures directly into PDF documents.
     */
    PDF
}

/**
 * Configuration options for exporting signatures to image files.
 *
 * This immutable data class specifies the parameters used when converting
 * signature path data to an exportable image format, including dimensions,
 * format, quality settings, and visual options.
 *
 * @property format The target image format for export.
 * @property width The width of the exported image in pixels.
 * @property height The height of the exported image in pixels.
 * @property backgroundColor The background color to use in the exported image.
 * @property quality The compression quality percentage for lossy formats (0-100).
 * @property includeBorder Whether to render a border around the signature in the export.
 * @property cropToBounds Whether to crop the export to the signature bounds,
 *                        removing excess whitespace.
 *
 * @throws IllegalArgumentException If width or height is not positive, or if quality
 *                                  is outside the valid range.
 */
@Immutable
public data class SignatureExportConfig(
    val format: SignatureExportFormat = SignatureExportFormat.PNG,
    val width: Int = 800,
    val height: Int = 400,
    val backgroundColor: Color = Color.White,
    val quality: Int = 90,
    val includeBorder: Boolean = false,
    val cropToBounds: Boolean = false
) {
    init {
        require(width > 0) { "Width must be positive" }
        require(height > 0) { "Height must be positive" }
        require(quality in 0..100) { "Quality must be between 0 and 100" }
    }
}
