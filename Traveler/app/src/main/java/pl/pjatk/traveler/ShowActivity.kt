package pl.pjatk.traveler

import android.graphics.ImageDecoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import pl.pjatk.traveler.databinding.ActivityShowBinding
import pl.pjatk.traveler.model.Shared
import kotlin.concurrent.thread

class ShowActivity : AppCompatActivity() {
    private val binding by lazy { ActivityShowBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id"))
            fillWithData(intent.extras?.get("id") as Long)
    }

    private fun fillWithData(i: Long) {
        thread {
            Shared.db?.photoNote?.getById(i).let {
                this.runOnUiThread{
                    if (it != null) {
                        binding.imageView.setImageBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, it.uri.toUri())))
                        binding.note.text = it.note
                    }
                }
            }
        }
    }
}