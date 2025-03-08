package st.masoom.emotiondetection.ViewModel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class EmotionDetectionViewModel : ViewModel() {
    val imageUri = mutableStateOf<Uri?>(null)
    val detectedEmotion = mutableStateOf("")

    // Face detection options
    private val detectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(detectorOptions)

    // Load and process image
    fun detectEmotionFromImage(context: Context, imageUri: Uri) {
        this.imageUri.value = imageUri // Store image URI
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val image = InputImage.fromBitmap(bitmap, 0)

        detector.process(image)
            .addOnSuccessListener { faces ->
                detectedEmotion.value = analyzeFace(faces)
            }
            .addOnFailureListener {
                detectedEmotion.value = "Error detecting emotion"
            }
    }

    // Analyze facial expressions
    private fun analyzeFace(faces: List<Face>): String {
        if (faces.isEmpty()) return "No face detected"
        val face = faces[0]

        val smiling = face.smilingProbability ?: 0.0f
        val leftEyeOpen = face.leftEyeOpenProbability ?: 0.0f
        val rightEyeOpen = face.rightEyeOpenProbability ?: 0.0f

        return when {
            smiling > 0.7 -> "Happy 😊"
            smiling < 0.3 && leftEyeOpen < 0.5 && rightEyeOpen < 0.5 -> "Sad 😢"
            leftEyeOpen < 0.3 && rightEyeOpen < 0.3 -> "Sleepy 😴"
            else -> "Neutral 😐"
        }
    }
}
