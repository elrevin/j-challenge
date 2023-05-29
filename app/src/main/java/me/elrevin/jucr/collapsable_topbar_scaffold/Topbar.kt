package me.elrevin.jucr.collapsable_topbar_scaffold

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Composable
fun Topbar(
    modifier: Modifier = Modifier,
    state: TopBarState,
    content: @Composable () -> Unit
) {
    val height by animateDpAsState(targetValue = state.height.value)
    Box(
        modifier = modifier
            .height(height)
    ) {
        content()
    }
}

enum class TopbarValue {
    EXPANDED, COLLAPSED, ADJUSTABLE
}

class TopBarState(
    val expandedHeight: Dp,
    val collapsedHeight: Dp,
    private val density: Density
) {
    private val expandedHeightPx = with(density) {expandedHeight.toPx()}
    private val collapsedHeightPx = with(density) {collapsedHeight.toPx()}

    private val value = mutableStateOf(TopbarValue.EXPANDED)

    private var heightPx: Float = with(density) {
        if (value.value == TopbarValue.EXPANDED) expandedHeight.toPx() else collapsedHeight.toPx()
    }

    val height = mutableStateOf(with(density) {heightPx.toDp()})

    private fun setHeight(newHeight: Float) {
        heightPx = newHeight
        height.value = with(density) { heightPx.toDp() }
        heightPx = heightPx.coerceIn(collapsedHeightPx, expandedHeightPx)
    }

    fun resizeBy(delta: Float) {
        setHeight(heightPx + delta)

        value.value = when (heightPx) {
            expandedHeightPx -> TopbarValue.EXPANDED
            collapsedHeightPx -> TopbarValue.COLLAPSED
            else -> TopbarValue.ADJUSTABLE
        }
    }

    fun expand() {
        value.value = TopbarValue.EXPANDED
        setHeight(expandedHeightPx)
    }

    fun collapse() {
        value.value = TopbarValue.COLLAPSED
        setHeight(collapsedHeightPx)
    }
}

@Composable
fun rememberTopbarState(
    expandedHeight: Dp,
    collapsedHeight: Dp
): TopBarState {
    val density = LocalDensity.current
    val state = remember {
        TopBarState(expandedHeight, collapsedHeight, density)
    }

    return state
}