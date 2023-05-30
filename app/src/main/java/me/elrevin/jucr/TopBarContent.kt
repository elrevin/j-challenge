package me.elrevin.jucr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun TopBarContent() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ConcentricCircles(maxWidth.value.toInt())
        Text(text = "$maxHeight , $maxWidth")
    }
}

@Composable
private fun ConcentricCircles(widht: Int) {
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
                val point = GetPointCoords(radius.toPx(), angle) + centres[index]
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

private fun GetPointCoords(radius: Float, angle: Float): Offset {
    val x = radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
    val y = radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
    return Offset(x, y)
}
