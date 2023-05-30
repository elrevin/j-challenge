package me.elrevin.jucr.collapsable_topbar_scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
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

@Composable
fun CollapsableTopbarScaffold(
    modifier: Modifier = Modifier,
    topbarState: TopBarState,
    topbarContent: (@Composable () -> Unit)? = null,
    listTitle: (@Composable () -> Unit)? = null,
    listContent: LazyListScope.() -> Unit,
    statistics: (@Composable () -> Unit)? = null,
    bottombarExpandedHeight: Dp = 56.dp,
    bottombar: (@Composable () -> Unit)? = null
) {
    var delta by remember {
        mutableStateOf(0f)
    }
    var wasListScrolled by remember {
        mutableStateOf(false)
    }

    var fling by remember {
        mutableStateOf(false)
    }

    val listState = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                fling = false
                delta = available.y
                wasListScrolled = listState.isScrollInProgress
                if (delta < 0f || listState.firstVisibleItemIndex == 0 || !wasListScrolled) {
                    topbarState.resizeBy(delta)
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
                topbarState.collapse()
            } else {
                if (listState.firstVisibleItemIndex == 0 || !wasListScrolled) {
                    topbarState.expand()
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
        Topbar(
            state = topbarState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red),
            content = topbarContent ?: {
                Text(text = "Delta = $delta")
            }
        )

        Box(
            Modifier
                .padding(top = topbarState.getHeightInDp())
                .fillMaxSize()
                .clip(IndentShape())
                .background(Color.White),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                Modifier
                    .padding(bottom = bottombarExpandedHeight)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (statistics != null) {
                    statistics()
                }
                if (listTitle != null) {
                    listTitle()
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = listState,
                    content = listContent
                )
            }

            if (bottombar !== null) {
                val bottomBarShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bottombarExpandedHeight)
                        .clip(bottomBarShape)
                ) {
                    bottombar()
                }
            }
        }

    }
}

fun isFirstListItemVisible(state: LazyListState) = state.firstVisibleItemIndex == 0

class IndentShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline =
        Outline.Generic(
            Path().apply {
                val w = size.width
                val h = size.height
                val d = density.density
                val cR = 12 * d
                val indentHeight = 38 * d
                val indentWidth = 142 * d

                val indentStart = (w - indentWidth) / 2 - cR
                val indentEnd = (w + indentWidth) / 2 - cR

                val indentD1 = 30 * d
                val indentX3 = indentStart + indentWidth / 2

                moveTo(cR, 0f)
                lineTo(indentStart, 0f)
                cubicTo(
                    indentStart + indentD1, 0f,
                    indentStart + indentD1, indentHeight,
                    indentX3, indentHeight
                )
                cubicTo(
                    indentEnd - indentD1, indentHeight,
                    indentEnd - indentD1, 0f,
                    indentEnd, 0f
                )
                lineTo(w - cR, 0f)
                arcTo(
                    rect = Rect(
                        offset = Offset(
                            w - cR,
                            0f
                        ),
                        size = Size(cR, cR)
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(w, h)
                lineTo(0f, h)
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