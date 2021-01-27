package tmg.flashback.ui.circuit

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_circuit_info.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.race.RaceActivity
import tmg.utilities.extensions.copyToClipboard
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class CircuitInfoActivity: BaseActivity() {

    private val viewModel: CircuitInfoViewModel by viewModel()

    override val analyticsScreenName: String
        get() = "Circuit Overview"
    override val analyticsCustomAttributes: Map<String, String>
        get() = mapOf(
                "extra_circuit" to circuitId
        )

    private lateinit var circuitId: String
    private lateinit var circuitName: String
    private lateinit var adapter: CircuitInfoAdapter

    override fun layoutId(): Int = R.layout.activity_circuit_info

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        circuitId = bundle.getString(keyCircuit)!!
        circuitName = bundle.getString(keyCircuitName)!!
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

        observeEvent(viewModel.outputs.goToMap) { (mapUri, coordinates) ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUri))
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                copyToClipboard("$circuitName - $coordinates")
                Toast.makeText(this, getString(R.string.no_app_copy_clipboard), Toast.LENGTH_LONG).show()
            }
        }

        observeEvent(viewModel.outputs.goToWikipediaPage) {
            if (it.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }

        viewModel.inputs.circuitId(circuitId)
    }

    companion object {

        private const val keyCircuit: String = "circuit"
        private const val keyCircuitName: String = "circuitName"

        fun intent(context: Context, circuitId: String, circuitName: String): Intent {
            val intent = Intent(context, CircuitInfoActivity::class.java)
            intent.putExtra(keyCircuit, circuitId)
            intent.putExtra(keyCircuitName, circuitName)
            return intent
        }
    }
}