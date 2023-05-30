package me.elrevin.jucr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Statistics() {
    StatisticsHeader()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),

        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 26.dp)
    ) {
        items(3) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Green)
                    .padding(22.dp)
            )
        }
    }
}

@Composable
private fun StatisticsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 26.dp, bottom = 10.dp, start = 26.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .background(Color.Green),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Statistics"
            )
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(Color.Cyan)) {
            Image(
                painter = painterResource(id = R.drawable.ic_ellipsis),
                contentDescription = "More options"
            )
        }
    }
}