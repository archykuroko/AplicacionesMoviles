package com.example.actividad1.domain.repo

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.actividad1.data.datastore.PrefsRepository
import com.example.actividad1.domain.model.FsItem
import com.example.actividad1.domain.model.FsType
import java.io.InputStream
import java.io.OutputStream

class FileRepository(
    private val context: Context,
    private val saf: SafOps,
    @Suppress("unused")
    private val prefs: PrefsRepository
) {

    fun list(pathUri: Uri, favorites: Set<String>): List<FsItem> {
        val root: DocumentFile = DocumentFile.fromTreeUri(context, pathUri) ?: return emptyList()
        val children: List<DocumentFile> = saf.listChildren(root)

        return children.map { df: DocumentFile ->
            val name: String = df.name ?: "(sin nombre)"
            val mime: String? = df.type ?: context.contentResolver.getType(df.uri)
            val type: FsType = when {
                df.isDirectory -> FsType.FOLDER
                (mime?.startsWith("image/") == true) -> FsType.IMAGE
                isTextLike(df, mime) -> FsType.TEXT
                else -> FsType.OTHER
            }

            FsItem(
                uri = df.uri,
                name = name,
                sizeBytes = if (df.isFile) df.length() else null,
                modified = df.lastModified(),
                mimeType = mime,
                type = type,
                isFavorite = favorites.contains(df.uri.toString())
            )
        }.sortedWith(compareBy<FsItem> { it.type != FsType.FOLDER }.thenBy { it.name.lowercase() })
    }

    /** Lee archivo de texto como UTF-8. */
    fun openText(uri: Uri): String {
        val input: InputStream = saf.openInput(uri) ?: return ""
        return input.bufferedReader(Charsets.UTF_8).use { it.readText() }
    }

    /** Copia archivo a [dstDir]; si [newName] es nulo, conserva el nombre. */
    fun copyFile(src: Uri, dstDir: DocumentFile, newName: String? = null): Boolean {
        val srcDf = DocumentFile.fromSingleUri(context, src) ?: return false
        val name = newName ?: (srcDf.name ?: "copy")
        val mime = context.contentResolver.getType(src) ?: srcDf.type ?: "application/octet-stream"
        val dst = dstDir.createFile(mime, name) ?: return false

        val input: InputStream = saf.openInput(src) ?: return false
        val output: OutputStream = saf.openOutput(dst.uri) ?: return false
        input.use { i -> output.use { o -> i.copyTo(o) } }
        return true
    }

    private fun isTextLike(df: DocumentFile, mime: String?): Boolean {
        val n = df.name?.lowercase() ?: return false
        return mime in setOf("text/plain", "application/json", "application/xml") ||
                n.endsWith(".md") || n.endsWith(".log") || n.endsWith(".json") || n.endsWith(".xml") || n.endsWith(".txt")
    }
}
