package com.uas.practica04

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uas.practica04.components.*

@Composable
fun FormScreen() {
    var activado by remember { mutableStateOf(false) }
    var acepta by remember { mutableStateOf(false) }
    var opcionRadio by remember { mutableStateOf("Opción 1") }
    var elemento by remember { mutableStateOf("Seleccionar Opción") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var resumenText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Práctica 4: Componentes Avanzados", style = MaterialTheme.typography.headlineMedium)

        Text("1. Switch", style = MaterialTheme.typography.titleMedium)
        CustomSwitch(checked = activado, onCheckedChange = { activado = it })

        Text("2. RadioButton", style = MaterialTheme.typography.titleMedium)
        CustomRadioButton(selectedOption = opcionRadio, onOptionSelected = { opcionRadio = it })

        Text("3. Checkbox", style = MaterialTheme.typography.titleMedium)
        CustomCheckbox(checked = acepta, onCheckedChange = { acepta = it })

        Text("4. Spinner (DropdownMenu)", style = MaterialTheme.typography.titleMedium)
        CustomSpinner(selectedText = elemento, onItemSelected = { elemento = it })

        Text("5. DatePicker", style = MaterialTheme.typography.titleMedium)
        CustomDatePicker(
            selectedDate = fechaNacimiento,
            onDateSelected = { fechaNacimiento = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de procesamiento requerido por la rúbrica
        Button(
            onClick = {
                resumenText = """
                    RESUMEN DE DATOS CAPTURADOS:
                    • Switch: ${if (activado) "Activado" else "Desactivado"}
                    • RadioButton: $opcionRadio
                    • Términos: ${if (acepta) "Aceptados" else "No aceptados"}
                    • Selección: $elemento
                    • Fecha: ${fechaNacimiento.ifEmpty { "No seleccionada" }}
                """.trimIndent()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Procesar Datos")
        }

        // Vista contenedor para desplegar la información resumida
        if (resumenText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = resumenText,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}