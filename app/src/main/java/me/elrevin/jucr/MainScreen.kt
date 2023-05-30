package me.elrevin.jucr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.elrevin.jucr.collapsable_topbar_scaffold.CollapsableTopbarScaffold
import me.elrevin.jucr.collapsable_topbar_scaffold.rememberTopbarState

/**
 * Main screen of the application, with CollapsableTopbarScaffold which
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val topbarState = rememberTopbarState(
        expandedHeight = 336.dp,
        collapsedHeight = 132.dp
    )
    CollapsableTopbarScaffold(
        modifier = Modifier.fillMaxSize(),
        topbarState = topbarState,
        topbarContent = {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(topbarState.getHeightInDp())
            ) {
                ConcentricCircles(maxWidth.value.toInt())
                Text(text = "$maxHeight , $maxWidth")
            }
        },
        statistics = {
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
        },
        listTitle = {
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
        },
        listContent = {
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
        },
        bottombar = {
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
    )
}

@Composable
fun ConcentricCircles(widht: Int) {
    val circleColor = Color.White
    val circleSizes = listOf(
        (widht / 2 + 5).dp,
        (widht / 2 - 10).dp,
        (widht / 2 - 25).dp,
        (widht / 2 - 40).dp
    )
    val circleSizesPx = with(LocalDensity.current) {
        circleSizes.map {
            it.toPx()
        }
    }
    val dotSize = 1.dp
    val dotCount = 90

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)

        val centres = listOf<Offset>(
            center,
            Offset(center.x, center.y + circleSizesPx[0] - circleSizesPx[1]),
            Offset(center.x, center.y + circleSizesPx[0] - circleSizesPx[2]),
            Offset(center.x, center.y + circleSizesPx[0] - circleSizesPx[3])
        )

        val points = mutableListOf<Offset>()
        circleSizes.forEachIndexed { index, radius ->
            val angleIncrement = 360f / dotCount
            val angleOffset = if (index % 2 == 0) 0f else angleIncrement / 2
            for (i in 0 until dotCount) {
                val angle = i * angleIncrement + angleOffset
                val point = PolarToCartesian(radius.toPx(), angle) + centres[index]
                points.add(point)
                drawCircle(
                    circleColor.copy(0.7f - (index + 1).toFloat() / 10),
                    dotSize.toPx(),
                    point
                )
            }
        }
    }
}

private fun PolarToCartesian(radius: Float, angle: Float): Offset {
    val x = radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
    val y = radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
    return Offset(x, y)
}
