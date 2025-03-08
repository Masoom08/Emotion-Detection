package st.masoom.emotiondetection.View

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

@Composable
fun EmotionDetectionScreen2(navController: NavController) {
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    var detectedEmotion by remember { mutableStateOf("") }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            selectedImage = bitmap
            detectEmotion(bitmap, context) { emotion ->
                detectedEmotion = emotion
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            selectedImage = it
            detectEmotion(it, context) { emotion ->
                detectedEmotion = emotion
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp),
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
            Text(
                text = "Emotion Detection",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
        }

        // Instruction Text
        Text(
            text = "Upload a photo of a person's face to detect their emotions.",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Buttons for Image Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { galleryLauncher.launch("image/*")/* Choose from Library Action */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D80F2)),
                shape = RoundedCornerShape(50)
            ) {
                Text("Choose from Library", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { cameraLauncher.launch(null)/* Take Photo Action */ },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(140.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F2F5)),
                shape = RoundedCornerShape(50)
            ) {
                Text("Take Photo", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        selectedImage?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp)
            )
            Text(
                text = "Detected Emotion: $detectedEmotion",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
            )
        }
    }
}
fun detectEmotion(bitmap: Bitmap, context: Context, onResult: (String) -> Unit) {
    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(options)
    val image = InputImage.fromBitmap(bitmap, 0)

    detector.process(image)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                val face = faces[0]
                val smileProb = face.smilingProbability ?: 0.0f
                val leftEyeOpenProb = face.leftEyeOpenProbability ?: 0.0f
                val rightEyeOpenProb = face.rightEyeOpenProbability ?: 0.0f

                val happy = (smileProb * 100).toInt()
                val sad = (100 - happy).coerceAtLeast(0)
                val neutral = ((1 - (smileProb + (1 - leftEyeOpenProb) / 2 + (1 - rightEyeOpenProb) / 2)) * 100).toInt()
                val confused = ((1 - smileProb) * 50).toInt()
                val angry = (100 - happy - neutral - confused).coerceAtLeast(0)

                val result = """
                    😊 Happy: $happy%
                    😢 Sad: $sad%
                    😐 Neutral: $neutral%
                    🤔 Confused: $confused%
                    😡 Angry: $angry%
                """.trimIndent()

                onResult(result)
            } else {
                onResult("No Face Detected")
            }
        }
        .addOnFailureListener { e ->
            Log.e("EmotionDetection", "Face Detection Failed: ${e.message}")
            onResult("Error Detecting Emotion")
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmotionDetectorScreen2() {
    val navController = rememberNavController()
    EmotionDetectionScreen2(navController)
}

