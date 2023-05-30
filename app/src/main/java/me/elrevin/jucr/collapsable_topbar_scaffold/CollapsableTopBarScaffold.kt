package me.elrevin.jucr.collapsable_topbar_scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp

/**
 * Scaffold with collapsable top bar, nested scroll and reaction on lazy list (has to be in content)
 * scrolling.
 *
 * @param modifier The modifier to be applied
 * @param topBarState State of the top bar
 * @param topBarContent Content to be placed on top bar
 * @param bottomBarContent Content to be placed on bottom bar
 * @param listState State of the lazy list
 * @param content Content to be placed between the top and bottom bars
 */
@Composable
fun CollapsableTopBarScaffold(
    modifier: Modifier = Modifier,
    indentWidth: Dp = 142.dp,
    indentHeight: Dp = 38.dp,
    topBarState: TopBarState,
    topBarContent: (@Composable () -> Unit)? = null,
    bottomBarContent: (@Composable () -> Unit)? = null,
    listState: LazyListState,
    content: @Composable ColumnScope.() -> Unit
) {
    // scrolling distance in px
    var delta by remember {
        mutableStateOf(0f)
    }

    // list scrolling
    var wasListScrolled by remember {
        mutableStateOf(false)
    }

    var fling by remember {
        mutableStateOf(false)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                fling = false
                delta = available.y
                wasListScrolled = listState.isScrollInProgress
                if (delta < 0f || listState.firstVisibleItemIndex == 0 || !wasListScrolled) {
                    topBarState.resizeBy(delta)
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                fling = true
                return super.onPreFling(available)
            }
        }
    }

    LaunchedEffect(key1 = fling, key2 = wasListScrolled, key3 = isFirstListItemVisible(listState)) {
        if (fling) {
            if (delta < 0f) {
                topBarState.collapse()
            } else {
                if (listState.firstVisibleItemIndex == 0 || !wasListScrolled) {
                    topBarState.expand()
                }
            }
        }
    }

    Box(
        modifier
            .fillMaxSize()
            // attach as a parent to the nested scroll system
            .nestedScroll(nestedScrollConnection)
    ) {
        TopBar(
            state = topBarState,
            modifier = Modifier
                .fillMaxWidth(),
            content = topBarContent ?: {
                Text(text = "Delta = $delta")
            }
        )

        Box(
            Modifier
                .padding(top = topBarState.getHeightInDp() - 38.dp)
                .fillMaxSize()
                .clip(IndentShape(indentWidth, indentHeight))
                .background(Color.White),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                Modifier
                    .padding(bottom = if (bottomBarContent != null) 56.dp else 0.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                content = content
            )

            if (bottomBarContent !== null) {
                BottomBar(content = bottomBarContent)
            }
        }

    }
}

fun isFirstListItemVisible(state: LazyListState) = state.firstVisibleItemIndex == 0

class IndentShape(
    private val indentWidth: Dp,
    private val indentHeight: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline =
        Outline.Generic(
            Path().apply {
                val width = size.width
                val height = size.height
                val densityPx = density.density
                val cR = 12 * densityPx
                val indentHeightPx: Float = indentHeight.value * densityPx
                val indentWidthPx: Float = indentWidth.value * densityPx

                val indentStart = (width - indentWidthPx) / 2
                val indentEnd = (width + indentWidthPx) / 2

                val indentD1 = 30 * densityPx
                val indentX3 = indentStart + indentWidthPx / 2

                moveTo(cR, 0f)
                lineTo(indentStart, 0f)
                cubicTo(
                    indentStart + indentD1, 0f,
                    indentStart + indentD1, indentHeightPx,
                    indentX3, indentHeightPx
                )
                cubicTo(
                    indentEnd - indentD1, indentHeightPx,
                    indentEnd - indentD1, 0f,
                    indentEnd, 0f
                )
                lineTo(width - cR, 0f)
                arcTo(
                    rect = Rect(
                        offset = Offset(
                            width - cR,
                            0f
                        ),
                        size = Size(cR, cR)
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(width, height)
                lineTo(0f, height)
                lineTo(0f, cR)
                arcTo(
                    rect = Rect(
                        offset = Offset(
                            0f,
                            0f
                        ),
                        size = Size(cR, cR)
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                close()
            }
        )
}