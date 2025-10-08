package com.example.actividad1.ui.screens.imageviewer

import android.net.Uri
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerScreen(uri: Uri?) {
    var rotation by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Imagen") },
                actions = {
                    IconButton(onClick = { rotation = (rotation + 90f) % 360f }) {
                        Icon(Icons.Default.RotateRight, contentDescription = "Rotar")
                    }
                }
            )
        }
    ) { p ->
        if (uri == null) {
            Box(Modifier.fillMaxSize().padding(p), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Sin imagen")
            }
        } else {
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(p)
                    .graphicsLayer {
                        rotationZ = rotation
                        scaleX = scale
                        scaleY = scale
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 6f)
                        }
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}
