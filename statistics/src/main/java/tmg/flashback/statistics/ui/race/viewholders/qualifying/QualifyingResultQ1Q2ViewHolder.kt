package tmg.flashback.statistics.ui.race.viewholders.qualifying

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.RaceQualifyingRoundDriver
import tmg.flashback.formula1.model.RaceQualifyingType
import tmg.flashback.statistics.databinding.LayoutQualifyingTimeBinding
import tmg.flashback.statistics.databinding.ViewRaceQualifyingQ1q2ResultBinding
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show

class QualifyingResultQ1Q2ViewHolder(
    private val orderBy: (raceQualifyingType: RaceQualifyingType) -> Unit,
    private val binding: ViewRaceQualifyingQ1q2ResultBinding,
): RecyclerView.ViewHolder(binding.root) {

    init {

    }

    fun bind(model: RaceItem.QualifyingResultQ1Q2) {
        binding.apply {
            this.driver.bind(model.driver, model.qualified)

            bind(model.q1, this.layoutQ1, model.q1Delta, showDelta = false)
            bind(model.q2, this.layoutQ2, model.q2Delta, showDelta = false)
        }
    }

    private fun bind(result: RaceQualifyingRoundDriver.Qualifying?, layout: LayoutQualifyingTimeBinding?, delta: String?, showDelta: Boolean = false): Boolean {
        if (layout == null) return false
        val label = result?.lapTime?.toString() ?: ""
        layout.tvQualifyingTime.text = label

        if (showDelta) {
            layout.tvQualifyingDelta.show()
        }
        else {
            layout.tvQualifyingDelta.gone()
        }
        layout.tvQualifyingDelta.text = ""
        if (delta != null && result?.lapTime?.totalMillis != 0 && showDelta) {
            layout.tvQualifyingDelta.text = delta
        }
        return label.isNotEmpty()
    }
}