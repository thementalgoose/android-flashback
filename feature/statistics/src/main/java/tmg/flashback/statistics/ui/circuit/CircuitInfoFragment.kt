package tmg.flashback.statistics.ui.circuit

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentCircuitInfoBinding
import tmg.flashback.statistics.ui.overview.constructor.ConstructorFragment
import tmg.flashback.statistics.ui.overview.constructor.ConstructorViewModel
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.statistics.ui.race.RaceData
import tmg.utilities.extensions.copyToClipboard
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.utils.ClipboardUtils.Companion.copyToClipboard

class CircuitInfoFragment: BaseFragment<FragmentCircuitInfoBinding>() {

    private val viewModel: CircuitInfoViewModel by viewModel()

    private lateinit var circuitId: String
    private lateinit var circuitName: String
    private lateinit var adapter: CircuitInfoAdapter

    override fun inflateView(inflater: LayoutInflater) =
            FragmentCircuitInfoBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            circuitId = it.getString(keyCircuit)!!
            circuitName = it.getString(keyCircuitName)!!
        }

        logScreenViewed("Circuit Overview", mapOf(
                "circuit_id" to circuitId,
                "circuit_name" to circuitName
        ))

        binding.titleExpanded.text = circuitName
        binding.titleCollapsed.text = circuitName

        binding.swipeRefresh.isEnabled = false
        adapter = CircuitInfoAdapter(
                clickShowOnMap = viewModel.inputs::clickShowOnMap,
                clickWikipedia = viewModel.inputs::clickWikipedia,
                clickRace = {
                    context?.let { context ->
                        val raceIntent = RaceActivity.intent(context, RaceData(
                            season = it.season,
                            round = it.round,
                            circuitId = circuitId,
                            defaultToRace = true,
                            country = "",
                            raceName = it.name,
                            trackName = circuitName,
                            countryISO = "",
                            date = it.date
                        ))
                        startActivity(raceIntent)
                    }
                }
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        binding.back.setOnClickListener {
            activity?.finish()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isLoading) {
            binding.swipeRefresh.isRefreshing = it
        }

        observe(viewModel.outputs.circuitName) {
            binding.titleExpanded.text = it
            binding.titleCollapsed.text = it
        }

        observeEvent(viewModel.outputs.goToMap) { (mapUri, coordinates) ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUri))
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context?.let {
                    copyToClipboard(it, "$circuitName - $coordinates")
                    Toast.makeText(it, getString(R.string.no_app_copy_clipboard), Toast.LENGTH_LONG).show()
                }
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

        fun instance(circuitId: String, circuitName: String): CircuitInfoFragment {
            return CircuitInfoFragment().apply {
                arguments = bundleOf(
                        keyCircuit to circuitId,
                        keyCircuitName to circuitName
                )
            }
        }
    }
}