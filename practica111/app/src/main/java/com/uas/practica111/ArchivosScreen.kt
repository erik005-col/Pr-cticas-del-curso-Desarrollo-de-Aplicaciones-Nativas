package com.uas.practica111



import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


private const val NOMBRE_ARCHIVO = "nota.txt"

private enum class Destino(val etiqueta: String) {
    INTERNO("Memoria interna"),
    EXTERNO("SDCard / Externo")
}


private enum class Accion { GUARDAR, AGREGAR, LEER }


private fun obtenerArchivo(context: Context, destino: Destino): File? {
    return when (destino) {
        Destino.INTERNO -> File(context.filesDir, NOMBRE_ARCHIVO)
        Destino.EXTERNO -> {
            val dirs = context.getExternalFilesDirs(null)
            val dir = dirs.getOrNull(1) ?: dirs.getOrNull(0)
            // Verificamos que el almacenamiento esté montado y disponible
            if (dir != null && Environment.getExternalStorageState(dir) == Environment.MEDIA_MOUNTED) {
                File(dir, NOMBRE_ARCHIVO)
            } else null
        }
    }
}


private fun escribir(archivo: File, texto: String, append: Boolean) {
    FileOutputStream(archivo, append).use { salida ->
        salida.write(texto.toByteArray())
    }
}


private fun leer(archivo: File): String {
    return FileInputStream(archivo).bufferedReader().use { it.readText() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivosScreen() {
    val context = LocalContext.current

    var texto by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf(Destino.INTERNO) }
    var mensaje by remember { mutableStateOf("Escribe algo y elige dónde guardarlo") }
    var contenidoLeido by remember { mutableStateOf("") }
    var accionPendiente by remember { mutableStateOf<Accion?>(null) }


    fun ejecutar(accion: Accion) {
        val archivo = obtenerArchivo(context, destino)
        if (archivo == null) {
            mensaje = " ${destino.etiqueta} no está disponible"
            return
        }
        try {
            when (accion) {
                Accion.GUARDAR -> {
                    escribir(archivo, texto, append = false)
                    mensaje = " Guardado en ${destino.etiqueta}:\n${archivo.absolutePath}"
                }
                Accion.AGREGAR -> {
                    escribir(archivo, "\n$texto", append = true)
                    mensaje = " Texto agregado en ${destino.etiqueta}:\n${archivo.absolutePath}"
                }
                Accion.LEER -> {
                    if (archivo.exists()) {
                        contenidoLeido = leer(archivo)
                        mensaje = " Leído de ${destino.etiqueta}:\n${archivo.absolutePath}"
                    } else {
                        contenidoLeido = ""
                        mensaje = "⚠️ El archivo aún no existe en ${destino.etiqueta}"
                    }
                }
            }
        } catch (e: Exception) {
            mensaje = " Error: ${e.message}"
        }
    }


    val lanzadorPermisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->
        val concedidos = resultados.values.all { it }
        val accion = accionPendiente
        accionPendiente = null
        if (concedidos && accion != null) {
            ejecutar(accion)
        } else {
            mensaje = " Permiso de almacenamiento denegado"
        }
    }


    fun solicitarYEjecutar(accion: Accion) {
        val requierePermisos = destino == Destino.EXTERNO &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.P

        if (!requierePermisos) {
            ejecutar(accion)
            return
        }

        val permisos = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val todosConcedidos = permisos.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (todosConcedidos) {
            ejecutar(accion)
        } else {
            accionPendiente = accion
            lanzadorPermisos.launch(permisos)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Archivos: Interna y SDCard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Texto a guardar") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Text("Destino:", style = MaterialTheme.typography.titleMedium)
            Destino.values().forEach { opcion ->
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    RadioButton(
                        selected = destino == opcion,
                        onClick = { destino = opcion }
                    )
                    Text(opcion.etiqueta)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { solicitarYEjecutar(Accion.GUARDAR) }) { Text("Guardar") }
                Button(onClick = { solicitarYEjecutar(Accion.AGREGAR) }) { Text("Agregar") }
                Button(onClick = { solicitarYEjecutar(Accion.LEER) }) { Text("Leer") }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(mensaje, style = MaterialTheme.typography.bodyMedium)
                }
            }

            if (contenidoLeido.isNotEmpty()) {
                Text("Contenido del archivo:", style = MaterialTheme.typography.titleMedium)
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        contenidoLeido,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}