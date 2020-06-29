package tmg.flashback.circuit.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_circuit_race.view.*
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.circuit.list.CircuitItem
import tmg.utilities.extensions.views.getString

class CircuitRaceViewHolder(
    private val raceClicked: (race: CircuitItem.Race) -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    private lateinit var race: CircuitItem.Race

    fun bind(circuit: CircuitItem.Race) {
        this.race = circuit
        itemView.name.text = circuit.name
        itemView.year.text = circuit.season.toString()
        itemView.round.text = getString(R.string.race_round, circuit.round)
        itemView.date.text = circuit.date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    }

    override fun onClick(p0: View?) {
        raceClicked(race)
    }
}