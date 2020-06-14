package tmg.flashback.home.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_home_constructor.view.*
import kotlinx.android.synthetic.main.view_home_driver.view.*
import kotlinx.android.synthetic.main.view_home_driver.view.lpvProgress
import tmg.flashback.R
import tmg.flashback.extensions.ordinalAbbreviation
import tmg.flashback.home.list.HomeItem
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class DriverViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: HomeItem.Driver) {

        itemView.tvPosition.text = item.position.toString()
        itemView.layoutDriver.tvName.text = item.driver.name
        itemView.layoutDriver.tvNumber.show(false)
        itemView.layoutDriver.imgFlag.show(false)

        Glide.with(itemView)
            .clear(itemView.image)
        Glide.with(itemView)
            .load(item.driver.photoUrl)
            .into(itemView.image)

        itemView.image.setBackgroundColor(context.theme.getColor(R.attr.f1TextTertiary))
        itemView.imgDriverFlag.setImageResource(itemView.context.getFlagResourceAlpha3(item.driver.nationalityISO))

        itemView.tvDriverNumber.text = item.driver.number.toString()
        itemView.tvConstructor.text = item.driver.constructorAtEndOfSeason.name

        itemView.lpvProgress.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundPrimary)
        itemView.lpvProgress.progressColour = item.driver.constructorAtEndOfSeason.color
        itemView.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.f1TextSecondary)
        itemView.lpvProgress.animateProgress(item.points.toFloat() / item.maxPointsInSeason.toFloat()) { (it * item.maxPointsInSeason.toFloat()).toInt().coerceIn(0, item.points).toString() }

        itemView.best.show(item.bestFinish != null || item.bestQualifying != null)
        itemView.best.text = StringBuilder().apply {
            if (item.bestFinish != null ) {
                val (position, circuits) = item.bestFinish
                val circuitsString = if (circuits.size > 3) {
                    getString(R.string.home_driver_grand_prixs, circuits.size)
                } else {
                    circuits.joinToString(separator = ", ") { it.country }
                }
                append(itemView.context.getString(R.string.home_driver_finish_pole, position.ordinalAbbreviation, circuitsString))
            }
            if (isNotEmpty()) append("<br/>")
            if (item.bestQualifying != null) {
                val (position, circuits) = item.bestQualifying
                val circuitsString = if (circuits.size > 3) {
                    getString(R.string.home_driver_grand_prixs, circuits.size)
                } else {
                    circuits.joinToString(separator = ", ") { it.country }
                }
                val qualiPos = if (position == 1) {
                    getString(R.string.on_pole_position)
                } else {
                    position.ordinalAbbreviation
                }
                append(itemView.context.getString(R.string.home_driver_qualified, qualiPos, circuitsString))
            }
        }.toString().fromHtml()
    }
}