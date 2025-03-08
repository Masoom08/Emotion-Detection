package st.masoom.emotiondetection.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter

@Composable
fun EmotionDetectorScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp),  // Added padding for better spacing
        verticalArrangement = Arrangement.spacedBy(16.dp),  // Even spacing
        horizontalAlignment = Alignment.CenterHorizontally  // Centered components
    ) {
        TopBar()
        ImageSection()

        Text(
            text = "What's your emotion?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
                .padding( 16.dp)
            ,
            textAlign = TextAlign.Start
        )

        DetectEmotionButton(navController)

        Text(
            text = "Powered by AI technology. All data is encrypted and secure.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))  // Pushes BottomNavigationBar to the bottom
        BottomNavigationBar()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top =16.dp),  // Adjusted padding for better alignment
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(48.dp))
        Text("Emotion Detector", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = { /* Settings Action */ }) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings"
            )
        }
    }
}

@Composable
fun ImageSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter("https://cdn.usegalileo.ai/sdxl10/c7e2fced-ce23-4945-beb0-de8c3d7a91e9.png"),
            contentDescription = "Emotion Detection Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun DetectEmotionButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("emotion_detection") },
        modifier = Modifier

            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D80F2))
    ) {
        Text("Detect Emotion", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),  // Adjusted padding
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = { /* Home Action */ }) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home"
            )
        }
        IconButton(onClick = { /* Profile Action */ }) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmotionDetectorScreen() {
    val navController =  rememberNavController()
    EmotionDetectorScreen(navController)
}