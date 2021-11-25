package tmg.flashback.statistics.ui.overview.viewholders

import android.content.res.ColorStateList
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.statistics.databinding.ViewOverviewStatBinding
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonItem
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryItem
import tmg.utilities.extensions.views.context

class StatsViewHolder(
    private val binding: ViewOverviewStatBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DriverSummaryItem.Stat) {
        this.bindItem(item.tint, item.icon, item.label, item.value)
    }

    fun bind(item: DriverSeasonItem.Stat) {
        this.bindItem(item.tint, item.icon, item.label, item.value)
    }

    fun bind(item: ConstructorSummaryItem.Stat) {
        this.bindItem(item.tint, item.icon, item.label, item.value)
    }

    private fun bindItem(
            @AttrRes tint: Int,
            @DrawableRes drawable: Int,
            @StringRes label: Int,
            value: String
    ) {
        binding.icon.imageTintList = ColorStateList.valueOf(context.theme.getColor(tint))
        binding.icon.setImageResource(drawable)
        binding.label.setText(label)
        binding.value.text = value
    }
}