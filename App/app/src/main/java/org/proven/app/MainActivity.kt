package org.proven.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redirige automáticamente al juego nada más abrir la app
        val intent = Intent(this, MainComposeActivity::class.java)
        startActivity(intent)
        finish() // Cierra el MainActivity para que no se quede abierto en segundo plano
    }
}