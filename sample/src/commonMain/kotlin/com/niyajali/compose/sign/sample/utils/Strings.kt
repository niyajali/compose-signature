/**
 * Copyright 2026 Sk Niyaj Ali
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.niyajali.compose.sign.sample.utils

import androidx.compose.runtime.Composable
import compose_signature.sample.generated.resources.Res
import compose_signature.sample.generated.resources.action_clear
import compose_signature.sample.generated.resources.action_export
import compose_signature.sample.generated.resources.action_redo
import compose_signature.sample.generated.resources.action_save
import compose_signature.sample.generated.resources.action_undo
import compose_signature.sample.generated.resources.actions_description
import compose_signature.sample.generated.resources.actions_title
import compose_signature.sample.generated.resources.app_name
import compose_signature.sample.generated.resources.app_welcome_subtitle
import compose_signature.sample.generated.resources.app_welcome_title
import compose_signature.sample.generated.resources.area
import compose_signature.sample.generated.resources.available_actions
import compose_signature.sample.generated.resources.background_color
import compose_signature.sample.generated.resources.basic_description
import compose_signature.sample.generated.resources.basic_title
import compose_signature.sample.generated.resources.bitmap_size
import compose_signature.sample.generated.resources.can_redo
import compose_signature.sample.generated.resources.can_undo
import compose_signature.sample.generated.resources.captured_signature
import compose_signature.sample.generated.resources.cd_app_logo
import compose_signature.sample.generated.resources.cd_back_button
import compose_signature.sample.generated.resources.cd_feature_icon
import compose_signature.sample.generated.resources.cd_sample_icon
import compose_signature.sample.generated.resources.cd_signature_preview
import compose_signature.sample.generated.resources.center
import compose_signature.sample.generated.resources.color_black
import compose_signature.sample.generated.resources.color_blue
import compose_signature.sample.generated.resources.color_cream
import compose_signature.sample.generated.resources.color_gray
import compose_signature.sample.generated.resources.color_green
import compose_signature.sample.generated.resources.color_light_blue
import compose_signature.sample.generated.resources.color_light_gray
import compose_signature.sample.generated.resources.color_purple
import compose_signature.sample.generated.resources.color_red
import compose_signature.sample.generated.resources.color_transparent
import compose_signature.sample.generated.resources.color_white
import compose_signature.sample.generated.resources.complexity_progress
import compose_signature.sample.generated.resources.complexity_score
import compose_signature.sample.generated.resources.coordinates
import compose_signature.sample.generated.resources.corner_radius
import compose_signature.sample.generated.resources.corner_radius_value
import compose_signature.sample.generated.resources.dark_config
import compose_signature.sample.generated.resources.dark_description
import compose_signature.sample.generated.resources.dark_title
import compose_signature.sample.generated.resources.description
import compose_signature.sample.generated.resources.destructive_action
import compose_signature.sample.generated.resources.dimensions
import compose_signature.sample.generated.resources.explore_samples
import compose_signature.sample.generated.resources.export_description
import compose_signature.sample.generated.resources.export_settings
import compose_signature.sample.generated.resources.export_size
import compose_signature.sample.generated.resources.export_title
import compose_signature.sample.generated.resources.exported_signature
import compose_signature.sample.generated.resources.feature_background
import compose_signature.sample.generated.resources.feature_cross_platform
import compose_signature.sample.generated.resources.feature_dark_theme
import compose_signature.sample.generated.resources.feature_export
import compose_signature.sample.generated.resources.feature_fullscreen
import compose_signature.sample.generated.resources.feature_grid
import compose_signature.sample.generated.resources.feature_persistence
import compose_signature.sample.generated.resources.feature_stroke_color
import compose_signature.sample.generated.resources.feature_undo_redo
import compose_signature.sample.generated.resources.feature_validation
import compose_signature.sample.generated.resources.grid_color
import compose_signature.sample.generated.resources.grid_description
import compose_signature.sample.generated.resources.grid_settings
import compose_signature.sample.generated.resources.grid_spacing
import compose_signature.sample.generated.resources.grid_spacing_value
import compose_signature.sample.generated.resources.grid_title
import compose_signature.sample.generated.resources.height
import compose_signature.sample.generated.resources.is_empty
import compose_signature.sample.generated.resources.last_action
import compose_signature.sample.generated.resources.library_features
import compose_signature.sample.generated.resources.maintain_aspect_ratio
import compose_signature.sample.generated.resources.nav_back
import compose_signature.sample.generated.resources.nav_home
import compose_signature.sample.generated.resources.no_signature_yet
import compose_signature.sample.generated.resources.path_count
import compose_signature.sample.generated.resources.paths
import compose_signature.sample.generated.resources.pixels
import compose_signature.sample.generated.resources.preview
import compose_signature.sample.generated.resources.requirement
import compose_signature.sample.generated.resources.sample_actions
import compose_signature.sample.generated.resources.sample_actions_desc
import compose_signature.sample.generated.resources.sample_basic
import compose_signature.sample.generated.resources.sample_basic_desc
import compose_signature.sample.generated.resources.sample_custom_style
import compose_signature.sample.generated.resources.sample_custom_style_desc
import compose_signature.sample.generated.resources.sample_dark
import compose_signature.sample.generated.resources.sample_dark_desc
import compose_signature.sample.generated.resources.sample_export
import compose_signature.sample.generated.resources.sample_export_desc
import compose_signature.sample.generated.resources.sample_fullscreen
import compose_signature.sample.generated.resources.sample_fullscreen_desc
import compose_signature.sample.generated.resources.sample_grid
import compose_signature.sample.generated.resources.sample_grid_desc
import compose_signature.sample.generated.resources.sample_validation
import compose_signature.sample.generated.resources.sample_validation_desc
import compose_signature.sample.generated.resources.show_grid
import compose_signature.sample.generated.resources.signature_bounds
import compose_signature.sample.generated.resources.signature_info
import compose_signature.sample.generated.resources.signature_metrics
import compose_signature.sample.generated.resources.signature_preview
import compose_signature.sample.generated.resources.signature_size
import compose_signature.sample.generated.resources.signature_state
import compose_signature.sample.generated.resources.size_large
import compose_signature.sample.generated.resources.size_medium
import compose_signature.sample.generated.resources.size_small
import compose_signature.sample.generated.resources.size_square
import compose_signature.sample.generated.resources.square_pixels
import compose_signature.sample.generated.resources.status_invalid
import compose_signature.sample.generated.resources.status_valid
import compose_signature.sample.generated.resources.stroke_color
import compose_signature.sample.generated.resources.stroke_width
import compose_signature.sample.generated.resources.stroke_width_value
import compose_signature.sample.generated.resources.style_description
import compose_signature.sample.generated.resources.style_title
import compose_signature.sample.generated.resources.total_length
import compose_signature.sample.generated.resources.usage
import compose_signature.sample.generated.resources.usage_code
import compose_signature.sample.generated.resources.usage_description
import compose_signature.sample.generated.resources.validation_description
import compose_signature.sample.generated.resources.validation_requirements
import compose_signature.sample.generated.resources.validation_status
import compose_signature.sample.generated.resources.validation_title
import compose_signature.sample.generated.resources.white_30_opacity
import compose_signature.sample.generated.resources.width
import org.jetbrains.compose.resources.stringResource

/**
 * Centralized string resource access for the Compose Signature Sample app
 *
 * This object provides easy access to all 161+ string resources defined in strings.xml
 * All text should use these resources instead of hardcoded strings for localization support.
 */
object Strings {
    // App strings
    @Composable fun appName() = stringResource(Res.string.app_name)

    @Composable fun appWelcomeTitle() = stringResource(Res.string.app_welcome_title)

    @Composable fun appWelcomeSubtitle() = stringResource(Res.string.app_welcome_subtitle)

    @Composable fun exploreSamples() = stringResource(Res.string.explore_samples)

    @Composable fun libraryFeatures() = stringResource(Res.string.library_features)

    // Navigation
    @Composable fun navHome() = stringResource(Res.string.nav_home)

    @Composable fun navBack() = stringResource(Res.string.nav_back)

    // Sample Screens
    @Composable fun sampleBasic() = stringResource(Res.string.sample_basic)

    @Composable fun sampleBasicDesc() = stringResource(Res.string.sample_basic_desc)

    @Composable fun sampleCustomStyle() = stringResource(Res.string.sample_custom_style)

    @Composable fun sampleCustomStyleDesc() = stringResource(Res.string.sample_custom_style_desc)

    @Composable fun sampleGrid() = stringResource(Res.string.sample_grid)

    @Composable fun sampleGridDesc() = stringResource(Res.string.sample_grid_desc)

    @Composable fun sampleActions() = stringResource(Res.string.sample_actions)

    @Composable fun sampleActionsDesc() = stringResource(Res.string.sample_actions_desc)

    @Composable fun sampleExport() = stringResource(Res.string.sample_export)

    @Composable fun sampleExportDesc() = stringResource(Res.string.sample_export_desc)

    @Composable fun sampleValidation() = stringResource(Res.string.sample_validation)

    @Composable fun sampleValidationDesc() = stringResource(Res.string.sample_validation_desc)

    @Composable fun sampleDark() = stringResource(Res.string.sample_dark)

    @Composable fun sampleDarkDesc() = stringResource(Res.string.sample_dark_desc)

    @Composable fun sampleFullscreen() = stringResource(Res.string.sample_fullscreen)

    @Composable fun sampleFullscreenDesc() = stringResource(Res.string.sample_fullscreen_desc)

    // Basic Signature
    @Composable fun basicTitle() = stringResource(Res.string.basic_title)

    @Composable fun basicDescription() = stringResource(Res.string.basic_description)

    @Composable fun signaturePreview() = stringResource(Res.string.signature_preview)

    @Composable fun capturedSignature() = stringResource(Res.string.captured_signature)

    @Composable fun noSignatureYet() = stringResource(Res.string.no_signature_yet)

    @Composable fun signatureSize(width: Int, height: Int) =
        stringResource(Res.string.signature_size, width, height)

    // Signature State
    @Composable fun signatureState() = stringResource(Res.string.signature_state)

    @Composable fun pathCount() = stringResource(Res.string.path_count)

    @Composable fun isEmpty() = stringResource(Res.string.is_empty)

    @Composable fun canUndo() = stringResource(Res.string.can_undo)

    @Composable fun canRedo() = stringResource(Res.string.can_redo)

    // Actions
    @Composable fun actionClear() = stringResource(Res.string.action_clear)

    @Composable fun actionUndo() = stringResource(Res.string.action_undo)

    @Composable fun actionRedo() = stringResource(Res.string.action_redo)

    @Composable fun actionSave() = stringResource(Res.string.action_save)

    @Composable fun actionExport() = stringResource(Res.string.action_export)

    // Custom Style
    @Composable fun styleTitle() = stringResource(Res.string.style_title)

    @Composable fun styleDescription() = stringResource(Res.string.style_description)

    @Composable fun strokeColor() = stringResource(Res.string.stroke_color)

    @Composable fun strokeWidth() = stringResource(Res.string.stroke_width)

    @Composable fun strokeWidthValue(width: Int) =
        stringResource(Res.string.stroke_width_value, width)

    @Composable fun backgroundColor() = stringResource(Res.string.background_color)

    @Composable fun cornerRadius() = stringResource(Res.string.corner_radius)

    @Composable fun cornerRadiusValue(radius: Int) =
        stringResource(Res.string.corner_radius_value, radius)

    // Colors
    @Composable fun colorBlack() = stringResource(Res.string.color_black)

    @Composable fun colorBlue() = stringResource(Res.string.color_blue)

    @Composable fun colorRed() = stringResource(Res.string.color_red)

    @Composable fun colorGreen() = stringResource(Res.string.color_green)

    @Composable fun colorPurple() = stringResource(Res.string.color_purple)

    @Composable fun colorWhite() = stringResource(Res.string.color_white)

    @Composable fun colorCream() = stringResource(Res.string.color_cream)

    @Composable fun colorLightGray() = stringResource(Res.string.color_light_gray)

    @Composable fun colorLightBlue() = stringResource(Res.string.color_light_blue)

    @Composable fun colorGray() = stringResource(Res.string.color_gray)

    @Composable fun colorTransparent() = stringResource(Res.string.color_transparent)

    // Grid
    @Composable fun gridTitle() = stringResource(Res.string.grid_title)

    @Composable fun gridDescription() = stringResource(Res.string.grid_description)

    @Composable fun gridSettings() = stringResource(Res.string.grid_settings)

    @Composable fun showGrid() = stringResource(Res.string.show_grid)

    @Composable fun gridColor() = stringResource(Res.string.grid_color)

    @Composable fun gridSpacing() = stringResource(Res.string.grid_spacing)

    @Composable fun gridSpacingValue(spacing: Int) =
        stringResource(Res.string.grid_spacing_value, spacing)

    // Actions Sample
    @Composable fun actionsTitle() = stringResource(Res.string.actions_title)

    @Composable fun actionsDescription() = stringResource(Res.string.actions_description)

    @Composable fun lastAction(action: String) =
        stringResource(Res.string.last_action, action)

    @Composable fun availableActions() = stringResource(Res.string.available_actions)

    @Composable fun destructiveAction() = stringResource(Res.string.destructive_action)

    // Export
    @Composable fun exportTitle() = stringResource(Res.string.export_title)

    @Composable fun exportDescription() = stringResource(Res.string.export_description)

    @Composable fun exportSettings() = stringResource(Res.string.export_settings)

    @Composable fun exportSize() = stringResource(Res.string.export_size)

    @Composable fun sizeSmall() = stringResource(Res.string.size_small)

    @Composable fun sizeMedium() = stringResource(Res.string.size_medium)

    @Composable fun sizeLarge() = stringResource(Res.string.size_large)

    @Composable fun sizeSquare() = stringResource(Res.string.size_square)

    @Composable fun maintainAspectRatio() = stringResource(Res.string.maintain_aspect_ratio)

    @Composable fun exportedSignature() = stringResource(Res.string.exported_signature)

    @Composable fun dimensions(width: Int, height: Int) =
        stringResource(Res.string.dimensions, width, height)

    // Validation
    @Composable fun validationTitle() = stringResource(Res.string.validation_title)

    @Composable fun validationDescription() = stringResource(Res.string.validation_description)

    @Composable fun validationStatus() = stringResource(Res.string.validation_status)

    @Composable fun statusValid() = stringResource(Res.string.status_valid)

    @Composable fun statusInvalid() = stringResource(Res.string.status_invalid)

    @Composable fun validationRequirements(minPaths: Int, minLength: Int, minComplexity: Int) =
        stringResource(Res.string.validation_requirements, minPaths, minLength, minComplexity)

    @Composable fun signatureMetrics() = stringResource(Res.string.signature_metrics)

    @Composable fun totalLength() = stringResource(Res.string.total_length)

    @Composable fun complexityScore() = stringResource(Res.string.complexity_score)

    @Composable fun complexityProgress() = stringResource(Res.string.complexity_progress)

    @Composable fun signatureBounds() = stringResource(Res.string.signature_bounds)

    @Composable fun width() = stringResource(Res.string.width)

    @Composable fun height() = stringResource(Res.string.height)

    @Composable fun area() = stringResource(Res.string.area)

    @Composable fun center() = stringResource(Res.string.center)

    @Composable fun description() = stringResource(Res.string.description)

    @Composable fun requirement(req: String) =
        stringResource(Res.string.requirement, req)

    @Composable fun pixels(value: Int) =
        stringResource(Res.string.pixels, value)

    @Composable fun squarePixels(value: Int) =
        stringResource(Res.string.square_pixels, value)

    @Composable fun coordinates(x: Int, y: Int) =
        stringResource(Res.string.coordinates, x, y)

    // Dark Theme
    @Composable fun darkTitle() = stringResource(Res.string.dark_title)

    @Composable fun darkDescription() = stringResource(Res.string.dark_description)

    @Composable fun darkConfig() = stringResource(Res.string.dark_config)

    @Composable fun usage() = stringResource(Res.string.usage)

    @Composable fun usageCode() = stringResource(Res.string.usage_code)

    @Composable fun usageDescription() = stringResource(Res.string.usage_description)

    @Composable fun signatureInfo() = stringResource(Res.string.signature_info)

    @Composable fun paths() = stringResource(Res.string.paths)

    @Composable fun bitmapSize() = stringResource(Res.string.bitmap_size)

    @Composable fun white30Opacity() = stringResource(Res.string.white_30_opacity)

    // Features
    @Composable fun featureCrossPlatform() = stringResource(Res.string.feature_cross_platform)

    @Composable fun featureStrokeColor() = stringResource(Res.string.feature_stroke_color)

    @Composable fun featureBackground() = stringResource(Res.string.feature_background)

    @Composable fun featureGrid() = stringResource(Res.string.feature_grid)

    @Composable fun featureUndoRedo() = stringResource(Res.string.feature_undo_redo)

    @Composable fun featureExport() = stringResource(Res.string.feature_export)

    @Composable fun featureValidation() = stringResource(Res.string.feature_validation)

    @Composable fun featureDarkTheme() = stringResource(Res.string.feature_dark_theme)

    @Composable fun featureFullscreen() = stringResource(Res.string.feature_fullscreen)

    @Composable fun featurePersistence() = stringResource(Res.string.feature_persistence)

    // Preview
    @Composable fun preview() = stringResource(Res.string.preview)

    // Content Descriptions
    @Composable fun cdSignaturePreview() = stringResource(Res.string.cd_signature_preview)

    @Composable fun cdBackButton() = stringResource(Res.string.cd_back_button)

    @Composable fun cdSampleIcon() = stringResource(Res.string.cd_sample_icon)

    @Composable fun cdFeatureIcon() = stringResource(Res.string.cd_feature_icon)

    @Composable fun cdAppLogo() = stringResource(Res.string.cd_app_logo)
}
