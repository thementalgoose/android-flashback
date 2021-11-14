package tmg.flashback.statistics.ui.race.viewholders.qualifying

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.RaceQualifyingRoundDriver
import tmg.flashback.statistics.databinding.LayoutQualifyingTimeBinding
import tmg.flashback.statistics.databinding.ViewRaceQualifyingQ1ResultBinding
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show

class QualifyingResultQ1ViewHolder(
    private val binding: ViewRaceQualifyingQ1ResultBinding,
): RecyclerView.ViewHolder(binding.root) {

    init {

    }

    fun bind(model: RaceItem.QualifyingResultQ1) {
        binding.apply {
            this.driver.bind(model.driver, model.qualified)

            bind(model.q1, this.layoutQ1, model.q1Delta, showDelta = false)
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