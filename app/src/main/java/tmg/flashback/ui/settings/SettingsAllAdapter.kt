package tmg.flashback.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_settings_all_category.view.*
import tmg.flashback.R
import tmg.flashback.core.ui.shared.GenericDiffCallback

class SettingsAllAdapter(
    private val categoryClicked: (category: Category) -> Unit
) : RecyclerView.Adapter<SettingsAllAdapter.ViewHolder>() {

    var list: List<Category> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_settings_all_category, parent, false), categoryClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
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
            itemView.subtext.setText(item.subtitle)
        }

        override fun onClick(p0: View?) {
            categoryClicked(category)
        }
    }
}

enum class Category(
    @StringRes val title: Int,
    @StringRes val subtitle: Int
) {
    CUSTOMISATION(
            title = R.string.settings_all_customisation,
            subtitle = R.string.settings_all_customisation_subtitle
    ),
    STATISTICS(
            title = R.string.settings_all_statistics,
            subtitle = R.string.settings_all_statistics_subtitle
    ),
    RSS(
            title = R.string.settings_all_rss,
            subtitle = R.string.settings_all_rss_subtitle
    ),
    NOTIFICATIONS(
            title = R.string.settings_all_notifications,
            subtitle = R.string.settings_all_notifications_subtitle
    ),
    WIDGETS(
            title = R.string.settings_all_widgets,
            subtitle = R.string.settings_all_widgets_subtitle
    ),
    DEVICE(
            title = R.string.settings_all_device,
            subtitle = R.string.settings_all_device_subtitle
    ),
    ABOUT(
            title = R.string.settings_all_about,
            subtitle = R.string.settings_all_about_subtitle
    ),
}
