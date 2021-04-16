package tmg.flashback.statistics.ui.circuit

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.statistics.R
import tmg.flashback.statistics.constants.ViewType
import tmg.flashback.statistics.constants.logEvent
import tmg.flashback.statistics.databinding.ActivityCircuitInfoBinding
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.utilities.extensions.copyToClipboard
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class CircuitInfoActivity: BaseActivity() {

    private lateinit var binding: ActivityCircuitInfoBinding
    private val viewModel: CircuitInfoViewModel by viewModel()

    override val screenAnalytics get() = ScreenAnalytics(
        screenName = "Circuit Overview",
        attributes = mapOf(
            "extra_circuit" to circuitId
        )
    )

    private lateinit var circuitId: String
    private lateinit var circuitName: String
    private lateinit var adapter: CircuitInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircuitInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            circuitId = it.getString(keyCircuit)!!
            circuitName = it.getString(keyCircuitName)!!

            analyticsController.logEvent(ViewType.CIRCUIT, mapOf(
                "circuit_id" to circuitId,
                "circuit_name" to circuitName
            ))
        }

        binding.header.text = circuitName

        binding.swipeContainer.isEnabled = false
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
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.back.setOnClickListener {
            onBackPressed()
        }


        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isLoading) {
            binding.swipeContainer.isRefreshing = it
        }

        observe(viewModel.outputs.circuitName) {
            binding.header.text = it
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