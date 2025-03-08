package st.masoom.emotiondetection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import st.masoom.emotiondetection.View.EmotionDetectionScreen2
import st.masoom.emotiondetection.View.EmotionDetectorScreen
import st.masoom.emotiondetection.ui.theme.EmotionDetectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmotionDetectionTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "emotion_detector"
    ) {
        composable("emotion_detector") { EmotionDetectorScreen(navController= navController) }
        composable("emotion_detection") { EmotionDetectionScreen2(navController =  navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavigation() {
    val navController = rememberNavController()
    AppNavigation(navController)
}