package com.example.actividad1.domain.repo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.InputStream
import java.io.OutputStream

class SafOps(private val context: Context) {
    fun takePersistablePermission(treeUri: Uri, flags: Int) {
        context.contentResolver.takePersistableUriPermission(
            treeUri, flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        )
    }
    fun docFile(uri: Uri): DocumentFile? = DocumentFile.fromTreeUri(context, uri)
    fun listChildren(dir: DocumentFile): List<DocumentFile> = dir.listFiles().toList()
    fun createFolder(parent: DocumentFile, name: String): DocumentFile? = parent.createDirectory(name)
    fun rename(doc: DocumentFile, newName: String): Boolean = doc.renameTo(newName)
    fun delete(doc: DocumentFile): Boolean = doc.delete()
    fun openInput(uri: Uri): InputStream? = context.contentResolver.openInputStream(uri)
    fun openOutput(uri: Uri): OutputStream? = context.contentResolver.openOutputStream(uri)
    fun mimeType(uri: Uri): String? = context.contentResolver.getType(uri)
}
