package tmg.flashback.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_settings_all_category.view.*
import tmg.flashback.R
import tmg.flashback.ui.utils.GenericDiffCallback

class SettingsAllAdapter(
    private val categoryClicked: (category: Category) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<Category> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_settings_all_category, parent, false), categoryClicked
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            this.text.setText(list[position].title)
        }
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(
        view: View,
        private val categoryClicked: (category: Category) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var category: Category

        init {
            itemView.container.setOnClickListener(this)
        }

        fun bind(item: Category) {
            this.category = item
            itemView.text.setText(item.title)
        }

        override fun onClick(p0: View?) {
            categoryClicked(category)
        }
    }
}

enum class Category(
    @StringRes val title: Int
) {
    RSS(R.string.settings_all_rss),
    ABOUT(R.string.settings_all_about),
}