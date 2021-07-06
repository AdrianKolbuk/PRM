package pl.pjatk.traveler.database


import androidx.room.*
import pl.pjatk.traveler.model.PhotoNoteDto

@Dao
interface PhotoNoteDao {
    @Insert
    fun insert(photo: PhotoNoteDto): Long

    @Update
    fun update(photo: PhotoNoteDto)

    @Query("SELECT * FROM PhotoNote;")
    fun selectAll(): List<PhotoNoteDto>

    @Query("SELECT * FROM PhotoNote WHERE id = :id")
    fun getById(id: Long) : PhotoNoteDto

    @Query("DELETE FROM PhotoNote WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM PhotoNote where uri = :uri;")
    fun selectByImageName(uri: String): PhotoNoteDto

}