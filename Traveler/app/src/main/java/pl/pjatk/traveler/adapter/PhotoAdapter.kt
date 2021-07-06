package pl.pjatk.traveler.adapter

import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import pl.pjatk.traveler.MainActivity
import pl.pjatk.traveler.databinding.ElementPhotoBinding
import pl.pjatk.traveler.model.Photo


class PhotoVh(private val binding: ElementPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: Photo){
        with(binding){
            photoImageView.setImageBitmap(
                photo.photoBitmap
            )
        }
    }
}

class PhotoAdapter(private val activity: MainActivity) : RecyclerView.Adapter<PhotoVh>() {
    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var photoList: MutableList<Photo> = mutableListOf()
        set(value){
            field = value
            handler.post {
                notifyDataSetChanged()
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVh {
        val binding = ElementPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PhotoVh(binding).also {holder ->
            binding.root.setOnClickListener{
                activity.openShowActivity(photoList[holder.layoutPosition].id)
            }
        }
    }


    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: PhotoVh, position: Int) {
        holder.bind(photoList[position])
    }




}






























