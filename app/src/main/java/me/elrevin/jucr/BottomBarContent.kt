package me.elrevin.jucr

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource

@Composable
fun BottomBarContent () {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_car),
                colorFilter = ColorFilter.tint(Color(0xFFEA5261)),
                contentDescription = "Your car"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                colorFilter = ColorFilter.tint(Color(0xFF8F9CA3)),
                contentDescription = "Search"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_map),
                colorFilter = ColorFilter.tint(Color(0xFF8F9CA3)),
                contentDescription = "Map of charges"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                colorFilter = ColorFilter.tint(Color(0xFF8F9CA3)),
                contentDescription = "Profile"
            )
        }
    }
}