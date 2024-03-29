package me.elrevin.jucr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.elrevin.jucr.ui.theme.Gray
import me.elrevin.jucr.ui.theme.Green
import me.elrevin.jucr.ui.theme.Red
import me.elrevin.jucr.ui.theme.Yellow

/**
 * Statistics bar on the screen
 */
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
                value = "240 Volts",
                hint = "Voltage"
            )
        }
        item {
            StatisticsItem(
                iconColor = Green,
                iconDescription = "Car battery remaining charge",
                icon = R.drawable.ic_battery_quarter,
                value = "540 Km",
                hint = "Remaining charge"
            )
        }
        item {
            StatisticsItem(
                iconColor = Yellow,
                iconDescription = "Fast charge",
                icon = R.drawable.ic_plug,
                value = "20 Min",
                hint = "Fast charge"
            )
        }
    }
}

/**
 * Title for statistics bar
 */
@Composable
private fun StatisticsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 26.dp, bottom = 10.dp, start = 26.dp)
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        // We can't use IconButton because it doesn't give change color of indication
        Box(
            modifier = Modifier
                .size(24.dp, 48.dp)
                .clickable(
                    onClick = { },
                    enabled = true,
                    role = Role.Button,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = false,
                        radius = 20.dp,
                        color = Gray
                    )
                ), contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_ellipsis),
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

/**
 * Item of statistics list
 *
 * @param iconColor color of the icon and it's background
 * @param iconDescription description text for the icon
 * @param icon
 * @param value Value of statistics item
 * @param hint Explanation of statistics item
 */
@Composable
fun StatisticsItem(
    iconColor: Color,
    iconDescription: String,
    icon: Int,
    value: String,
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

        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    lineHeight = 10.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}