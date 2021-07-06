package pl.pjatk.traveler.model

import android.graphics.Bitmap
import android.net.Uri

data class Photo(
    val id: Long,
    val note: String,
    val uri: String,
    val photoBitmap: Bitmap
)