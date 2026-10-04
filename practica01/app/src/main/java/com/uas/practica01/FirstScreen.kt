package com.uas.practica01

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FirstScreen(onNavigateToSecondScreen: (String) -> Unit) {
    // Estado del campo de texto (lo que el usuario escribe)
    var nombreIngresado by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nombreIngresado,
                onValueChange = { nombreIngresado = it },
                label = { Text("Escribe tu nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (nombreIngresado.isNotBlank()) {
                        onNavigateToSecondScreen(nombreIngresado)
                    }
                }
            ) {
                Text("Saludar")
            }
        }
    }
}