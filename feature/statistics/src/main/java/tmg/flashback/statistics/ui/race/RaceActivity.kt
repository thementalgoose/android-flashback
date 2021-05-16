package tmg.flashback.statistics.ui.race

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.core.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityRaceBinding
import tmg.flashback.statistics.ui.circuit.CircuitInfoActivity
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.shared.pill.PillAdapter
import tmg.flashback.statistics.ui.shared.pill.PillItem
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.toEnum
import tmg.utilities.extensions.views.show

private const val dateFormat: String = "yyyy/MM/dd"

class RaceActivity : BaseActivity(), RaceAdapterCallback {

    private lateinit var binding: ActivityRaceBinding
    private val viewModel: RaceViewModel by inject()

//    override val screenAnalytics get() = ScreenAnalytics(
//        screenName = "Race Results",
//        attributes = mapOf(
//            "race_season" to "$season",
//            "race_round" to "$round"
//        )
//    )

    private lateinit var raceAdapter: RaceAdapter
    private lateinit var linkAdapter: PillAdapter

    var season: Int = -1
    var round: Int = -1
    lateinit var circuitId: String

    private var defaultToRace: Boolean = true

    private var initialCountry: String = ""
    private var initialCountryISO: String = ""
    private var initialRaceName: String = ""
    private var initialTrackName: String = ""
    private var initialDate: LocalDate? = null

    companion object {

        private const val keySeason: String = "season"
        private const val keyRound: String = "round"
        private const val keyCircuitId: String = "circuit"

        private const val keyDefaultToRace: String = "orderType"

        private const val keyCountry: String = "keyCountry"
        private const val keyRaceName: String = "keyRaceName"
        private const val keyTrackName: String = "keyTrackName"
        private const val keyCountryISO: String = "keyCountryISO"
        private const val keyDate: String = "keyDate"

        fun intent(
                context: Context,
                season: Int,
                round: Int,
                circuitId: String,
                defaultToRace: Boolean = true,
                country: String? = null,
                raceName: String? = null,
                trackName: String? = null,
                countryISO: String? = null,
                date: LocalDate? = null
        ): Intent {
            return Intent(context, RaceActivity::class.java).apply {
                putExtra(keySeason, season)
                putExtra(keyRound, round)
                putExtra(keyCircuitId, circuitId)
                putExtra(keyDefaultToRace, defaultToRace)
                putExtra(keyCountry, country)
                putExtra(keyRaceName, raceName)
                putExtra(keyTrackName, trackName)
                putExtra(keyCountryISO, countryISO)
                putExtra(keyDate, date?.format(DateTimeFormatter.ofPattern(dateFormat)))
            }
        }
    }

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            season = it.getInt(keySeason)
            round = it.getInt(keyRound)
            circuitId = it.getString(keyCircuitId)!!

            logScreenViewed("Race Results", mapOf(
                "race_season" to season.toString(),
                "race_round" to round.toString(),
            ))

            defaultToRace = it.getBoolean(keyDefaultToRace)

            initialCountry = it.getString(keyCountry, "")
            initialCountryISO = it.getString(keyCountryISO, "")
            initialRaceName = it.getString(keyRaceName, "")
            initialTrackName = it.getString(keyTrackName, "")
            val date = it.getString(keyDate, "")
            if (date.isNotEmpty()) {
                initialDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat))
            }

            viewModel.inputs.initialise(season, round, initialDate)
        }



        raceAdapter = RaceAdapter(this)
        binding.rvContent.adapter = raceAdapter
        binding.rvContent.layoutManager = LinearLayoutManager(this)

        binding.grandprix.text = "$season $initialRaceName"
        binding.tvCircuitName.text = "$initialTrackName\n$initialCountry"
        if (initialCountryISO.isNotEmpty()) {
            binding.imgCountry.setImageResource(getFlagResourceAlpha3(initialCountryISO))
        }
        initialDate?.let {
            binding.tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }

        @Suppress("RemoveExplicitTypeArguments")
        val track = TrackLayout.getOverride(season, initialRaceName) ?: circuitId.toEnum<TrackLayout> { it.circuitId }
        binding.trackLayout.show(track != null)
        if (track != null) {
            binding.trackLayout.setImageResource(track.icon)
        }

        /**
         * Setup the links at the top of the file
         */
        linkAdapter = PillAdapter(
                pillClicked = {
                    when (it) {
                        is PillItem.Wikipedia -> viewModel.inputs.clickWikipedia()
                        is PillItem.Circuit -> startActivity(CircuitInfoActivity.intent(this, circuitId, initialTrackName))
                        else -> {} /* Do nothing */
                    }
                }
        )
        binding.links.adapter = linkAdapter
        binding.links.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

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
        binding.back.setOnClickListener { finish() }

        binding.trackLayout.setOnClickListener {
            startActivity(CircuitInfoActivity.intent(this, circuitId, initialTrackName))
        }

        // Observe

        observe(viewModel.outputs.showLinks) {
            linkAdapter.list = mutableListOf<PillItem>().apply {
                if (track == null) {
                    add(PillItem.Circuit())
                }
                if (it) {
                    add(PillItem.Wikipedia(""))
                }
            }
        }

        observe(viewModel.outputs.seasonRoundData) { (season, round) ->
            binding.tvRoundInfo.text = getString(
                    R.string.race_round_format,
                    round.toString(),
                    season.toString()
            ).fromHtml()
        }

        observe(viewModel.outputs.circuitInfo) {
            binding.imgCountry.setImageResource(getFlagResourceAlpha3(it.circuit.countryISO))
            binding.tvCircuitName.text = "${it.circuit.name}\n${it.circuit.country}"
            binding.grandprix.text = "${it.season} ${it.name}"
            binding.tvDate.text = it.date.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }

        observe(viewModel.outputs.raceItems) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        observeEvent(viewModel.outputs.goToDriverOverview) { (driverId, driverName) ->
            startActivity(DriverActivity.intent(this, driverId, driverName))
        }

        observeEvent(viewModel.outputs.goToConstructorOverview) { (constructorId, constructorName) ->
            startActivity(ConstructorActivity.intent(this, constructorId, constructorName))
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


        if (defaultToRace) {
            binding.menu.selectedItemId = R.id.nav_race
        } else {
            binding.menu.selectedItemId = R.id.nav_qualifying
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
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