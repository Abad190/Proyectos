package org.proven.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Ajusta este import al nombre real de tu tema (carpeta ui/theme)
import org.proven.app.ui.theme.AppTheme

class MainComposeActivity : ComponentActivity() {

    private val controller = GameController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                TrileroGame(controller)
            }
        }
    }
}

// VIEW: solo dibuja el estado y delega los eventos en el controller
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrileroGame(controller: GameController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.fondo),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // FlowRow: los cubiletes saltan de línea al ir añadiendo más
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                controller.cubes.forEachIndexed { index, resId ->
                    Image(
                        painter = painterResource(resId),
                        contentDescription = "Cubilete ${index + 1}",
                        modifier = Modifier
                            .size(90.dp)
                            .clickable { controller.onCubeSelected(index + 1) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (controller.showButton) {
                Button(onClick = { controller.onStart() }) {
                    Text(text = controller.buttonText)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = controller.message,
                fontSize = 32.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrileroGame() {
    AppTheme {
        TrileroGame(remember { GameController() })
    }
}