package com.uas.miproyecto.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun CustomSpinner(
    carreraSeleccionada: String,
    onCarreraSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val carreras = listOf(
        "Ingeniería en Software",
        "Ingeniería Industrial",
        "Contador Público",
        "Licenciatura en Administración"
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = carreraSeleccionada,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Carrera") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            carreras.forEach { carrera ->
                DropdownMenuItem(
                    text = { Text(carrera) },
                    onClick = {
                        onCarreraSelected(carrera)
                        expanded = false
                    }
                )
            }
        }
    }
}