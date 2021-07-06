package pl.pjatk.prm03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.pjatk.prm03.databinding.ActivityNewsDetailsBinding

class NewsClickedActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNewsDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        binding.newsDetailsTitle.text = intent.getStringExtra("title")
        binding.newsDetailsContent.text = intent.getStringExtra("description")
        binding.newsDetailsImage.setImageBitmap(intent.getParcelableExtra("img"))
    }

    fun close(view: View) {
        finish()
    }
}