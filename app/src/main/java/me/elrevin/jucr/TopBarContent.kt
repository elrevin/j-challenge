package me.elrevin.jucr

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun TopBarContent() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        ConcentricCircles(maxWidth.value.toInt())
        Text(
            modifier = Modifier.align(Alignment.TopCenter).offset(-(maxWidth.value/2f).dp),
            text = "Hello"
        )
        //TextAnimationBox()
    }
}

@Composable
private fun ConcentricCircles(width: Int) {
    val circleColor = Color.White
    val circleSizes = listOf(
        (width / 2 + 5).dp, (width / 2 - 10).dp, (width / 2 - 25).dp, (width / 2 - 40).dp
    )
    val circleSizesPx = with(LocalDensity.current) {
        circleSizes.map {
            it.toPx()
        }
    }
    val dotSize = 1.dp
    val dotCount = 90

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2 * 0.7f)

        val centres = listOf<Offset>(
            center,
            Offset(center.x, center.y + circleSizesPx[0] - circleSizesPx[1]),
            Offset(center.x, center.y + circleSizesPx[0] - circleSizesPx[2]),
            Offset(center.x, center.y + circleSizesPx[0] - circleSizesPx[3])
        )

        val points = mutableListOf<Offset>()
        circleSizes.forEachIndexed { index, radius ->
            val baseAlpha = 0.4f
            val angleIncrement = 360f / dotCount
            val angleOffset = if (index % 2 == 0) 0f else angleIncrement / 2
            for (i in 0 until dotCount) {
                val angle = i * angleIncrement + angleOffset

                val alpha =
                    baseAlpha //* if (angle <= 180f) abs(angle - 90f) / 90f else abs(angle - 270f) / 90f

                val point = GetPointCoords(radius.toPx(), angle) + centres[index]
                points.add(point)
                drawCircle(
                    circleColor.copy(alpha), dotSize.toPx(), point
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

@Composable
fun TextAnimationBox() {
    var isTextMoved by remember { mutableStateOf(false) }
    val textOffset by animateOffsetAsState(
        if (isTextMoved) Offset(0f, 0f) else Offset(-140f, -140f),
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .size(300.dp)
            .background(color = Color.LightGray)
            .padding(16.dp)
    ) {
        Text(
            text = "Hello, World!",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(textOffset.x.dp, textOffset.y.dp)
        )
    }
    Button(
        onClick = { isTextMoved = !isTextMoved },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Move Text")
    }
}