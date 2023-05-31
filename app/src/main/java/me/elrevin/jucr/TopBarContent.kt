package me.elrevin.jucr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.elrevin.jucr.collapsable_topbar_scaffold.TopBarState
import me.elrevin.jucr.ui.Loader
import me.elrevin.jucr.ui.theme.FontAwesome
import kotlin.math.abs

@OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun TopBarContent(
    state: TopBarState
) {
    val density = LocalDensity.current
    var greetingsTextWidth: Dp by remember {
        mutableStateOf(0.dp)
    }

    var chargingTextWidth: Dp by remember {
        mutableStateOf(0.dp)
    }

    var chargingTimeTextWidth: Dp by remember {
        mutableStateOf(0.dp)
    }

    val resizeValue = 1f - (state.getHeightInDp().value - state.collapsedHeight.value) /
            (state.expandedHeight.value - state.collapsedHeight.value)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        ConcentricCircles(maxWidth.value.toInt(), resizeValue)

        val greetingsTextX = (maxWidth - greetingsTextWidth) / 2
        val chargingTextPosition = DpOffset(
            (maxWidth - chargingTextWidth) / 2,
            84.dp
        )
        val chargingTimeTextPosition = DpOffset(
            (maxWidth - chargingTimeTextWidth) / 2,
            246.dp
        )

        val carPosition = DpOffset(
            (maxWidth - 246.dp) / 2,
            140.dp
        )

        AnimatedVisibility(
            modifier = Modifier
                .offset(greetingsTextX, 58.dp)
                .onGloballyPositioned {
                    if (greetingsTextWidth.value == 0f) {
                        greetingsTextWidth = with(density) {
                            it.size.width.toDp()
                        }
                    }
                },
            visible = resizeValue < 0.2,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                text = "Good Morning, Billy"
            )
        }

        AnimatedContent(
            modifier = Modifier
                .offset(
                    (chargingTextPosition.x.value - (chargingTextPosition.x.value - 26f) * resizeValue).dp,
                    (chargingTextPosition.y.value - (chargingTextPosition.y.value - 54f) * resizeValue).dp
                )
                .onGloballyPositioned {
                    if (chargingTextWidth.value == 0f) {
                        chargingTextWidth = with(density) {
                            it.size.width.toDp()
                        }
                    }
                },
            targetState = if (resizeValue < 0.5f) "Charging your car..." else "Tesla model X",
            transitionSpec = {
                fadeIn().togetherWith(fadeOut())
            }
        ) { targetString ->
            Text(
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = (22f - 6f * resizeValue).toInt().sp,
                    lineHeight = (28f - 10f * resizeValue).toInt().sp,
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                text = targetString
            )
        }

        Image(
            modifier = Modifier
                .offset(
                    (carPosition.x.value + 170 * resizeValue).dp,
                    (carPosition.y.value - 80 * resizeValue).dp
                )
                .size(
                    (state.getHeightInDp().value * 246 / state.expandedHeight.value).dp,
                    (state.getHeightInDp().value * 94 / state.expandedHeight.value).dp
                ),
            painter = painterResource(id = R.drawable.car),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )

        AnimatedContent(
            modifier = Modifier
                .offset(
                    (chargingTimeTextPosition.x.value - (chargingTimeTextPosition.x.value - 26f) * resizeValue).dp,
                    (chargingTimeTextPosition.y.value - (chargingTimeTextPosition.y.value - 80f) * resizeValue).dp
                )
                .onGloballyPositioned {
                    chargingTimeTextWidth = with(density) {
                        it.size.width.toDp()
                    }
                },
            targetState = if (resizeValue < 0.5f) "TIME TO END OF CHARGE: 49 MIN" else "Charging: ⚡ 58%",
            transitionSpec = {
                fadeIn().togetherWith(fadeOut())
            }
        ) {chargingTimeString ->
            val chargingTimeStringBuilder = AnnotatedString.Builder(chargingTimeString)
            if (chargingTimeString == "TIME TO END OF CHARGE: 49 MIN") {
                chargingTimeStringBuilder.addStyle(SpanStyle(fontWeight = FontWeight.Bold), 23, chargingTimeString.length)
            } else {
                chargingTimeStringBuilder.addStyle(SpanStyle(fontFamily = FontAwesome, fontSize = 8.sp), 10, 11)
            }

            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                text = chargingTimeStringBuilder.toAnnotatedString()
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .offset(maxWidth / 2 - 29.dp, state.getHeightInDp() - 74.dp),
            visible = resizeValue < 0.2,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp),
                contentAlignment = Alignment.Center
            ) {
                Loader(
                    modifier = Modifier
                        .size(58.dp)
                        .alpha(0.7f)
                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "⚡",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontAwesome
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(text = "58%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .offset(maxWidth / 2 - 20.dp, state.getHeightInDp() - 44.dp),
            visible = resizeValue > 0.2,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(
                            onClick = { state.expand() },
                            enabled = true,
                            role = Role.Button,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = false,
                                radius = 20.dp,
                                color = Color.White
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_angle_down),
                        contentDescription = "Expand top bar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ConcentricCircles(width: Int, resizeValue: Float) {
    val circleColor = Color.White
    val halfWidth = width / 2
    val circleSizes = listOf(
        ((halfWidth + 5 ) - (halfWidth + 5 ) * 0.5f * resizeValue).dp,
        ((halfWidth - 10) - (halfWidth - 10) * 0.5f * resizeValue).dp,
        ((halfWidth - 25) - (halfWidth - 25) * 0.5f * resizeValue).dp,
        ((halfWidth - 40) - (halfWidth - 40) * 0.5f * resizeValue).dp
    )
    val circleSizesPx = with(LocalDensity.current) {
        circleSizes.map {
            it.toPx()
        }
    }
    val dotSize = 1.dp
    val dotCount = 90

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2 + (size.width / 2) * resizeValue * 0.7f, size.height / 2 * 0.7f)

        val centres = listOf<Offset>(
            center,
            Offset(center.x + (circleSizesPx[0] - circleSizesPx[1]) * resizeValue, center.y + (circleSizesPx[0] - circleSizesPx[1]) * (1 - resizeValue)),
            Offset(center.x + (circleSizesPx[0] - circleSizesPx[2]) * resizeValue, center.y + (circleSizesPx[0] - circleSizesPx[2]) * (1 - resizeValue)),
            Offset(center.x + (circleSizesPx[0] - circleSizesPx[3]) * resizeValue, center.y + (circleSizesPx[0] - circleSizesPx[3]) * (1 - resizeValue))
        )

        val points = mutableListOf<Offset>()
        circleSizes.forEachIndexed { index, radius ->
            val baseAlpha = 0.4f
            val angleIncrement = 360f / dotCount
            val angleOffset = if (index % 2 == 0) 0f else angleIncrement / 2
            for (i in 0 until dotCount) {
                val angle = i * angleIncrement + angleOffset

                val alpha =
                    baseAlpha * if (angle <= 180f) abs(angle - 90f) / 90f else abs(angle - 270f) / 90f

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