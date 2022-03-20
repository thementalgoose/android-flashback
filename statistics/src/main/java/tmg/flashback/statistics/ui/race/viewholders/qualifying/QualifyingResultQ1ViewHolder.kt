package tmg.flashback.statistics.ui.race.viewholders.qualifying

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.RaceQualifyingResult
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutQualifyingTimeBinding
import tmg.flashback.statistics.databinding.ViewRaceQualifyingQ1ResultBinding
import tmg.flashback.statistics.ui.race.RaceItem
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show

class QualifyingResultQ1ViewHolder(
    private val driverClicked: (driver: Driver) -> Unit,
    private val binding: ViewRaceQualifyingQ1ResultBinding,
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var driver: DriverConstructor

    init {
        binding.driver.qualiOverviewContainer.setOnClickListener(this)
    }

    fun bind(model: RaceItem.QualifyingResultQ1) {
        this.driver = model.driver
        binding.apply {
            this.driver.bind(model.driver, model.qualified)

            bind(model.q1, this.layoutQ1, model.q1Delta, showDelta = false)
        }
    }

    private fun bind(result: RaceQualifyingResult?, layout: LayoutQualifyingTimeBinding?, delta: String?, showDelta: Boolean = false): Boolean {
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

        // Accessibility
        result?.lapTime?.contentDescription?.let {
            layout.qualiTimeContainer.contentDescription = getString(R.string.ab_qualifying_time, "Q1", it)
        }

        return label.isNotEmpty()
    }

    override fun onClick(v: View?) {
        driverClicked(driver.driver)
    }
}