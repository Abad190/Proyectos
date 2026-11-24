package org.proven.diarideviatges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.proven.diarideviatges.ui.navigation.AppNavigation
import org.proven.diarideviatges.ui.theme.DiariDeViatgesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiariDeViatgesTheme {
                AppNavigation()
            }
        }
    }
}
