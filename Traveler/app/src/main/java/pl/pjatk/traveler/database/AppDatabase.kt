package pl.pjatk.traveler.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.pjatk.traveler.model.PhotoNoteDto

@Database(
    entities = arrayOf(PhotoNoteDto::class),
    version = 1
)
abstract class AppDatabase() : RoomDatabase() {
    abstract val photoNote: PhotoNoteDao

    companion object{
        fun open(context: Context) = Room.databaseBuilder(
            context, AppDatabase::class.java, "photoNoteDb"
        ).build()
    }
}