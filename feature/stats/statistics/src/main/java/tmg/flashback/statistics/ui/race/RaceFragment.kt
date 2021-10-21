package tmg.flashback.statistics.ui.race

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.core.ui.base.BaseFragment
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentRaceBinding
import tmg.flashback.statistics.ui.circuit.CircuitInfoActivity
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.utilities.extensions.*
import tmg.utilities.extensions.views.show

class RaceFragment: BaseFragment<FragmentRaceBinding>(), RaceAdapterCallback {

    private val viewModel: RaceViewModel by inject()

    private lateinit var raceData: RaceData

    private lateinit var raceAdapter: RaceAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentRaceBinding
        .inflate(inflater)

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

        binding.swipeRefresh.isEnabled = false

        // Race content
        raceAdapter = RaceAdapter(this)
        binding.dataList.adapter = raceAdapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        // Initial data
        raceAdapter.list = listOf(
            RaceModel.Overview(
                raceName = raceData.raceName,
                country = raceData.country,
                countryISO = raceData.countryISO,
                circuitId = raceData.circuitId,
                circuitName = raceData.trackName,
                round = raceData.round,
                season = raceData.season,
                raceDate = raceData.date,
                wikipedia = null
            )
        )

        // Menu
        binding.menu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_qualifying -> {
                    viewModel.inputs.orderBy(RaceAdapterType.QUALIFYING_POS)
                    true
                }
                R.id.nav_sprint_qualifying -> {
                    viewModel.inputs.orderBy(RaceAdapterType.QUALIFYING_SPRINT)
                    true
                }
                R.id.nav_race -> {
                    viewModel.inputs.orderBy(RaceAdapterType.RACE)
                    true
                }
                R.id.nav_constructor -> {
                    viewModel.inputs.orderBy(RaceAdapterType.CONSTRUCTOR_STANDINGS)
                    true
                }
                else -> false
            }
        }
        binding.back.setOnClickListener { activity?.finish() }

        observe(viewModel.outputs.showSprintQualifying) {
            binding.menu.menu.findItem(R.id.nav_sprint_qualifying).isVisible = it
        }

        observe(viewModel.outputs.raceItems) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        observeEvent(viewModel.outputs.goToDriverOverview) { (driverId, driverName) ->
            context?.let {
                startActivity(DriverActivity.intent(it, driverId, driverName))
            } ?: crashManager.logError(RuntimeException("Context not available (race go to driver overview)"), "RaceFragment, goToDriverOverview")
        }

        observeEvent(viewModel.outputs.goToConstructorOverview) { (constructorId, constructorName) ->
            context?.let {
                startActivity(ConstructorActivity.intent(it, constructorId, constructorName))
            } ?: crashManager.logError(RuntimeException("Context not available (race go to constructor overview)"), "RaceFragment, goToConstructorOverview")
        }

        if (raceData.defaultToRace) {
            binding.menu.selectedItemId = R.id.nav_race
        } else {
            binding.menu.selectedItemId = R.id.nav_qualifying
        }

        setupInitialContent()

        viewModel.inputs.initialise(raceData.season, raceData.round, raceData.date)
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

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        viewModel.inputs.orderBy(adapterType)
    }

    override fun driverClicked(driverId: String, driverName: String) {
        viewModel.inputs.goToDriver(driverId, driverName)
    }

    override fun constructorClicked(constructorId: String, constructorName: String) {
        viewModel.inputs.goToConstructor(constructorId, constructorName)
    }

    override fun toggleQualifyingDeltas(toNewState: Boolean) {
        viewModel.inputs.toggleQualifyingDelta(toNewState)
    }

    override fun pillClicked(pillItem: PillItem) {
        when (pillItem) {
            is PillItem.Circuit -> {
                context?.let {
                    startActivity(CircuitInfoActivity.intent(it, pillItem.circuitId, pillItem.circuitName))
                }
            }
            is PillItem.Wikipedia -> {
                viewWebpage(pillItem.link)
            }
            else -> { /* Do nothing */ }
        }
    }

    //endregion
}