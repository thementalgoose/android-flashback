package tmg.flashback.home.list.viewholders

import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_home_driver.view.*
import kotlinx.android.synthetic.main.view_home_driver.view.lpvProgress
import tmg.flashback.R
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.showDriverSummary
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class DriverViewHolder(
        val driverClicked: (season: Int, driverId: String, firstName: String?, lastName: String?) -> Unit,
        itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
        itemView.containerQuali.setOnClickListener(this)
        itemView.containerFinish.setOnClickListener(this)
        itemView.containerMore.setOnClickListener(this)
    }

    private lateinit var driverId: String
    private var season: Int = -1
    private var firstName: String? = null
    private var lastName: String? = null

    private var qualiList: String? = null
    private var finishList: String? = null

    fun bind(item: HomeItem.Driver) {

        driverId = item.driverId
        season = item.season
        firstName = item.driver.firstName
        lastName = item.driver.lastName

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
        when (item.barAnimation) {
            BarAnimation.NONE -> {
                itemView.lpvProgress.setProgress(item.points.toFloat() / item.maxPointsInSeason.toFloat()) { (it * item.maxPointsInSeason.toFloat()).toInt().coerceIn(0, item.points).toString() }
            }
            else -> {
                itemView.lpvProgress.timeLimit = item.barAnimation.millis
                itemView.lpvProgress.animateProgress(item.points.toFloat() / item.maxPointsInSeason.toFloat()) { (it * item.maxPointsInSeason.toFloat()).toInt().coerceIn(0, item.points).toString() }
            }
        }

        itemView.stats.show(false)

        // Qualifying
        val (qualiPosition, qualiCircuits) = item.bestQualifying ?: Pair(0, emptyList())
        if (qualiPosition == 0) {
            itemView.qualifying.text = getString(R.string.endash)
            qualiList = null
        }
        else {
            itemView.qualifying.text = qualiPosition.ordinalAbbreviation
            qualiList = qualiCircuits
                    .map { it.name }
                    .distinct()
                    .joinToString(separator = ", ")
        }

        // Finish
        val (position, circuits) = item.bestFinish ?: Pair(0, emptyList())
        if (position == 0) {
            itemView.finish.text = getString(R.string.endash)
            finishList = null
        }
        else {
            itemView.finish.text = position.ordinalAbbreviation
            finishList = circuits
                    .map { it.name }
                    .distinct()
                    .joinToString(separator = ", ")
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            itemView.container -> {
                @Suppress("ConstantConditionIf")
                if (showDriverSummary) {
                    itemView.stats.show(!itemView.stats.isVisible)
                }
                else {
                    driverClicked(season, driverId, firstName, lastName)
                }
            }
            itemView.containerQuali -> {
                if (qualiList != null) {
                    Toast.makeText(context, qualiList, Toast.LENGTH_LONG).show()
                }
            }
            itemView.containerFinish -> {
                if (finishList != null) {
                    Toast.makeText(context, finishList, Toast.LENGTH_LONG).show()
                }
            }
            itemView.containerMore -> {
                driverClicked(season, driverId, firstName, lastName)
            }
        }
    }
}