package me.elrevin.jucr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import me.elrevin.jucr.ui.CircularProgressIndicator
import me.elrevin.jucr.ui.theme.FontAwesome
import kotlin.math.abs

/**
 * Content of the collapsable top bar
 *
 * @param state State of the top bar
 */
@Composable
fun TopBarContent(
    state: TopBarState
) {
    // Current screen density
    val density = LocalDensity.current

    // Width of the greating text element, it calculation is below
    var greetingsTextWidth: Dp by remember {
        mutableStateOf(0.dp)
    }

    // Width of the charging indication text element
    var chargingTextWidth: Dp by remember {
        mutableStateOf(0.dp)
    }

    // Width of the charging progress text element
    var chargingTimeTextWidth: Dp by remember {
        mutableStateOf(0.dp)
    }

    // Radius of circles below the car image will be changing in animation
    val transition = rememberInfiniteTransition()
    val circlesRadiusChange = transition.animateFloat(
        0.5f,
        1.5f,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 10000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Centres of the circles will be changing too
    val circlesCenterChangeAngle = transition.animateFloat(
        0f,
        360f,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 20000,
                easing = LinearEasing
            )
        )
    )

    // The top bar resize coefficient
    val resizeValue = 1f - (state.getHeightInDp().value - state.collapsedHeight.value) /
            (state.expandedHeight.value - state.collapsedHeight.value)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        // Draw the circles
        ConcentricCircles(
            maxWidth.value.toInt(),
            resizeValue,
            circlesRadiusChange.value,
            circlesCenterChangeAngle.value
        )

        // Calculate of a position of the charging indication text element
        val chargingTextPosition = DpOffset(
            (maxWidth - chargingTextWidth) / 2,
            84.dp
        )

        // Calculate of a position of the charging progress text element
        val chargingTimeTextPosition = DpOffset(
            (maxWidth - chargingTimeTextWidth) / 2,
            246.dp
        )

        // Calculate of the car image's position
        val carPosition = DpOffset(
            (maxWidth - 246.dp) / 2,
            140.dp
        )

        // Animate the greeting, when the top bar will start ro shrink in height, the greeting will be hidden
        AnimatedVisibility(
            modifier = Modifier
                .offset(chargingTextPosition.x, 58.dp)
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

        // Change text of charging indication text element
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

        // Change text of charging progress text element
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
        ) { chargingTimeString ->
            val chargingTimeStringBuilder = AnnotatedString.Builder(chargingTimeString)
            if (chargingTimeString == "TIME TO END OF CHARGE: 49 MIN") {
                chargingTimeStringBuilder.addStyle(
                    SpanStyle(fontWeight = FontWeight.Bold),
                    23,
                    chargingTimeString.length
                )
            } else {
                chargingTimeStringBuilder.addStyle(
                    SpanStyle(
                        fontFamily = FontAwesome,
                        fontSize = 8.sp
                    ), 10, 11
                )
            }

            Text(
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                text = chargingTimeStringBuilder.toAnnotatedString()
            )
        }

        // Circular indicator of charging progress has to be hidden when the toolbar is going to be decreased in size.
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
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(58.dp)
                        .alpha(0.7f)
                )
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⚡",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontAwesome
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "58%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        // Expand button should appear hidden when the toolbar is going to be decreased in size.
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

/**
 * Animated concentric circles for the background of the top bar
 *
 * @param width Width of canvas
 * @param resizeValue The top bar resize coefficient
 * @param circlesRadiusChange Coefficient for circles' radius changing in animation
 * @param circlesCenterChangeAngle Animated value of an angle for calculating the dynamic circles' centers offset
 */
@Composable
private fun ConcentricCircles(
    width: Int,
    resizeValue: Float,
    circlesRadiusChange: Float,
    circlesCenterChangeAngle: Float
) {
    val circleColor = Color.White
    val halfWidth = width / 2

    // calculate radius of the circles considering animated radius changing and
    val circleSizes = listOf(
        ((halfWidth + 5  * circlesRadiusChange) - (halfWidth + 5 ) * 0.5f * resizeValue).dp,
        ((halfWidth - 10 * circlesRadiusChange) - (halfWidth - 10) * 0.5f * resizeValue).dp,
        ((halfWidth - 25 * circlesRadiusChange) - (halfWidth - 25) * 0.5f * resizeValue).dp,
        ((halfWidth - 40 * circlesRadiusChange) - (halfWidth - 40) * 0.5f * resizeValue).dp
    )

    // Converting radius of all circles to px
    val circleSizesPx = with(LocalDensity.current) {
        circleSizes.map {
            it.toPx()
        }
    }

    val dotSize = 1.dp
    val dotCount = 90

    Canvas(modifier = Modifier.fillMaxSize()) {

        // Calculate of offset of next circle
        val centerOffset = getPointCoords(50f, circlesCenterChangeAngle)

        // calculate of the base center position
        val center = Offset(
            size.width / 2 + (size.width / 2) * resizeValue * 0.7f + centerOffset.x,
            size.height / 2 * 0.7f + centerOffset.y
        )

        // calculate of center positions of all circles
        val centres = listOf<Offset>(
            center,
            Offset(
                center.x + (circleSizesPx[0] - circleSizesPx[1]) * resizeValue,
                center.y + (circleSizesPx[0] - circleSizesPx[1]) * (1 - resizeValue) * circlesRadiusChange
            ),
            Offset(
                center.x + (circleSizesPx[0] - circleSizesPx[2]) * resizeValue,
                center.y + (circleSizesPx[0] - circleSizesPx[2]) * (1 - resizeValue) * circlesRadiusChange
            ),
            Offset(
                center.x + (circleSizesPx[0] - circleSizesPx[3]) * resizeValue,
                center.y + (circleSizesPx[0] - circleSizesPx[3]) * (1 - resizeValue) * circlesRadiusChange
            )
        )

        // Draw circles by points
        circleSizes.forEachIndexed { index, radius ->
            val baseAlpha = 0.4f
            val angleIncrement = 360f / dotCount
            val angleOffset = if (index % 2 == 0) 0f else angleIncrement / 2
            for (i in 0 until dotCount) {
                val angle = i * angleIncrement + angleOffset

                // Points which placed nearby of top and bottom of circle must be more transparent
                val alpha =
                    baseAlpha * if (angle <= 180f) abs(angle - 90f) / 90f else abs(angle - 270f) / 90f

                // Get the point coordinates
                val point = getPointCoords(radius.toPx(), angle) + centres[index]

                // Draw the point
                drawCircle(
                    circleColor.copy(alpha), dotSize.toPx(), point
                )
            }
        }
    }
}

private fun getPointCoords(radius: Float, angle: Float): Offset {
    val x = radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
    val y = radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
    return Offset(x, y)
}