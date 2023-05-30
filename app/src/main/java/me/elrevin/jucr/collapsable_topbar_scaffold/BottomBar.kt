package me.elrevin.jucr.collapsable_topbar_scaffold

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Bottom bar of the screen
 */
@Composable
fun BottomBar(
    content: @Composable () -> Unit
) {
    // Shape with rounded top corners
    val bottomBarShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)

    // Surface with the bottomBarShape and elevation
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = bottomBarShape,
        shadowElevation = 8.dp,
        content = content
    )
}