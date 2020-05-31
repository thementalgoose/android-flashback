package tmg.flashback.standings.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_race_race_result.view.imgDriverFlag
import kotlinx.android.synthetic.main.view_race_race_result.view.layoutDriver
import kotlinx.android.synthetic.main.view_race_race_result.view.tvPosition
import kotlinx.android.synthetic.main.view_standings_driver.view.*
import kotlinx.android.synthetic.main.view_standings_driver.view.lpvProgress
import tmg.flashback.R
import tmg.flashback.standings.StandingsItem
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.show

class StandingsDriverViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: StandingsItem.Driver, maxPoints: Int, position: Int) {

        itemView.tvPosition.text = position.toString()
        itemView.layoutDriver.tvName.text = item.driver.name
        itemView.layoutDriver.tvNumber.show(false)
        itemView.layoutDriver.imgFlag.show(false)

        Glide.with(itemView)
            .clear(itemView.image)
        Glide.with(itemView)
            .load(item.driver.photoUrl)
            .into(itemView.image)

        itemView.imgDriverFlag.setImageResource(itemView.context.getFlagResourceAlpha3(item.driver.nationalityISO))

        itemView.tvDriverNumber.text = item.driver.number.toString()
        itemView.tvConstructor.text = item.driver.constructorAtEndOfSeason.name

        itemView.lpvProgress.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundPrimary)
        itemView.lpvProgress.progressColour = item.driver.constructorAtEndOfSeason.color
        itemView.lpvProgress.animateProgress(item.points.toFloat() / maxPoints.toFloat()) { (it * maxPoints.toFloat()).toInt().coerceIn(0, item.points).toString() }
    }
}