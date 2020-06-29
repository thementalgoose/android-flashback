package tmg.flashback.circuit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_circuit_info.*
import kotlinx.android.synthetic.main.activity_circuit_info.back
import kotlinx.android.synthetic.main.activity_circuit_info.list
import kotlinx.android.synthetic.main.activity_circuit_info.titlebar
import kotlinx.android.synthetic.main.view_circuit_info.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.circuit.list.CircuitInfoAdapter
import tmg.flashback.race.RaceActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class CircuitInfoActivity: BaseActivity() {

    private val viewModel: CircuitInfoViewModel by viewModel()

    private lateinit var circuitId: String
    private var circuitName: String? = null
    private lateinit var adapter: CircuitInfoAdapter

    override fun layoutId(): Int = R.layout.activity_circuit_info

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        circuitId = bundle.getString(keyCircuit)!!
        circuitName = bundle.getString(keyCircuitName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = circuitName

        swipeContainer.isEnabled = false
        adapter = CircuitInfoAdapter(
            clickShowOnMap = viewModel.inputs::clickShowOnMap,
            clickWikipedia = viewModel.inputs::clickWikipedia,
            clickRace = {
                val raceIntent = RaceActivity.intent(
                    context = this,
                    season = it.season,
                    round = it.round,
                    circuitId = circuitId,
                    raceName = it.name,
                    trackName = circuitName,
                    date = it.date
                )
                startActivity(raceIntent)
            }
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        back.setOnClickListener {
            onBackPressed()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isLoading) {
            swipeContainer.isRefreshing = it
        }

        observe(viewModel.outputs.circuitName) {
            header.text = it
        }

        observeEvent(viewModel.outputs.showOnMap) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }

        viewModel.inputs.circuitId(circuitId)
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        list.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    companion object {

        private const val keyCircuit: String = "circuit"
        private const val keyCircuitName: String = "circuitName"

        fun intent(context: Context, circuitId: String, circuitName: String? = null): Intent {
            val intent = Intent(context, CircuitInfoActivity::class.java)
            intent.putExtra(keyCircuit, circuitId)
            intent.putExtra(keyCircuitName, circuitName)
            return intent
        }
    }
}