package com.example.actividad1.domain.model

import android.net.Uri

enum class FsType { FOLDER, IMAGE, TEXT, OTHER }

data class FsItem(
    val uri: Uri,
    val name: String,
    val sizeBytes: Long?,
    val modified: Long?,
    val mimeType: String?,
    val type: FsType,
    val isFavorite: Boolean = false
)
