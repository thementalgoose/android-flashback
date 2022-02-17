package tmg.flashback.statistics.ui.race

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentRaceBinding
import tmg.flashback.statistics.ui.circuit.CircuitActivity
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.race.RaceDisplayType.*
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewWebpage
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible
import tmg.utilities.lifecycle.viewInflateBinding

class RaceFragment: BaseFragment() {

    private val viewModel: RaceViewModel by inject()
    private val binding by viewInflateBinding(FragmentRaceBinding::inflate)

    private lateinit var raceData: RaceData

    private lateinit var raceAdapter: RaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            raceData = it.getParcelable(keyRaceData)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Race Overview", mapOf(
            "season" to raceData.season.toString(),
            "round" to raceData.round.toString()
        ))

        raceAdapter = RaceAdapter(
            pillItemClicked = { pillItem ->
                when (pillItem) {
                    is PillItem.Circuit -> {
                        context?.let {
                            startActivity(CircuitActivity.intent(it, pillItem.circuitId, pillItem.circuitName))
                        }
                    }
                    is PillItem.Wikipedia -> {
                        viewWebpage(pillItem.link)
                    }
                    is PillItem.Youtube -> {
                        viewWebpage(pillItem.link)
                    }
                    else -> { /* Do nothing */ }
                }
            },
            driverClicked = viewModel.inputs::clickDriver,
            constructorClicked = viewModel.inputs::clickConstructor
        )
        binding.dataList.adapter = raceAdapter
        binding.dataList.layoutManager = LinearLayoutManager(context)
        binding.progress.invisible()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.inputs.refresh()
            binding.progress.visible()
        }

        raceAdapter.list = listOf(
            RaceItem.Overview(
                raceName = raceData.raceName,
                country = raceData.country,
                countryISO = raceData.countryISO,
                circuitId = raceData.circuitId,
                circuitName = raceData.trackName,
                round = raceData.round,
                season = raceData.season,
                raceDate = raceData.date,
                wikipedia = null,
                laps = null,
                youtube = null,
                schedule = emptyList()
            )
        )
        // Menu
        binding.menu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_qualifying -> {
                    viewModel.inputs.displayType(QUALIFYING)
                    true
                }
                R.id.nav_sprint_qualifying -> {
                    viewModel.inputs.displayType(QUALIFYING_SPRINT)
                    true
                }
                R.id.nav_race -> {
                    viewModel.inputs.displayType(RACE)
                    true
                }
                R.id.nav_constructor -> {
                    viewModel.inputs.displayType(CONSTRUCTOR)
                    true
                }
                else -> false
            }
        }
        binding.back.setOnClickListener { activity?.finish() }

        observe(viewModel.outputs.showSprintQualifying) {
            binding.menu.menu.findItem(R.id.nav_sprint_qualifying).isVisible = it
        }

        observe(viewModel.outputs.list) {
            raceAdapter.list = it
        }

        observe(viewModel.outputs.showLoading) {
            if (it) {
                binding.progress.visible()
            } else {
                binding.swipeRefresh.isRefreshing = false
                binding.progress.invisible()
            }
        }

        observeEvent(viewModel.outputs.goToConstructor) { constructor ->
            context?.let {
                startActivity(ConstructorActivity.intent(it, constructor.id, constructor.name))
            } ?: crashManager.logError(RuntimeException("Context not available (race go to constructor overview)"), "RaceFragment, goToConstructorOverview")
        }

        observeEvent(viewModel.outputs.goToDriver) { driver ->
            context?.let {
                startActivity(DriverActivity.intent(it, driver.id, driver.name))
            } ?: crashManager.logError(RuntimeException("Context not available (race go to driver overview)"), "RaceFragment, goToDriverOverview")
        }

        if (raceData.defaultToRace) {
            binding.menu.selectedItemId = R.id.nav_race
        } else {
            binding.menu.selectedItemId = R.id.nav_qualifying
        }

        setupInitialContent()
        viewModel.inputs.initialise(raceData.season, raceData.round)
    }

    @SuppressLint("SetTextI18n")
    private fun setupInitialContent() {
        binding.titleCollapsed.text = "${raceData.season} ${raceData.raceName}"
        binding.titleExpanded.text = "${raceData.raceName}\n${raceData.season}"
    }

    companion object {

        private const val keyRaceData: String = "keyRaceData"

        fun instance(raceData: RaceData): RaceFragment {
            return RaceFragment().apply {
                arguments = bundleOf(
                    keyRaceData to raceData
                )
            }
        }
    }
}