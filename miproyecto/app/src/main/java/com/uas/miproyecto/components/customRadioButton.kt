package com.uas.miproyecto.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(
    turnoSeleccionado: String,
    onTurnoSelected: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = (turnoSeleccionado == "Matutino"),
            onClick = { onTurnoSelected("Matutino") }
        )
        Text(
            text = "Matutino",
            modifier = Modifier.selectable(
                selected = (turnoSeleccionado == "Matutino"),
                onClick = { onTurnoSelected("Matutino") }
            )
        )

        Spacer(modifier = Modifier.width(16.dp))

        RadioButton(
            selected = (turnoSeleccionado == "Vespertino"),
            onClick = { onTurnoSelected("Vespertino") }
        )
        Text(
            text = "Vespertino",
            modifier = Modifier.selectable(
                selected = (turnoSeleccionado == "Vespertino"),
                onClick = { onTurnoSelected("Vespertino") }
            )
        )
    }
}