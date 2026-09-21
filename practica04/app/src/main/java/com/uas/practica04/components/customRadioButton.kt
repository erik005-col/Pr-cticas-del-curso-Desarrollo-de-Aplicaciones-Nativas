package com.uas.practica04.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val opciones = listOf("Opción 1", "Opción 2")

    Row(verticalAlignment = Alignment.CenterVertically) {
        opciones.forEach { opcion ->
            RadioButton(
                selected = selectedOption == opcion,
                onClick = { onOptionSelected(opcion) }
            )
            Text(
                text = opcion,
                modifier = Modifier.clickable { onOptionSelected(opcion) }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}