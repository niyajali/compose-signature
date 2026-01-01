package com.niyajali.compose.sign.sample.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.niyajali.compose.sign.sample.theme.Spacing

/**
 * Responsive grid layout that adapts to screen size
 *
 * Breakpoints:
 * - Phone: 2 columns (< 600dp)
 * - Tablet/Desktop: 3 columns (≥ 600dp)
 *
 * @param modifier Modifier for the grid
 * @param minColumnWidth Minimum width for each column (determines number of columns)
 * @param spacing Spacing between grid items
 * @param contentPadding Padding around the entire grid
 * @param content Grid content
 */
@Composable
fun ResponsiveGrid(
    modifier: Modifier = Modifier,
    minColumnWidth: Dp = 160.dp,
    spacing: Dp = Spacing.md,
    contentPadding: PaddingValues = PaddingValues(Spacing.md),
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minColumnWidth),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        content()
    }
}

/**
 * Fixed 2-column grid layout
 *
 * @param modifier Modifier for the grid
 * @param spacing Spacing between grid items
 * @param contentPadding Padding around the entire grid
 * @param content Grid content
 */
@Composable
fun TwoColumnGrid(
    modifier: Modifier = Modifier,
    spacing: Dp = Spacing.md,
    contentPadding: PaddingValues = PaddingValues(Spacing.md),
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        content()
    }
}

/**
 * Fixed 3-column grid layout
 *
 * @param modifier Modifier for the grid
 * @param spacing Spacing between grid items
 * @param contentPadding Padding around the entire grid
 * @param content Grid content
 */
@Composable
fun ThreeColumnGrid(
    modifier: Modifier = Modifier,
    spacing: Dp = Spacing.md,
    contentPadding: PaddingValues = PaddingValues(Spacing.md),
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        content()
    }
}
