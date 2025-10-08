package com.example.actividad1.ui.screens.browser

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.actividad1.domain.model.FsItem
import com.example.actividad1.domain.model.FsType
import com.example.actividad1.ui.screens.browser.FileItem
import android.net.Uri

@Composable
fun BrowserContent(
    items: List<FsItem>,
    onItemClick: (FsItem) -> Unit,
    onFav: (FsItem) -> Unit,
    onGoUp: () -> Unit,
    onRenameRequest: (uri: Uri, currentName: String) -> Unit,
    onDeleteRequest: (uri: Uri) -> Unit,
    onCopyRequest: (item: FsItem) -> Unit,
    onCutRequest: (item: FsItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var isList by remember { mutableStateOf(true) }

    Column(modifier.fillMaxSize()) {

        TopBarPathActions(
            pathText = currentPathLabel(items),
            isList = isList,
            onToggleView = { isList = !isList },
            onGoUp = onGoUp
        )

        Divider()

        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Carpeta vacía")
            }
        } else {
            if (isList) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(items.size) { idx ->
                        val it = items[idx]
                        FileItem(
                            item = it,
                            onClick = { onItemClick(it) },
                            onFav = onFav,
                            onRename = onRenameRequest,
                            onDelete = onDeleteRequest,
                            onCopy = onCopyRequest,
                            onCut  = onCutRequest
                        )
                        Divider()
                    }
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { it ->
                        GridFileItem(
                            item = it,
                            onClick = { onItemClick(it) },
                            onFav = onFav
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun currentPathLabel(items: List<FsItem>): String {
    return if (items.isEmpty()) "Contenido actual" else "Contenido actual (${items.size} elementos)"
}

@Composable
private fun TopBarPathActions(
    pathText: String,
    isList: Boolean,
    onToggleView: () -> Unit,
    onGoUp: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            pathText,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onGoUp, modifier = Modifier.padding(start = 4.dp)) {
            Icon(Icons.Filled.ArrowUpward, contentDescription = "Subir nivel")
        }
        IconButton(onClick = onToggleView, modifier = Modifier.padding(start = 4.dp)) {
            Icon(
                if (isList) Icons.Filled.GridView else Icons.Filled.List,
                contentDescription = if (isList) "Vista cuadrícula" else "Vista lista"
            )
        }
    }
}

@Composable
private fun GridFileItem(
    item: FsItem,
    onClick: () -> Unit,
    onFav: (FsItem) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(2.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(12.dp)) {
            val emoji = when (item.type) {
                FsType.FOLDER -> "📁"
                FsType.IMAGE  -> "🖼️"
                FsType.TEXT   -> "📄"
                FsType.OTHER  -> "📦"
            }
            Text(emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                item.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            AssistChip(
                onClick = { onFav(item) },
                label = { Text(if (item.isFavorite) "Favorito" else "Marcar") }
            )
        }
    }
}
