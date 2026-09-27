package com.uas.miproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class DetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val matricula = intent.getStringExtra("EXTRA_MATRICULA") ?: "Sin matrícula"
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Sin nombre"
        val carrera = intent.getStringExtra("EXTRA_CARRERA") ?: "Sin carrera"
        val turno = intent.getStringExtra("EXTRA_TURNO") ?: "Sin turno"
        val estatus = intent.getBooleanExtra("EXTRA_ESTATUS", false)

        setContent {
            DetalleScreen(
                matricula = matricula,
                nombre = nombre,
                carrera = carrera,
                turno = turno,
                estatus = estatus,
                onBackClick = { finish() }
            )
        }
    }
}
