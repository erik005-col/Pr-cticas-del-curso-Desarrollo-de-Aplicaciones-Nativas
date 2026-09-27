package com.uas.miproyecto.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun CustomSwitch(
    estatusActivo: Boolean,
    onEstatusChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Estatus: " + if (estatusActivo) "Activo" else "Inactivo", fontSize = 16.sp)
        Switch(
            checked = estatusActivo,
            onCheckedChange = { onEstatusChange(it) }
        )
    }
}