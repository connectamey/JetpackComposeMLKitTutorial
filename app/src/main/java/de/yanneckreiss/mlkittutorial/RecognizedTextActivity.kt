package de.yanneckreiss.mlkittutorial

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.yanneckreiss.mlkittutorial.ui.theme.JetpackComposeMLKitTutorialTheme


class RecognizedTextActivity : ComponentActivity() {
    private val TAG: String? = "RecognizedTextActivity"
    private var tts: TextToSpeech? = null
    private var detectedText: String = ""
    private var ttsListener: TextToSpeech.OnInitListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ttsListener = TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR) {
                    Log.d(TAG, "onCreate: TTS FAILED")
                }
            }
            detectedText = intent.getStringExtra("detected_text") ?: "Loading..."
            tts = TextToSpeech(this, ttsListener )


            JetpackComposeMLKitTutorialTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { androidx.compose.material.Text("Quick Learning") },
                        actions = {
                            // Action items
                            IconButton(onClick = { speakOut() }) {
                                Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                            }
                            IconButton(onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "text/plain"
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Text From Image")
                                shareIntent.putExtra(Intent.EXTRA_TEXT, detectedText)

                                startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Choose an app to share"
                                    )
                                ) }) {
                                Icon(Icons.Filled.NoteAlt, contentDescription = "Note")
                            }
                            IconButton(onClick = {
                                val textToTranslate =detectedText
                                val translateUrl =
                                    "https://translate.google.com/?sl=auto&tl=en&text=" + Uri.encode(
                                        textToTranslate
                                    ) + "&op=translate"

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(translateUrl))
                                intent.setPackage("com.android.chrome") // Set Chrome's package name


                                try {
                                    startActivity(intent)
                                } catch (ex: ActivityNotFoundException) {
                                    // Chrome is not installed, so fall back to another browser or handle the error
                                    intent.setPackage(null) // Clear the specific package requirement
                                    startActivity(intent) // Attempt to open the URL in a different browser
                                }}) {
                                Icon(Icons.Filled.LocalLibrary, contentDescription = "Translate")
                            }
                        }) },
                ) { paddingValues: PaddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.BottomCenter
                    ) {

                        Text(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(androidx.compose.ui.graphics.Color.White)
                                .padding(16.dp),
                            text = detectedText,
                        )

                    }
                }
            }
        }
    }
    private fun speakOut() {
        tts!!.speak(detectedText, TextToSpeech.QUEUE_FLUSH, null,"")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeMLKitTutorialTheme {
        Greeting("Android")
    }
}


