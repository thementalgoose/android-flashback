package tmg.flashback.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.databinding.ViewSettingsAllCategoryBinding
import tmg.utilities.difflist.GenericDiffCallback

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
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ViewSettingsAllCategoryBinding.inflate(layoutInflater, parent, false), categoryClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(
        private val binding: ViewSettingsAllCategoryBinding,
        private val categoryClicked: (category: Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var category: Category

        init {
            binding.container.setOnClickListener(this)
        }

        fun bind(item: Category) {
            this.category = item
            binding.text.setText(item.title)
            binding.subtext.setText(item.subtitle)
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
    );
}
