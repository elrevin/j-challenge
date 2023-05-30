package me.elrevin.jucr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.SuperchargesList(
    listState: LazyListState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f), state = listState
    ) {
        items(50) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 26.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(82.dp)
                    .background(
                        if (it % 2 == 0) Color.Magenta
                        else Color.Yellow
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Text(text = "Item $it")
            }
        }

    }
}

@Composable
private fun SuperchargesListHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 22.dp)
            .height(36.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Nearby Supercharges")
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "View all")
        }
    }
}