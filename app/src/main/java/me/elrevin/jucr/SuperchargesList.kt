package me.elrevin.jucr

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.elrevin.jucr.ui.theme.Gray
import me.elrevin.jucr.ui.theme.Green

@Composable
fun ColumnScope.SuperchargesList(
    listState: LazyListState
) {
    SuperchargesListHeader()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 26.dp, end = 26.dp, bottom = 16.dp)
    ) {
        items(50) {
            ListItem(
                name = "Name of charge #$it",
                availableCount = 4,
                totalCount = 20,
                distance = 28.9f,
                status = if (it % 3 == 0) ListItemStatus.FAVORITE else ListItemStatus.NORMAL
            )
        }

    }
}

@Composable
private fun SuperchargesListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 26.dp, end = 26.dp, top = 24.dp, bottom = 16.dp)
            .height(36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Nearby Sapercharges",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Box(
            modifier = Modifier
                .height(48.dp)
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
            Text(
                text = "View All",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

enum class ListItemStatus {
    NORMAL, FAVORITE
}

@Composable
fun ListItem(
    name: String,
    availableCount: Int,
    totalCount: Int,
    distance: Float,
    status: ListItemStatus
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.onBackground.copy(0.05f)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .height(46.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Clip
            )

            Text(
                text = "$availableCount/$totalCount available",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Clip
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(
                    id = if (status == ListItemStatus.NORMAL)
                        R.drawable.ic_location_pin_bolt
                    else
                        R.drawable.ic_location_pin_heart
                ),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = if (status == ListItemStatus.NORMAL)
                    ""
                else
                    "Favorite"
            )
            Text(
                text = "$distance km",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}