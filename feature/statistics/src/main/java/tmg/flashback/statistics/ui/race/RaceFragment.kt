package tmg.flashback.statistics.ui.race

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.toEnum
import tmg.utilities.extensions.views.show

class RaceFragment: BaseFragment<FragmentRaceBinding>(), RaceAdapterCallback {

    private val viewModel: RaceViewModel by inject()

    private lateinit var raceData: RaceData

    private lateinit var raceAdapter: RaceAdapter
    private lateinit var linkAdapter: PillAdapter

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
        binding.rvContent.adapter = raceAdapter
        binding.rvContent.layoutManager = LinearLayoutManager(context)

        // Pill Adapter
        linkAdapter = PillAdapter(
            pillClicked = {
                when (it) {
                    is PillItem.Wikipedia -> viewModel.inputs.clickWikipedia()
                    is PillItem.Circuit -> {
                        context?.let { context ->
                            startActivity(CircuitInfoActivity.intent(context, raceData.circuitId, raceData.trackName))
                        } ?: crashManager.logError(RuntimeException("Context not available (Race, Circuit Pill)"), "RaceFragment, CircuitPill")
                    }
                    else -> {} /* Do nothing */
                }
            }
        )
        binding.links.adapter = linkAdapter
        binding.links.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        // Menu
        binding.menu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_qualifying -> {
                    viewModel.inputs.orderBy(RaceAdapterType.QUALIFYING_POS)
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

        binding.trackLayout.setOnClickListener {
            startActivity(CircuitInfoActivity.intent(it.context, raceData.circuitId, raceData.trackName))
        }



        observe(viewModel.outputs.showLinks) {
            linkAdapter.list = mutableListOf<PillItem>().apply {
                add(PillItem.Circuit())
                if (it) {
                    add(PillItem.Wikipedia(""))
                }
            }
        }

        observe(viewModel.outputs.showSprintQualifying) {
            binding.menu.menu.findItem(R.id.nav_sprint_qualifying).isVisible = it
        }

        observe(viewModel.outputs.seasonRoundData) { (season, round) ->
            binding.tvRoundInfo.text = getString(
                R.string.race_round_format,
                round.toString()
            ).fromHtml()
        }

        observe(viewModel.outputs.circuitInfo) {
            binding.imgCountry.setImageResource(binding.imgCountry.context.getFlagResourceAlpha3(it.circuit.countryISO))
            binding.tvCircuitName.text = "${it.circuit.name}\n${it.circuit.country}"
            binding.titleCollapsed.text = "${it.season} ${it.name}"
            binding.titleExpanded.text = "${it.name}\n${it.season} "
            binding.tvDate.text = it.date.format(DateTimeFormatter.ofPattern("dd MMMM"))
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

        observeEvent(viewModel.outputs.goToWikipedia) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }

        observeEvent(viewModel.outputs.showAppHintLongPress) {
//            Snackbar.make(binding.appbar, getString(R.string.app_hint_race_qualifying_long_click), appHintDelay)
//                .setAnchorView(binding.menu)
//                .show()
        }


        if (raceData.defaultToRace) {
            binding.menu.selectedItemId = R.id.nav_race
        } else {
            binding.menu.selectedItemId = R.id.nav_qualifying
        }

        setupInitialContent()

        viewModel.inputs.initialise(raceData.season, raceData.round, raceData.date)
    }

    private fun setupInitialContent() {
        binding.titleCollapsed.text = "${raceData.season} ${raceData.raceName}"
        binding.titleExpanded.text = "${raceData.raceName}\n${raceData.season}"

        binding.tvCircuitName.text = "${raceData.trackName}\n${raceData.country}"
        context?.let {
            if (raceData.countryISO.isEmpty()) {
                return@let
            }
            binding.imgCountry.setImageResource(it.getFlagResourceAlpha3(raceData.countryISO))
        }
        raceData.date?.let {
            binding.tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }

        @Suppress("RemoveExplicitTypeArguments")
        val track = TrackLayout.getTrack(raceData.circuitId, raceData.season, raceData.raceName)
        binding.trackLayout.show(track != null)
        if (track != null) {
            binding.trackLayout.setImageResource(track.icon)
        }
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

    //endregion
}