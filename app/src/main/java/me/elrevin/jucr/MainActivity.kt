package me.elrevin.jucr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.elrevin.jucr.ui.theme.JUCRChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JUCRChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Magenta,
                ) {
                    MainScreen()
                }
            }
        }
    }
}