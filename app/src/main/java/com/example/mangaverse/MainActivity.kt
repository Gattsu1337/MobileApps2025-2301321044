package com.example.mangaverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mangaverse.ui.theme.MangaVerseTheme

class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     *
     * This is where the majority of initialization should go: calling `setContentView(int)`
     * to inflate the activity's UI, using `findViewById(int)` to programmatically interact
     * with widgets in the UI, and configuring the activity's initial state.
     *
     * This implementation sets up the main content view using Jetpack Compose. It enables
     * edge-to-edge display, applies the [MangaVerseTheme], and sets up a basic layout
     * with a [Scaffold] that contains a [Greeting] composable.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in [onSaveInstanceState].  **Note: Otherwise it is null.**
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangaVerseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as MangaVerseApp
                    MangaVerseNavHost(app = app)
                }
            }
        }
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
    MangaVerseTheme {
        Greeting("Android")
    }
}