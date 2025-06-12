package com.niyajali.compose.sign

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Clock
import kotlin.math.sqrt

/**
 * Represents a single stroke line in the signature
 *
 * @property start Starting point of the line segment
 * @property end Ending point of the line segment
 * @property strokeWidth Width of this specific stroke
 * @property color Color of this specific stroke
 */
@Immutable
public data class SignaturePath(
    val start: Offset,
    val end: Offset,
    val strokeWidth: Float = 5f,
    val color: Color = Color.Black
) {
    /**
     * Calculate the length of this path segment
     */
    public fun length(): Float {
        val dx = end.x - start.x
        val dy = end.y - start.y
        return sqrt(dx * dx + dy * dy)
    }

    /**
     * Get the midpoint of this path segment
     */
    public fun midpoint(): Offset {
        return Offset(
            x = (start.x + end.x) / 2f,
            y = (start.y + end.y) / 2f
        )
    }

    /**
     * Check if this path intersects with another path
     */
    public fun intersectsWith(other: SignaturePath): Boolean {
        // Simplified intersection check for basic collision detection
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
 * Represents the current state of signature input
 */
public enum class SignatureInputState {
    /**
     * No signature activity - pad is empty and ready for input
     */
    IDLE,

    /**
     * User is actively drawing on the signature pad
     */
    DRAWING,

    /**
     * Signature has been completed and is ready for use
     */
    COMPLETED
}

/**
 * Represents bounds information for a signature
 *
 * @property left Leftmost x-coordinate of the signature
 * @property top Topmost y-coordinate of the signature
 * @property right Rightmost x-coordinate of the signature
 * @property bottom Bottommost y-coordinate of the signature
 */
@Immutable
public data class SignatureBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    /**
     * Width of the signature bounds
     */
    public val width: Float get() = right - left

    /**
     * Height of the signature bounds
     */
    public val height: Float get() = bottom - top

    /**
     * Center point of the signature bounds
     */
    public val center: Offset get() = Offset(
        x = left + width / 2f,
        y = top + height / 2f
    )

    /**
     * Top-left corner of the signature bounds
     */
    public val topLeft: Offset get() = Offset(left, top)

    /**
     * Bottom-right corner of the signature bounds
     */
    public val bottomRight: Offset get() = Offset(right, bottom)

    /**
     * Check if the signature bounds are valid (non-zero area)
     */
    public fun isValid(): Boolean = width > 0 && height > 0

    /**
     * Calculate the area of the signature bounds
     */
    public fun area(): Float = width * height

    /**
     * Check if a point is within these bounds
     */
    public fun contains(point: Offset): Boolean {
        return point.x >= left && point.x <= right &&
                point.y >= top && point.y <= bottom
    }
}

/**
 * Represents metadata about a signature
 *
 * @property pathCount Total number of path segments in the signature
 * @property totalLength Total length of all path segments combined
 * @property bounds Bounding rectangle of the signature
 * @property complexity Calculated complexity score (0-100)
 * @property timestamp When the signature was created/last modified
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
     * Check if the signature is considered simple (low complexity)
     */
    public fun isSimple(): Boolean = complexity < 30

    /**
     * Check if the signature is considered complex (high complexity)
     */
    public fun isComplex(): Boolean = complexity > 70

    /**
     * Get a human-readable complexity description
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
 * Export format options for signature
 */
public enum class SignatureExportFormat {
    /**
     * Export as PNG image
     */
    PNG,

    /**
     * Export as JPEG image
     */
    JPEG,

    /**
     * Export as SVG vector format
     */
    SVG,

    /**
     * Export as PDF document
     */
    PDF
}

/**
 * Export configuration for signature output
 *
 * @property format Export format
 * @property width Output width in pixels
 * @property height Output height in pixels
 * @property backgroundColor Background color for the export
 * @property quality Quality setting (0-100, applicable for JPEG)
 * @property includeBorder Whether to include border in export
 * @property cropToBounds Whether to crop the export to signature bounds
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