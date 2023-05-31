package me.elrevin.jucr.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun Loader(
    modifier: Modifier = Modifier,
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = 7.dp.toPx(), cap = StrokeCap.Butt)
    }
    val transition = rememberInfiniteTransition()
    val baseRotation = transition.animateFloat(
        0f,
        360f,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    )
    Canvas(
        modifier
    ) {
        drawCircularIndicator(
            0f,
            360f,
            Color.Transparent,
            Color.White,
            stroke,
            baseRotation.value
        )
    }
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    gradientStart: Color,
    gradientEnd: Color,
    stroke: Stroke,
    rotation: Float
) {
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset

    rotate(degrees = rotation) {
        drawArc(
            brush = Brush.sweepGradient(
                colorStops = listOf(
                    0.0f to gradientStart,
                    sweep / 360 to gradientEnd,
                ).toTypedArray()
            ),
            startAngle = startAngle + 90,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )

        val point = GetPointCoords(size.width / 2f - diameterOffset, sweep ) +
                Offset(size.width / 2, size.height / 2)

        drawCircle(gradientEnd, stroke.width / 2f, point)
    }
}

private fun GetPointCoords(radius: Float, angle: Float): Offset {
    val x = radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
    val y = radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
    return Offset(x, y)
}
