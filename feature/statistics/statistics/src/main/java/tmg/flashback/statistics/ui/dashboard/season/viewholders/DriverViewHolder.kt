package tmg.flashback.statistics.ui.dashboard.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tmg.flashback.statistics.R
import tmg.flashback.shared.ui.model.AnimationSpeed
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.flashback.core.extensions.getColor
import tmg.flashback.statistics.databinding.ViewDashboardSeasonDriverBinding
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.show

class DriverViewHolder(
        val driverClicked: (driver: SeasonItem.Driver) -> Unit,
        private val binding: ViewDashboardSeasonDriverBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.container.setOnClickListener(this)
    }

    private lateinit var driver: SeasonItem.Driver

    private var qualiList: String? = null
    private var finishList: String? = null

    fun bind(item: SeasonItem.Driver) {

        driver = item

        binding.tvPosition.text = item.position.toString()
        binding.layoutDriver.tvName.text = item.driver.name
        binding.layoutDriver.tvNumber.show(false)
        binding.layoutDriver.imgFlag.show(false)

        Glide.with(itemView)
            .clear(binding.image)
        Glide.with(itemView)
            .load(item.driver.photoUrl)
            .into(binding.image)

        binding.image.setBackgroundColor(context.theme.getColor(R.attr.f1TextTertiary))
        binding.imgDriverFlag.setImageResource(itemView.context.getFlagResourceAlpha3(item.driver.nationalityISO))

        binding.tvDriverNumber.text = item.driver.number.toString()
        binding.tvConstructor.text = item.driver.constructorAtEndOfSeason.name

        binding.lpvProgress.backgroundColour = itemView.context.theme.getColor(R.attr.f1BackgroundPrimary)
        binding.lpvProgress.progressColour = item.driver.constructorAtEndOfSeason.color
        binding.lpvProgress.textBackgroundColour = context.theme.getColor(R.attr.f1TextSecondary)

        var maxProgress = item.points.toFloat() / item.maxPointsInSeason.toFloat()
        if (maxProgress.isNaN()) {
            maxProgress = 0.0f
        }

        when (item.animationSpeed) {
            AnimationSpeed.NONE -> {
                binding.lpvProgress.setProgress(maxProgress) { item.points.toString() }
            }
            else -> {
                binding.lpvProgress.timeLimit = item.animationSpeed.millis
                binding.lpvProgress.animateProgress(maxProgress) {
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
            binding.container -> {
                driverClicked(driver)
            }
        }
    }
}