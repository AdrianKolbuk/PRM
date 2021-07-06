package pl.pjatk.managerfinansowy.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.pjatk.managerfinansowy.databinding.ItemFundBinding
import pl.pjatk.managerfinansowy.model.Fund

class FundVh(private val binding: ItemFundBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(fund: Fund){
        with(binding){
            place.text = fund.place
            amount.text = fund.amount.toString()
            date.text = fund.date.toString()
            category.text = fund.category
            option.text = fund.option
            if(binding.option.text.equals("Wydatek")){
                binding.image.setImageResource(android.R.drawable.presence_busy)
            } else
                binding.image.setImageResource(android.R.drawable.ic_input_add)
        }
    }
}

class FundAdapter(initList: MutableList<Fund>) : RecyclerView.Adapter<FundVh>() {
    var list: MutableList<Fund> = initList
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundVh {
        val binding = ItemFundBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return FundVh(binding).also {holder ->
            binding.root.setOnLongClickListener{
                var builder = AlertDialog.Builder(parent.context)
                builder.setMessage("Are you sure you want to delete it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _, _ -> remove(holder.layoutPosition) }
                        .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
                return@setOnLongClickListener true
            }
            binding.root.setOnClickListener{

            }
        }
    }

    fun refresh() = notifyDataSetChanged()

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FundVh, position: Int) {
        holder.bind(list[position])
    }

    private fun remove(position: Int): Boolean{
        if (position >= 0 && position < list.size){
            list.removeAt(position)
            notifyItemRemoved(position)
            return true
        }
        return false
    }

}