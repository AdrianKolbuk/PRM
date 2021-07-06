package pl.pjatk.prm03.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.pjatk.prm03.NewsClickedActivity

import pl.pjatk.prm03.databinding.ItemNewsBinding
import pl.pjatk.prm03.shared.News

class NewsAdapter : RecyclerView.Adapter<NewsViewHolder>(){
    private val newsList = News.newsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding).also { holder ->
            binding.root.setOnClickListener {
                val showClickedNewsIntent = Intent(parent.context, NewsClickedActivity::class.java)
                showClickedNewsIntent.putExtra("title", newsList[holder.layoutPosition].title)
                showClickedNewsIntent.putExtra("description", newsList[holder.layoutPosition].description)
                showClickedNewsIntent.putExtra("img", newsList[holder.layoutPosition].img)
                newsList[holder.layoutPosition].isRead = true
                parent.context.startActivity(showClickedNewsIntent)
            }
        }
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    fun refresh() = notifyDataSetChanged()
}