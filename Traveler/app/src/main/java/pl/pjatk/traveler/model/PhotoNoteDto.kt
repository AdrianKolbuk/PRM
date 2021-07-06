package pl.pjatk.traveler.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PhotoNote")
data class PhotoNoteDto(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val note: String,
        val uri: String,
        val city: String,
        val country: String
)