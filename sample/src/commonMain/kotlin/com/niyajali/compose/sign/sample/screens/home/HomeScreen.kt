package com.niyajali.compose.sign.sample.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.sample.SampleScreen
import com.niyajali.compose.sign.sample.components.animations.AnimationDelays
import com.niyajali.compose.sign.sample.components.animations.IconAnimation
import com.niyajali.compose.sign.sample.components.cards.SampleCard
import com.niyajali.compose.sign.sample.components.icons.AnimatedIcon
import com.niyajali.compose.sign.sample.components.icons.IconMapper
import com.niyajali.compose.sign.sample.components.layout.SectionHeader
import com.niyajali.compose.sign.sample.theme.Gradients
import com.niyajali.compose.sign.sample.theme.Size
import com.niyajali.compose.sign.sample.theme.Spacing
import com.niyajali.compose.sign.sample.utils.Strings
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

/**
 * Modern redesigned home screen with gradient backgrounds,
 * grid layout, icons, and animations
 *
 * Features:
 * - Hero section with app logo and gradient
 * - Responsive 2-column grid of sample cards
 * - Each card has an icon, gradient, and description
 * - Feature list with check icons
 * - Staggered entry animations
 */
@Composable
fun HomeScreen(
    onNavigate: (SampleScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600),
        label = "home_fade_in"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        // Sample Cards Grid
        SampleCardsSection(onNavigate = onNavigate)
    }
}

/**
 * Hero section with gradient background, logo, and welcome text
 */
@Composable
private fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gradients.primaryDiagonal)
            .padding(Spacing.xl),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            // App Logo
            AnimatedIcon(
                icon = IconMapper.Icons.appLogo,
                contentDescription = Strings.cdAppLogo(),
                size = Size.iconXXL,
                tint = MaterialTheme.colorScheme.onPrimary,
                animationType = IconAnimation.Bounce
            )

            // Welcome Title
            Text(
                text = Strings.appWelcomeTitle(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            // Subtitle
            Text(
                text = Strings.appWelcomeSubtitle(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                modifier = Modifier.padding(horizontal = Spacing.md)
            )
        }
    }
}

/**
 * Grid section with sample cards
 */
@Composable
private fun SampleCardsSection(onNavigate: (SampleScreen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Sample cards in grid
        val samples = getSampleScreens()

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            items(samples) { sampleData ->
                var cardVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(sampleData.delay)
                    cardVisible = true
                }

                val cardAlpha by animateFloatAsState(
                    targetValue = if (cardVisible) 1f else 0f,
                    animationSpec = tween(400),
                    label = "card_fade"
                )

                Box(modifier = Modifier.alpha(cardAlpha)) {
                    SampleCard(
                        title = sampleData.title,
                        description = sampleData.description,
                        icon = IconMapper.getScreenIcon(sampleData.screen),
                        gradient = IconMapper.getScreenGradientSubtle(sampleData.screen),
                        onClick = { onNavigate(sampleData.screen) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        // Features Section
        FeaturesSection()

        // Bottom padding
        Spacer(modifier = Modifier.height(Spacing.xl))
    }
}

/**
 * Features section with icons
 */
@Composable
private fun FeaturesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.md)
    ) {
        // Section Header with icon
        SectionHeader(
            title = Strings.libraryFeatures(),
            icon = IconMapper.Icons.star
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        // Feature list
        val features = getFeatureList()

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            features.forEachIndexed { index, feature ->
                var featureVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(index * AnimationDelays.STAGGER_SHORT)
                    featureVisible = true
                }

                val featureAlpha by animateFloatAsState(
                    targetValue = if (featureVisible) 1f else 0f,
                    animationSpec = tween(300),
                    label = "feature_fade"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(featureAlpha),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(IconMapper.Icons.check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * Data class for sample screen info
 */
private data class SampleData(
    val screen: SampleScreen,
    val title: String,
    val description: String,
    val delay: Long = 0L
)

/**
 * Get list of sample screens with data
 */
@Composable
private fun getSampleScreens(): List<SampleData> {
    return listOf(
        SampleData(
            screen = SampleScreen.BASIC,
            title = Strings.sampleBasic(),
            description = Strings.sampleBasicDesc(),
            delay = 0L
        ),
        SampleData(
            screen = SampleScreen.CUSTOM_STYLE,
            title = Strings.sampleCustomStyle(),
            description = Strings.sampleCustomStyleDesc(),
            delay = AnimationDelays.STAGGER_SHORT
        ),
        SampleData(
            screen = SampleScreen.WITH_GRID,
            title = Strings.sampleGrid(),
            description = Strings.sampleGridDesc(),
            delay = AnimationDelays.STAGGER_SHORT * 2
        ),
        SampleData(
            screen = SampleScreen.WITH_ACTIONS,
            title = Strings.sampleActions(),
            description = Strings.sampleActionsDesc(),
            delay = AnimationDelays.STAGGER_SHORT * 3
        ),
        SampleData(
            screen = SampleScreen.EXPORT,
            title = Strings.sampleExport(),
            description = Strings.sampleExportDesc(),
            delay = AnimationDelays.STAGGER_SHORT * 4
        ),
        SampleData(
            screen = SampleScreen.VALIDATION,
            title = Strings.sampleValidation(),
            description = Strings.sampleValidationDesc(),
            delay = AnimationDelays.STAGGER_SHORT * 5
        ),
        SampleData(
            screen = SampleScreen.DARK_THEME,
            title = Strings.sampleDark(),
            description = Strings.sampleDarkDesc(),
            delay = AnimationDelays.STAGGER_SHORT * 6
        ),
        SampleData(
            screen = SampleScreen.FULLSCREEN,
            title = Strings.sampleFullscreen(),
            description = Strings.sampleFullscreenDesc(),
            delay = AnimationDelays.STAGGER_SHORT * 7
        )
    )
}

/**
 * Get list of features
 */
@Composable
private fun getFeatureList(): List<String> {
    return listOf(
        Strings.featureCrossPlatform(),
        Strings.featureStrokeColor(),
        Strings.featureBackground(),
        Strings.featureGrid(),
        Strings.featureUndoRedo(),
        Strings.featureExport(),
        Strings.featureValidation(),
        Strings.featureDarkTheme(),
        Strings.featureFullscreen(),
        Strings.featurePersistence()
    )
}
