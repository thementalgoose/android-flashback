package tmg.flashback.statistics.ui.dashboard.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewDashboardSeasonDriverBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.flashback.ui.animation.GlideProvider
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.ui.model.AnimationSpeed
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show
import kotlin.math.roundToInt

private val glideProvider: tmg.flashback.ui.animation.GlideProvider =
    tmg.flashback.ui.animation.GlideProvider()

class DriverViewHolder(
    val driverClicked: (driver: SeasonItem.Driver) -> Unit,
    private val binding: ViewDashboardSeasonDriverBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.container.setOnClickListener(this)
    }

    private lateinit var driver: SeasonItem.Driver

    fun bind(item: SeasonItem.Driver) {
        driver = item

        binding.tvPosition.text = item.position?.toString() ?: "-"
        binding.layoutDriver.tvName.text = item.driver.name
        binding.layoutDriver.tvNumber.show(false)
        binding.layoutDriver.imgFlag.show(false)

        glideProvider.clear(binding.image)
        glideProvider.load(binding.image, item.driver.photoUrl)

        binding.image.setBackgroundColor(context.theme.getColor(R.attr.contentTertiary))
        binding.imgDriverFlag.setImageResource(itemView.context.getFlagResourceAlpha3(item.driver.nationalityISO))

        binding.tvDriverNumber.text = item.driver.number?.toString() ?: ""
        binding.tvDriverNumber.colorHighlight = item.constructors.lastOrNull()?.color ?: context.theme.getColor(R.attr.colorPrimary)
        binding.tvConstructor.text = item.constructors.joinToString(separator = ", ") { it.name }

        binding.lpvProgress.backgroundColour = itemView.context.theme.getColor(R.attr.backgroundPrimary)
        // TODO: Look at a way of representing multiple constructors on the driver overview board
        binding.lpvProgress.progressColour = item.constructors.firstOrNull()?.color ?: context.theme.getColor(R.attr.colorPrimary)
        binding.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.contentSecondary)

        var maxProgress = item.points.toFloat() / item.maxPointsInSeason.toFloat()
        if (maxProgress.isNaN()) {
            maxProgress = 0.0f
        }

        when (item.animationSpeed) {
            AnimationSpeed.NONE -> {
                binding.lpvProgress.setProgress(maxProgress) { item.points.pointsDisplay() }
            }
            else -> {
                binding.lpvProgress.timeLimit = item.animationSpeed.millis
                binding.lpvProgress.animateProgress(maxProgress) {
                    when (it) {
                        maxProgress -> item.points.pointsDisplay()
                        0.0f -> "0"
                        else -> (it * item.maxPointsInSeason.toFloat())
                            .coerceIn(0.0f, item.points.toFloat())
                            .roundToInt()
                            .toString()
                    }
                }
            }
        }

        // Accessibility
        val constructors = item.constructors
            .joinToString(separator = " and ") { it.name }
        val contentDescription = getString(
            R.string.ab_season_driver,
            item.driver.name,
            constructors,
            when (item.position) {
                0 -> getString(R.string.ab_season_unclassified)
                null -> getString(R.string.ab_season_unclassified)
                else -> "P${item.position}"
            },
            item.points
        )
        binding.container.contentDescription = contentDescription
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.container -> {
                driverClicked(driver)
            }
        }
    }
}