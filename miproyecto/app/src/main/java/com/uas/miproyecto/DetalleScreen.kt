package com.uas.miproyecto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetalleScreen(
    matricula: String,
    nombre: String,
    carrera: String,
    turno: String,
    estatus: Boolean,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Confirmación de Registro",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Matrícula:", style = MaterialTheme.typography.labelLarge)
                    Text(text = matricula, fontSize = 18.sp)

                    Text(text = "Nombre:", style = MaterialTheme.typography.labelLarge)
                    Text(text = nombre, fontSize = 18.sp)

                    Text(text = "Carrera:", style = MaterialTheme.typography.labelLarge)
                    Text(text = carrera, fontSize = 18.sp)

                    Text(text = "Turno:", style = MaterialTheme.typography.labelLarge)
                    Text(text = turno, fontSize = 18.sp)

                    Text(text = "Estatus:", style = MaterialTheme.typography.labelLarge)
                    Text(text = if (estatus) "Activo" else "Inactivo", fontSize = 18.sp)
                }
            }

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}