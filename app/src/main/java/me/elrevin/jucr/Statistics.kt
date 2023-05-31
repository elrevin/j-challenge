package me.elrevin.jucr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.elrevin.jucr.ui.theme.Green
import me.elrevin.jucr.ui.theme.Red
import me.elrevin.jucr.ui.theme.Yellow

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
        item {
            StatisticsItem(
                iconColor = Red,
                iconDescription = "Car battery voltage",
                icon = R.drawable.ic_car_battery,
                label = "240 Volt",
                hint = "Voltage"
            )
        }
        item {
            StatisticsItem(
                iconColor = Green,
                iconDescription = "Car battery remaining charge",
                icon = R.drawable.ic_battery_quarter,
                label = "540 Km",
                hint = "Remaining charge"
            )
        }
        item {
            StatisticsItem(
                iconColor = Yellow,
                iconDescription = "Fast charge",
                icon = R.drawable.ic_plug,
                label = "20 Min",
                hint = "Fast charge"
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
            modifier = Modifier.height(48.dp), contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_ellipsis),
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun StatisticsItem(
    iconColor: Color,
    iconDescription: String,
    icon: Int,
    label: String,
    hint: String
) {
    Column(
        modifier = Modifier
            .size(140.dp)
            .background(Color.White)
            .border(
                border = BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.onBackground.copy(0.05f)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(22.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconColor.copy(0.1f), RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                tint = iconColor,
                contentDescription = iconDescription
            )
        }

        Column() {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, lineHeight = 10.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}