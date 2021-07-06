package pl.pjatk.prm03.adapter

import androidx.recyclerview.widget.RecyclerView
import pl.pjatk.prm03.model.ItemNews
import pl.pjatk.prm03.databinding.ItemNewsBinding

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(itemNews: ItemNews){
        with(binding){
            newsTitle.text = itemNews.title
            newsContent.text = itemNews.description
            newsImage.setImageBitmap(
                itemNews.img
            )
            if(itemNews.isRead)
                itemView.alpha = 0.5F
            else
                itemView.alpha = 1F
        }
    }
}