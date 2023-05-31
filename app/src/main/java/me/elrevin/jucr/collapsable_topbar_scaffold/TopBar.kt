package me.elrevin.jucr.collapsable_topbar_scaffold

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * TopBar of the screen.
 * Collapsable top bar, whose size depends on the scroll of the bottom part of the screen.
 */
@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    state: TopBarState,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(state.getHeightInDp())
    ) {
        content()
    }
}

/**
 * Three possible variants of the top bar state value
 * EXPANDED - the top bar is expanded
 * COLLAPSED - the top bar is collapsed
 * ADJUSTABLE - height of the top bar is changing right now
 */
enum class TopBarValue {
    EXPANDED, COLLAPSED, ADJUSTABLE
}

/**
 * State of the top bar.
 *
 * @property expandedHeight - height of the top bar when it's expanded
 * @property collapsedHeight - height of the top bar when it's collapsed
 * @property density - current density, you can get it by LocalDensity.current expression
 * @property scope - coroutine scope for an animation
 */
class TopBarState(
    val expandedHeight: Dp,
    val collapsedHeight: Dp,
    private val density: Density,
    private val scope: CoroutineScope
) {
    /**
     * Extreme heights of the top bar in px
     */
    private val expandedHeightPx = with(density) {expandedHeight.toPx()}
    private val collapsedHeightPx = with(density) {collapsedHeight.toPx()}

    /**
     * Current value of the top bar state
     */
    val value = mutableStateOf(TopBarValue.EXPANDED)

    /**
     * Animatable value of height of the top bar
     */
    val height = Animatable(
        with(density) {
            if (value.value == TopBarValue.EXPANDED) expandedHeight.toPx() else collapsedHeight.toPx()
        }
    )

    /**
     * Height of the top bar in dp
     *
     * @return the height in Dp
     */
    fun getHeightInDp(): Dp = with(density) { height.value.toDp() }

    /**
     * Set height of the top bar. If the animate property is true, the height changes slowly with
     * animation.
     *
     * @param newHeight - new height of top bar in px
     * @param animate - the animation is needed
     */
    private fun setHeight(newHeight: Float, animate: Boolean) {

        // Height of the top bar can't be less than collapsedHeightPx and greater than expandedHeightPx
        val heightPx = newHeight.coerceIn(collapsedHeightPx, expandedHeightPx)

        // Set the state value depending on the height of the top bar
        value.value = when (heightPx) {
            expandedHeightPx -> TopBarValue.EXPANDED
            collapsedHeightPx -> TopBarValue.COLLAPSED
            else -> TopBarValue.ADJUSTABLE
        }

        if (animate) {
            // Run the animation if it's needed
            scope.launch {
                height.animateTo(heightPx)
            }
        } else {
            // Just changing the height
            scope.launch {
                height.snapTo(heightPx)
            }
        }
    }

    /**
     * Change the top bar height by delta param value in px
     * @param delta - value for changing the height
     */
    fun resizeBy(delta: Float) {
        // Changing the height without animation
        setHeight(height.value + delta, false)
    }

    /**
     * Expand the top bar
     */
    fun expand() {
        value.value = TopBarValue.EXPANDED

        // Changing the height with animation
        setHeight(expandedHeightPx, true)
    }

    /**
     * Collapse the top bar
     */
    fun collapse() {
        value.value = TopBarValue.COLLAPSED

        // Changing the height with animation
        setHeight(collapsedHeightPx, true)
    }
}

/**
 * Useful function for remember the top bar state
 * @param expandedHeight - height of the top bar when it's expanded, in Dp
 * @param collapsedHeight - height of the top bar when it's collapsed, in Dp
 *
 * @return the top bar state
 */
@Composable
fun rememberTopBarState(
    expandedHeight: Dp,
    collapsedHeight: Dp,
): TopBarState {
    // get the current density
    val density = LocalDensity.current

    // get a coroutine scope
    val scope = rememberCoroutineScope()

    // remember the state
    val state = remember {
        TopBarState(expandedHeight, collapsedHeight, density, scope)
    }

    return state
}