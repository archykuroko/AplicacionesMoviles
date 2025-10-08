package com.example.actividad1.ui.screens.browser

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.actividad1.domain.model.FsItem
import com.example.actividad1.domain.model.FsType
import com.example.actividad1.util.formatDate
import com.example.actividad1.util.formatSize

@Composable
fun FileItem(
    item: FsItem,
    onClick: () -> Unit,
    onFav: (FsItem) -> Unit,
    onRename: (uri: Uri, currentName: String) -> Unit,
    onDelete: (uri: Uri) -> Unit,
    onCopy: (FsItem) -> Unit,
    onCut: (FsItem) -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = when (item.type) {
            FsType.FOLDER -> Icons.Filled.Folder
            FsType.IMAGE  -> Icons.Filled.Image
            FsType.TEXT   -> Icons.Filled.Description
            FsType.OTHER  -> Icons.Filled.InsertDriveFile
        }
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyLarge)
            val meta = buildString {
                item.sizeBytes?.let { append(formatSize(it)); append("  •  ") }
                item.modified?.let { append(formatDate(it)) }
            }
            if (meta.isNotBlank()) Text(meta, style = MaterialTheme.typography.bodySmall)
        }

        IconToggleButton(checked = item.isFavorite, onCheckedChange = { onFav(item) }) {
            Icon(if (item.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder, contentDescription = "Favorito")
        }

        Box {
            IconButton(onClick = { menuOpen = true }) { Icon(Icons.Filled.MoreVert, contentDescription = "Más acciones") }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(text = { Text("Copiar") }, onClick = { menuOpen = false; onCopy(item) })
                DropdownMenuItem(text = { Text("Cortar") }, onClick = { menuOpen = false; onCut(item) })
                DropdownMenuItem(text = { Text("Renombrar") }, onClick = { menuOpen = false; onRename(item.uri, item.name) })
                DropdownMenuItem(text = { Text("Eliminar") }, onClick = { menuOpen = false; onDelete(item.uri) })
            }
        }
    }
}
