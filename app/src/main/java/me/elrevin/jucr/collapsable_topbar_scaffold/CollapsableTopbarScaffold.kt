package me.elrevin.jucr.collapsable_topbar_scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp

@Composable
fun CollapsableTopbarScaffold(
    modifier: Modifier = Modifier,
    expandedTopbarHeight: Dp = 200.dp,
    collapsedTopbarHeight: Dp = 40.dp,
    topbarContent: (@Composable () -> Unit)? = null,
    listContent: LazyListScope.() -> Unit,
    statisticsRow: (@Composable () -> Unit)? = null
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

    val topbarState = rememberTopbarState(
        expandedHeight = expandedTopbarHeight,
        collapsedHeight = collapsedTopbarHeight
    )

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

    Column(
        modifier
            .fillMaxSize()
            // attach as a parent to the nested scroll system
            .nestedScroll(nestedScrollConnection)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (statisticsRow != null) {
                    statisticsRow()
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = listState,
                    content = listContent
                )
            }
        }
    }
}

fun isFirstListItemVisible(state: LazyListState) = state.firstVisibleItemIndex == 0