package com.uas.miproyecto

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.miproyecto.components.CustomRadioButton
import com.uas.miproyecto.components.CustomSpinner
import com.uas.miproyecto.components.CustomSwitch
import com.uas.miproyecto.data.PreferencesManager

@Composable
fun RegistroScreen() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    var matricula by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var turno by remember { mutableStateOf("Matutino") }
    var estatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        matricula = preferencesManager.getMatricula()
        nombre = preferencesManager.getNombre()
        carrera = preferencesManager.getCarrera()
        turno = if (preferencesManager.getTurno().isNotEmpty()) preferencesManager.getTurno() else "Matutino"
        estatus = preferencesManager.getEstatus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro de Alumnos",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matrícula") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        CustomSpinner(
            carreraSeleccionada = carrera,
            onCarreraSelected = { carrera = it }
        )

        HorizontalDivider()

        Text("Turno:", fontSize = 16.sp)
        CustomRadioButton(
            turnoSeleccionado = turno,
            onTurnoSelected = { turno = it }
        )

        HorizontalDivider()

        CustomSwitch(
            estatusActivo = estatus,
            onEstatusChange = { estatus = it }
        )

        HorizontalDivider()

        Button(
            onClick = {
                if (matricula.isNotEmpty() && nombre.isNotEmpty() && carrera.isNotEmpty()) {
                    preferencesManager.saveData(matricula, nombre, carrera, turno, estatus)

                    val intent = Intent(context, DetalleActivity::class.java).apply {
                        putExtra("EXTRA_MATRICULA", matricula)
                        putExtra("EXTRA_NOMBRE", nombre)
                        putExtra("EXTRA_CARRERA", carrera)
                        putExtra("EXTRA_TURNO", turno)
                        putExtra("EXTRA_ESTATUS", estatus)
                    }
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Completa matrícula, nombre y carrera", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar y Ver Detalle")
        }
    }
}