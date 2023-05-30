package me.elrevin.jucr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.elrevin.jucr.collapsable_topbar_scaffold.CollapsableTopBarScaffold
import me.elrevin.jucr.collapsable_topbar_scaffold.rememberTopBarState

/**
 * Main screen of the application, with CollapsableTopbarScaffold which
 *
 */
@Composable
fun MainScreen() {
    val topBarState = rememberTopBarState(
        expandedHeight = 300.dp + 38.dp,
        collapsedHeight = 132.dp + 38.dp
    )

    val listState = rememberLazyListState()

    CollapsableTopBarScaffold(
        modifier = Modifier.fillMaxSize(),
        topBarState = topBarState,
        topBarContent = { TopBarContent() },
        listState = listState,
        bottomBarContent = { BottomBarContent() }
    ) {
        Statistics()
        SuperchargesList(listState)
    }
}

