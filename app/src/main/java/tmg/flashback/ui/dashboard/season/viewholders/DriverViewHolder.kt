package tmg.flashback.ui.dashboard.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_dashboard_season_driver.view.*
import tmg.flashback.R
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.ui.dashboard.season.SeasonItem
import tmg.flashback.ui.utils.getColor
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.show

class DriverViewHolder(
        val driverClicked: (driver: SeasonItem.Driver) -> Unit,
        itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    private lateinit var driver: SeasonItem.Driver

    private var qualiList: String? = null
    private var finishList: String? = null

    fun bind(item: SeasonItem.Driver) {

        driver = item

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

        var maxProgress = item.points.toFloat() / item.maxPointsInSeason.toFloat()
        if (maxProgress.isNaN()) {
            maxProgress = 0.0f
        }

        when (item.barAnimation) {
            BarAnimation.NONE -> {
                itemView.lpvProgress.setProgress(maxProgress) { item.points.toString() }
            }
            else -> {
                itemView.lpvProgress.timeLimit = item.barAnimation.millis
                itemView.lpvProgress.animateProgress(maxProgress) {
                    when (it) {
                        maxProgress -> item.points.toString()
                        0.0f -> "0"
                        else -> (it * item.maxPointsInSeason.toFloat()).toInt().coerceIn(0, item.points).toString()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            itemView.container -> {
                driverClicked(driver)
            }
        }
    }
}