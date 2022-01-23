package tmg.flashback.statistics.ui.race.viewholders

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.compose.inject
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.statistics.databinding.LayoutPodiumBinding
import tmg.flashback.statistics.databinding.ViewRaceRacePodiumBinding
import tmg.flashback.statistics.extensions.bindRaceModel
import tmg.flashback.ui.animation.GlideProvider

class RacePodiumViewHolder(
        val driverClicked: (driver: Driver) -> Unit,
        private val binding: ViewRaceRacePodiumBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(first: RaceRaceResult, second: RaceRaceResult, third: RaceRaceResult) {
        bind(first, binding.layoutFirst, 1, binding.tvPoint1)
        bind(second, binding.layoutSecond, 2, binding.tvPoints2)
        bind(third, binding.layoutThird, 3, binding.tvPoints3)

        binding.layoutFirst.container.accessibilityTraversalBefore = binding.layoutSecond.root.id
        binding.layoutSecond.container.accessibilityTraversalBefore = binding.layoutThird.root.id
    }

    private fun bind(model: RaceRaceResult, layout: LayoutPodiumBinding, position: Int, pointsLayout: TextView) {
        layout.bindRaceModel(model, position, pointsLayout)

        layout.imgDriver.setOnClickListener {
            driverClicked(model.driver.driver)
        }
    }
}