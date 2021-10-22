package tmg.flashback.statistics.ui.circuit.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewCircuitInfoRaceBinding
import tmg.flashback.statistics.ui.circuit.CircuitItem
import tmg.utilities.extensions.views.getString

class RaceViewHolder(
        private val raceClicked: (race: CircuitItem.Race) -> Unit,
        private val binding: ViewCircuitInfoRaceBinding
): RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {

    init {
        binding.container.setOnClickListener(this)
    }

    private lateinit var race: CircuitItem.Race

    fun bind(circuit: CircuitItem.Race) {
        this.race = circuit
        binding.name.text = circuit.name
        binding.year.text = circuit.season.toString()
        binding.round.text = getString(R.string.race_round, circuit.round)
        binding.date.text = circuit.date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    }

    override fun onClick(p0: View?) {
        raceClicked(race)
    }
}