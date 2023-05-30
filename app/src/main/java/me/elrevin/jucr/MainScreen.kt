package me.elrevin.jucr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
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
    val topbarState = rememberTopBarState(
        expandedHeight = 280.dp + 38.dp,
        collapsedHeight = 132.dp + 38.dp
    )

    val listState = rememberLazyListState()

    CollapsableTopBarScaffold(
        modifier = Modifier.fillMaxSize(),
        topBarState = topbarState,
        topBarContent = { TopBarContent() },
        listState = listState,
        bottomBarContent = { BottomBarContent() }
    ) {
        Statistics()
        SuperchargesList(listState)
    }
}

