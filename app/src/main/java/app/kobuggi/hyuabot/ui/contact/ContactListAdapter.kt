package app.kobuggi.hyuabot.ui.contact

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.kobuggi.hyuabot.R
import app.kobuggi.hyuabot.databinding.ItemContactSearchBinding

class ContactListAdapter(private var result: List<ContactItem>, private val onClickItem: (ContactItem) -> Unit, private val onLongClickItem : (Int, Int) -> Unit) : RecyclerView.Adapter<ContactListAdapter.ContactItemViewHolder>() {
    private var selectedPosition = -1
    inner class ContactItemViewHolder(private val binding: ItemContactSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContactItem) {
            binding.searchResultName.text = item.name
            binding.searchResultPhone.text = item.phone
            binding.searchResultItem.setOnLongClickListener {
                setSelectedPosition(absoluteAdapterPosition)
                true
            }
            binding.searchResultItem.setOnClickListener {
                onClickItem(ContactItem(item.name, item.phone))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_search, parent, false)
        return ContactItemViewHolder(ItemContactSearchBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        holder.bind(result[position])
    }

    override fun getItemCount(): Int {
        return result.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setResult(result: List<ContactItem>) {
        this.result = result
        notifyDataSetChanged()
    }

    fun setSelectedPosition(adapterPosition: Int) {
        onLongClickItem(selectedPosition, adapterPosition)
        selectedPosition = adapterPosition
    }
}