package com.example.practica11

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


private fun nombreDe(context: Context, uri: Uri): String {
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val col = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (col >= 0 && cursor.moveToFirst()) {
            return cursor.getString(col)
        }
    }
    return "Pista sin nombre"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReproductorScreen() {
    val context = LocalContext.current


    var playlist by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var indice by remember { mutableStateOf(0) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }


    fun reproducir(lista: List<Uri>, posicion: Int) {

        mediaPlayer?.release()

        val nuevo = MediaPlayer.create(context, lista[posicion])
        mediaPlayer = nuevo
        indice = posicion

        if (nuevo == null) {
            isPlaying = false
            return
        }

        nuevo.start()
        isPlaying = true


        nuevo.setOnCompletionListener {
            if (posicion + 1 < lista.size) {
                reproducir(lista, posicion + 1)
            } else {
                isPlaying = false
            }
        }
    }


    val selectorAudio = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            playlist = uris
            reproducir(uris, 0)
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mini Reproductor MP3") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Texto de estado: nombre de la canción y posición en la lista
            Text(
                text = if (playlist.isEmpty()) {
                    "Ningún archivo seleccionado"
                } else {
                    "${nombreDe(context, playlist[indice])}\nPista ${indice + 1} de ${playlist.size}"
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))


            Button(onClick = { selectorAudio.launch("audio/*") }) {
                Text("Buscar MP3 en SD/Memoria")
            }

            Spacer(modifier = Modifier.height(32.dp))


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                FloatingActionButton(
                    onClick = {
                        if (playlist.isNotEmpty()) {
                            val anterior = if (indice - 1 < 0) playlist.size - 1 else indice - 1
                            reproducir(playlist, anterior)
                        }
                    }
                ) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Anterior")
                }


                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && !isPlaying) {
                            mediaPlayer?.start()
                            isPlaying = true
                        }
                    }
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
                }


                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && isPlaying) {
                            mediaPlayer?.pause()
                            isPlaying = false
                        }
                    }
                ) {
                    Text(" || ", style = MaterialTheme.typography.titleLarge)
                }


                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null) {
                            mediaPlayer?.pause()
                            isPlaying = false
                            mediaPlayer?.seekTo(0)
                        }
                    }
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Detener")
                }


                FloatingActionButton(
                    onClick = {
                        if (playlist.isNotEmpty()) {
                            val siguiente = (indice + 1) % playlist.size
                            reproducir(playlist, siguiente)
                        }
                    }
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Siguiente")
                }
            }
        }
    }
}