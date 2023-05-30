package me.elrevin.jucr.collapsable_topbar_scaffold

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Topbar(
    modifier: Modifier = Modifier,
    state: TopBarState,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
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
    val overHeight: Dp,
    private val density: Density,
    private val scope: CoroutineScope
) {
    private val expandedHeightPx = with(density) {expandedHeight.toPx()}
    private val collapsedHeightPx = with(density) {collapsedHeight.toPx()}

    private val value = mutableStateOf(TopbarValue.EXPANDED)

    val height = Animatable(
        with(density) {
            if (value.value == TopbarValue.EXPANDED) expandedHeight.toPx() else collapsedHeight.toPx()
        }
    )

    fun getHeightInDp(): Dp = with(density) { height.value.toDp() }

    private fun setHeight(newHeight: Float, animate: Boolean) {
        val heightPx = newHeight.coerceIn(collapsedHeightPx, expandedHeightPx)
        value.value = when (heightPx) {
            expandedHeightPx -> TopbarValue.EXPANDED
            collapsedHeightPx -> TopbarValue.COLLAPSED
            else -> TopbarValue.ADJUSTABLE
        }

        if (animate) {
            scope.launch {
                height.animateTo(heightPx)
            }
        } else {
            scope.launch {
                height.snapTo(heightPx)
            }
        }
    }

    fun resizeBy(delta: Float) {
        setHeight(height.value + delta, false)
    }

    fun expand() {
        value.value = TopbarValue.EXPANDED
        setHeight(expandedHeightPx, true)
    }

    fun collapse() {
        value.value = TopbarValue.COLLAPSED
        setHeight(collapsedHeightPx, true)
    }
}

@Composable
fun rememberTopbarState(
    expandedHeight: Dp,
    collapsedHeight: Dp,
    overHeight: Dp = 38.dp
): TopBarState {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val state = remember {
        TopBarState(expandedHeight, collapsedHeight, overHeight, density, scope)
    }

    return state
}