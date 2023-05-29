package me.elrevin.jucr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.elrevin.jucr.collapsable_topbar_scaffold.CollapsableTopbarScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_car),
                        contentDescription = "Your car"
                    )
                }
                Button(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search"
                    )
                }
                Button(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_map),
                        contentDescription = "Map of charges"
                    )
                }
                Button(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Profile"
                    )
                }
            }
        }
    ) { padding ->
        CollapsableTopbarScaffold(
            modifier = Modifier.padding(padding),
            statisticsRow = {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(3) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Green)
                                .padding(4.dp)
                        )
                    }
                }
            },
            listContent = {
                items(50) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (it % 2 == 0) Color.Magenta
                                else Color.Yellow
                            )
                            .padding(8.dp)
                    ) {
                        Text(text = "Item $it")
                    }
                }
            }
        )

    }
}