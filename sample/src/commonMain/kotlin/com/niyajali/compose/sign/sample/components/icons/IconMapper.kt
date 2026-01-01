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
package com.niyajali.compose.sign.sample.components.icons

import androidx.compose.ui.graphics.Brush
import com.niyajali.compose.sign.sample.SampleScreen
import com.niyajali.compose.sign.sample.theme.Gradients
import compose_signature.sample.generated.resources.Res
import compose_signature.sample.generated.resources.ic_app_logo
import compose_signature.sample.generated.resources.ic_arrow_back
import compose_signature.sample.generated.resources.ic_block
import compose_signature.sample.generated.resources.ic_check
import compose_signature.sample.generated.resources.ic_chevron_right
import compose_signature.sample.generated.resources.ic_clear
import compose_signature.sample.generated.resources.ic_dark_mode
import compose_signature.sample.generated.resources.ic_edit
import compose_signature.sample.generated.resources.ic_export
import compose_signature.sample.generated.resources.ic_fullscreen
import compose_signature.sample.generated.resources.ic_grid
import compose_signature.sample.generated.resources.ic_palette
import compose_signature.sample.generated.resources.ic_redo
import compose_signature.sample.generated.resources.ic_save
import compose_signature.sample.generated.resources.ic_star
import compose_signature.sample.generated.resources.ic_touch
import compose_signature.sample.generated.resources.ic_undo
import compose_signature.sample.generated.resources.ic_validation
import org.jetbrains.compose.resources.DrawableResource

/**
 * Maps sample screens and actions to their associated icons and gradients
 */
object IconMapper {
    /**
     * Get the icon for a specific sample screen
     */
    fun getScreenIcon(screen: SampleScreen): DrawableResource {
        return when (screen) {
            SampleScreen.HOME -> Res.drawable.ic_app_logo
            SampleScreen.BASIC -> Res.drawable.ic_edit
            SampleScreen.CUSTOM_STYLE -> Res.drawable.ic_palette
            SampleScreen.WITH_GRID -> Res.drawable.ic_grid
            SampleScreen.WITH_ACTIONS -> Res.drawable.ic_touch
            SampleScreen.EXPORT -> Res.drawable.ic_export
            SampleScreen.VALIDATION -> Res.drawable.ic_validation
            SampleScreen.DARK_THEME -> Res.drawable.ic_dark_mode
            SampleScreen.FULLSCREEN -> Res.drawable.ic_fullscreen
        }
    }

    /**
     * Get the gradient for a specific sample screen
     */
    fun getScreenGradient(screen: SampleScreen): Brush {
        return when (screen) {
            SampleScreen.HOME -> Gradients.primaryDiagonal
            SampleScreen.BASIC -> Gradients.Sample.basic
            SampleScreen.CUSTOM_STYLE -> Gradients.Sample.customStyle
            SampleScreen.WITH_GRID -> Gradients.Sample.grid
            SampleScreen.WITH_ACTIONS -> Gradients.Sample.actions
            SampleScreen.EXPORT -> Gradients.Sample.export
            SampleScreen.VALIDATION -> Gradients.Sample.validation
            SampleScreen.DARK_THEME -> Gradients.Sample.darkTheme
            SampleScreen.FULLSCREEN -> Gradients.Sample.fullscreen
        }
    }

    /**
     * Get the subtle (background) gradient for a specific sample screen
     */
    fun getScreenGradientSubtle(screen: SampleScreen): Brush {
        return when (screen) {
            SampleScreen.HOME -> Gradients.backgroundMesh
            SampleScreen.BASIC -> Gradients.Sample.basicSubtle
            SampleScreen.CUSTOM_STYLE -> Gradients.Sample.customStyleSubtle
            SampleScreen.WITH_GRID -> Gradients.Sample.gridSubtle
            SampleScreen.WITH_ACTIONS -> Gradients.Sample.actionsSubtle
            SampleScreen.EXPORT -> Gradients.Sample.exportSubtle
            SampleScreen.VALIDATION -> Gradients.Sample.validationSubtle
            SampleScreen.DARK_THEME -> Gradients.Sample.darkThemeSubtle
            SampleScreen.FULLSCREEN -> Gradients.Sample.fullscreenSubtle
        }
    }

    /**
     * Navigation and UI icons
     */
    object Icons {
        val arrowBack = Res.drawable.ic_arrow_back
        val chevronRight = Res.drawable.ic_chevron_right
        val appLogo = Res.drawable.ic_app_logo
        val star = Res.drawable.ic_star
        val check = Res.drawable.ic_check
        val block = Res.drawable.ic_block
    }

    /**
     * Action icons
     */
    object Actions {
        val clear = Res.drawable.ic_clear
        val undo = Res.drawable.ic_undo
        val redo = Res.drawable.ic_redo
        val save = Res.drawable.ic_save
    }
}
