package tmg.f1stats.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_text.view.*
import tmg.f1stats.R

class TextAdapter: RecyclerView.Adapter<TextAdapter.ViewHolder>() {

    var list: List<Selected<String>> = listOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(TextDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    var itemClicked: (value: String) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_text, parent, false), itemClicked)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(
        view: View,
        val itemClicked: (value: String) -> Unit
    ): RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var value: String

        init {
            itemView.tvSelection.setOnClickListener(this)
        }

        fun bind(item: Selected<String>) {
            itemView.tvSelection.isSelected = item.isSelected
            itemView.tvSelection.text = item.value
            this.value = item.value
        }

        override fun onClick(p0: View?) {
            itemClicked(value)
        }
    }

    inner class TextDiffCallback(
        private val oldList: List<Selected<String>>,
        private val newList: List<Selected<String>>
    ): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newItemPosition)
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }
}