package com.pinto.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.pinto.recordatorios.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtenemos el contenedor de dependencias desde la Application
        val container = (application as RecordatoriosApplication).container

        setContent {
            MaterialTheme {
                AppNavigation(container = container)
            }
        }
    }
}