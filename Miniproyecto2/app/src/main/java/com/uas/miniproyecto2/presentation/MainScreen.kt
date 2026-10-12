package com.uas.miniproyecto2.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.uas.miniproyecto2.data.Tarea
import com.uas.miniproyecto2.domain.AppDatabase
import kotlinx.coroutines.launch

enum class Filtro { TODAS, PENDIENTES, COMPLETADAS }
enum class Orden { FECHA, ESTADO }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.tareaDao()
    val scope = rememberCoroutineScope()

    // Lectura reactiva de Room
    val tareas by remember { dao.obtenerTodas() }.collectAsState(initial = emptyList())

    // Estados de la interfaz
    var filtro by remember { mutableStateOf(Filtro.TODAS) }
    var orden by remember { mutableStateOf(Orden.FECHA) }
    var menuFiltro by remember { mutableStateOf(false) }
    var menuOpciones by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var tareaEditando by remember { mutableStateOf<Tarea?>(null) }

    // Aplicar filtro y orden a la lista
    val tareasMostradas = tareas
        .filter {
            when (filtro) {
                Filtro.TODAS -> true
                Filtro.PENDIENTES -> !it.completada
                Filtro.COMPLETADAS -> it.completada
            }
        }
        .let { lista ->
            when (orden) {
                Orden.FECHA -> lista.sortedByDescending { it.fechaCreacion }
                Orden.ESTADO -> lista.sortedBy { it.completada }
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // Menú de filtrado
                    IconButton(onClick = { menuFiltro = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                    DropdownMenu(expanded = menuFiltro, onDismissRequest = { menuFiltro = false }) {
                        DropdownMenuItem(
                            text = { Text("Todas") },
                            onClick = { filtro = Filtro.TODAS; menuFiltro = false },
                            trailingIcon = { if (filtro == Filtro.TODAS) Icon(Icons.Default.Check, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Pendientes") },
                            onClick = { filtro = Filtro.PENDIENTES; menuFiltro = false },
                            trailingIcon = { if (filtro == Filtro.PENDIENTES) Icon(Icons.Default.Check, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Completadas") },
                            onClick = { filtro = Filtro.COMPLETADAS; menuFiltro = false },
                            trailingIcon = { if (filtro == Filtro.COMPLETADAS) Icon(Icons.Default.Check, null) }
                        )
                    }

                    // Menú de opciones (ordenar y borrar completadas)
                    IconButton(onClick = { menuOpciones = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(expanded = menuOpciones, onDismissRequest = { menuOpciones = false }) {
                        DropdownMenuItem(
                            text = { Text("Ordenar por fecha") },
                            onClick = { orden = Orden.FECHA; menuOpciones = false },
                            trailingIcon = { if (orden == Orden.FECHA) Icon(Icons.Default.Check, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Ordenar por estado") },
                            onClick = { orden = Orden.ESTADO; menuOpciones = false },
                            trailingIcon = { if (orden == Orden.ESTADO) Icon(Icons.Default.Check, null) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Borrar completadas") },
                            onClick = {
                                menuOpciones = false
                                scope.launch { dao.eliminarCompletadas() }
                                Toast.makeText(context, "Tareas completadas eliminadas", Toast.LENGTH_SHORT).show()
                            },
                            leadingIcon = { Icon(Icons.Default.DeleteSweep, null) }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                tareaEditando = null
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (tareasMostradas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay tareas para mostrar", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tareasMostradas, key = { it.id }) { tarea ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Marcar como completada
                                Checkbox(
                                    checked = tarea.completada,
                                    onCheckedChange = { marcada ->
                                        scope.launch { dao.actualizar(tarea.copy(completada = marcada)) }
                                    }
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tarea.titulo,
                                        style = MaterialTheme.typography.titleMedium,
                                        textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
                                    )
                                    if (tarea.descripcion.isNotBlank()) {
                                        Text(
                                            text = tarea.descripcion,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                                // Editar
                                IconButton(onClick = {
                                    tareaEditando = tarea
                                    mostrarDialogo = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                // Eliminar
                                IconButton(onClick = {
                                    scope.launch { dao.eliminar(tarea) }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para crear o editar
    if (mostrarDialogo) {
        TareaDialog(
            tarea = tareaEditando,
            onDismiss = { mostrarDialogo = false },
            onGuardar = { titulo, descripcion ->
                scope.launch {
                    val editando = tareaEditando
                    if (editando == null) {
                        dao.insertar(Tarea(titulo = titulo, descripcion = descripcion))
                    } else {
                        dao.actualizar(editando.copy(titulo = titulo, descripcion = descripcion))
                    }
                }
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun TareaDialog(
    tarea: Tarea?,
    onDismiss: () -> Unit,
    onGuardar: (String, String) -> Unit
) {
    var titulo by remember { mutableStateOf(tarea?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(tarea?.descripcion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (tarea == null) "Nueva tarea" else "Editar tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (titulo.isNotBlank()) onGuardar(titulo.trim(), descripcion.trim())
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}