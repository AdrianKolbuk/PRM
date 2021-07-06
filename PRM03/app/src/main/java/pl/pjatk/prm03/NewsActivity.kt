package pl.pjatk.prm03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import pl.pjatk.prm03.adapter.NewsAdapter
import pl.pjatk.prm03.databinding.ActivityNewsBinding
import pl.pjatk.prm03.shared.CurrentUser

class NewsActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityNewsBinding.inflate(layoutInflater)
    }

    private val newsAdapter by lazy {
        NewsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.news.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@NewsActivity)
        }
        binding.welcomeText.text = "Hello " + CurrentUser.username
        newsAdapter.refresh()
    }

    override fun onResume() {
        super.onResume()
        newsAdapter.refresh()
    }

    fun logout(view: View){
        finish()
    }

}