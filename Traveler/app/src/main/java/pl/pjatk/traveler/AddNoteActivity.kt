package pl.pjatk.traveler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.pjatk.traveler.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddNoteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.saveButton.setOnClickListener{
            val intent = Intent()
            if (binding.note.text.length > 500) {
                Toast.makeText(this, "Note cannot be longer than 500 characters", Toast.LENGTH_SHORT).show()
            } else{
                intent.putExtra("note",binding.note.text.toString())
                setResult(RESULT_OK,intent)
                finish()
            }
        }

    }
}