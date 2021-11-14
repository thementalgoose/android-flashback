package tmg.flashback.statistics.ui.circuit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentCircuitInfoBinding
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.statistics.ui.race.RaceData
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible
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

        adapter = CircuitInfoAdapter(
                clickShowOnMap = viewModel.inputs::clickShowOnMap,
                clickLink = viewModel.inputs::clickLink,
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
                        )
                        )
                        startActivity(raceIntent)
                    }
                }
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)
        binding.progress.invisible()

        binding.back.setOnClickListener {
            activity?.finish()
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.progress.visible()
            viewModel.inputs.refresh()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.showLoading) {
            if (it) {
                binding.progress.visible()
            } else {
                binding.swipeRefresh.isRefreshing = false
                binding.progress.invisible()
            }
        }

        observe(viewModel.outputs.circuitName) {
            binding.titleExpanded.text = it
            binding.titleCollapsed.text = it
        }

        observeEvent(viewModel.outputs.goToMap) { (mapUri, coordinates) ->
            if (!viewUrl(mapUri)) {
                context?.let {
                    copyToClipboard(it, "$circuitName - $coordinates")
                    Toast.makeText(it, getString(R.string.no_app_copy_clipboard), Toast.LENGTH_LONG).show()
                }
            }
        }

        observeEvent(viewModel.outputs.goToLink) {
            if (it.isNotEmpty()) {
                viewUrl(it)
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