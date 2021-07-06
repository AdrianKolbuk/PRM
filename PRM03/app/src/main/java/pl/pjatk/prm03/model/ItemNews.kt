package pl.pjatk.prm03.model

import android.graphics.Bitmap

data class ItemNews(
        val title: String,
        val description: String,
        val link: String,
        val img: Bitmap,
        val imgUrl: String,
        var isRead: Boolean
)
