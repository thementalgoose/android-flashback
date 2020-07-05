package tmg.flashback.race

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.faltenreich.skeletonlayout.SkeletonLayout
import kotlinx.android.synthetic.main.activity_race.*
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.TrackLayout
import tmg.flashback.base.BaseActivity
import tmg.flashback.circuit.CircuitInfoActivity
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.toEnum
import tmg.utilities.extensions.views.show

private const val duration: Long = 200L
private const val dateFormat: String = "yyyy/MM/dd"

class RaceActivity : BaseActivity(), RaceAdapterCallback {

    private val viewModel: RaceViewModel by inject()
    private lateinit var raceAdapter: RaceAdapter

    var season: Int = -1
    var round: Int = -1
    lateinit var circuitId: String

    private var initialCountry: String = ""
    private var initialCountryISO: String = ""
    private var initialRaceName: String = ""
    private var initialTrackName: String = ""
    private var initialDate: LocalDate? = null

    companion object {

        private const val keySeason: String = "season"
        private const val keyRound: String = "round"
        private const val keyCircuitId: String = "circuit"

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
                putExtra(keyCountry, country)
                putExtra(keyRaceName, raceName)
                putExtra(keyTrackName, trackName)
                putExtra(keyCountryISO, countryISO)
                putExtra(keyDate, date?.format(DateTimeFormatter.ofPattern(dateFormat)))
            }
        }
    }

    override fun layoutId(): Int = R.layout.activity_race

    override fun arguments(bundle: Bundle) {
        season = bundle.getInt(keySeason)
        round = bundle.getInt(keyRound)
        circuitId = bundle.getString(keyCircuitId)!!

        initialCountry = bundle.getString(keyCountry, "")
        initialCountryISO = bundle.getString(keyCountryISO, "")
        initialRaceName = bundle.getString(keyRaceName, "")
        initialTrackName = bundle.getString(keyTrackName, "")
        val date = bundle.getString(keyDate, "")
        if (date.isNotEmpty()) {
            initialDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat))
        }

        viewModel.inputs.initialise(season, round, initialDate)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup

        raceAdapter = RaceAdapter(this)
        rvContent.adapter = raceAdapter
        rvContent.layoutManager = LinearLayoutManager(this)

        grandprix.text = "$season $initialRaceName"
        tvCircuitName.text = "$initialTrackName\n$initialCountry"
        if (initialCountryISO.isNotEmpty()) {
            imgCountry.setImageResource(getFlagResourceAlpha3(initialCountryISO))
        }
        initialDate?.let {
            tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }

        val track = circuitId.toEnum<TrackLayout> { it.circuitId }
        trackLayout.show(track != null)
        circuit.show(track == null)
        if (track != null) {
            trackLayout.setImageResource(track.icon)
        }

        menu.setOnNavigationItemSelectedListener {
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
        back.setOnClickListener { finish() }

        circuit.setOnClickListener {
            startActivity(CircuitInfoActivity.intent(this, circuitId, initialTrackName))
        }

        trackLayout.setOnClickListener {
            startActivity(CircuitInfoActivity.intent(this, circuitId, initialTrackName))
        }

        // Observe


        observe(viewModel.outputs.seasonRoundData) { (season, round) ->
            tvRoundInfo.text = getString(
                R.string.race_round_format,
                round.toString(),
                season.toString()
            ).fromHtml()
        }

        observe(viewModel.outputs.circuitInfo) {
            imgCountry.setImageResource(getFlagResourceAlpha3(it.circuit.countryISO))
            tvCircuitName.text = "${it.circuit.name}\n${it.circuit.country}"
            grandprix.text = "${it.season} ${it.name}"
            tvDate.text = it.date.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }

        observe(viewModel.outputs.raceItems) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        menu.selectedItemId = R.id.nav_race
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        menu.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
        rvContent.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        viewModel.inputs.orderBy(adapterType)
    }

    //endregion
}