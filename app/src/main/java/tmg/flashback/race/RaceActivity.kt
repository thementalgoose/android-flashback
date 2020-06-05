package tmg.flashback.race

import android.content.Context
import android.content.Intent
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.faltenreich.skeletonlayout.SkeletonLayout
import kotlinx.android.synthetic.main.activity_race.*
import kotlinx.android.synthetic.main.layout_race_not_found.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.season.DashboardSeasonAdapterItem
import tmg.flashback.race.RaceDisplayMode.*
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.observe

private const val duration: Long = 200L
private const val dateFormat: String = "yyyy/MM/dd"

class RaceActivity : BaseActivity(), RaceAdapterCallback {

    private val viewModel: RaceViewModel by inject()
    private lateinit var raceAdapter: RaceAdapter

    private var initialCountry: String = ""
    private var initialCountryISO: String = ""
    private var initialRaceName: String = ""
    private var initialTrackName: String = ""
    private var initialDate: LocalDate? = null

    companion object {

        private const val keySeason: String = "season"
        private const val keyRound: String = "round"

        private const val keyCountry: String = "keyCountry"
        private const val keyRaceName: String = "keyRaceName"
        private const val keyTrackName: String = "keyTrackName"
        private const val keyCountryISO: String = "keyCountryISO"
        private const val keyDate: String = "keyDate"

        fun intent(context: Context, trackModel: DashboardSeasonAdapterItem.Track): Intent {
            return Intent(context, RaceActivity::class.java).apply {
                putExtra(keySeason, trackModel.season)
                putExtra(keyRound, trackModel.round)
                putExtra(keyTrackName, trackModel.trackName)
                putExtra(keyRaceName, trackModel.raceName)
                putExtra(keyCountry, trackModel.trackNationality)
                putExtra(keyCountryISO, trackModel.trackISO)
                putExtra(keyDate, trackModel.date.format(DateTimeFormatter.ofPattern(dateFormat)))
            }
        }

        fun intent(
            context: Context,
            season: Int,
            round: Int,
            country: String? = null,
            raceName: String? = null,
            trackName: String? = null,
            countryISO: String? = null,
            date: String? = null
        ): Intent {
            return Intent(context, RaceActivity::class.java).apply {
                putExtra(keySeason, season)
                putExtra(keyRound, round)
                putExtra(keyCountry, country)
                putExtra(keyRaceName, raceName)
                putExtra(keyTrackName, trackName)
                putExtra(keyCountryISO, countryISO)
                putExtra(keyDate, date)
            }
        }
    }

    override fun layoutId(): Int = R.layout.activity_race

    override fun arguments(bundle: Bundle) {
        val season = bundle.getInt(keySeason)
        val round = bundle.getInt(keyRound)

        viewModel.inputs.initialise(season, round)

        initialCountry = bundle.getString(keyCountry, "")
        initialCountryISO = bundle.getString(keyCountryISO, "")
        initialRaceName = bundle.getString(keyRaceName, "")
        initialTrackName = bundle.getString(keyTrackName, "")
        val date = bundle.getString(keyDate, "")
        if (date.isNotEmpty()) {
            initialDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup

        initToolbar(R.id.toolbar, true, R.drawable.ic_back)

        raceAdapter = RaceAdapter(this)
        rvContent.adapter = raceAdapter
        rvContent.layoutManager = LinearLayoutManager(this)

        notFound.alpha = 0.0f
        networkError.alpha = 0.0f

        toolbarLayout.header.text = initialRaceName
        tvCircuitName.text = "$initialTrackName\n$initialCountry"
        if (initialCountryISO.isNotEmpty()) {
            imgCountry.setImageResource(getFlagResourceAlpha3(initialCountryISO))
        }
        initialDate?.let {
            tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }

        val layout = findViewById<SkeletonLayout>(R.id.skeleton)
        layout.showSkeleton()

        initialiseLottie(notFound.lottieNoData)

        bnvType.setOnNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.nav_qualifying -> {
//                    viewModel.inputs.orderBy(RaceAdapterType.QUALIFYING_POS)
//                    true
//                }
//                R.id.nav_race -> {
//                    viewModel.inputs.orderBy(RaceAdapterType.RACE)
//                    true
//                }
                R.id.nav_constructor -> {
                    viewModel.inputs.orderBy(RaceAdapterType.CONSTRUCTOR_STANDINGS)
                    true
                }
                else -> false
            }
        }

        // Observe

        observe(viewModel.outputs.raceDisplayMode) {
            updateDisplay(it)
        }

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
            toolbarLayout.header.text = it.name
            tvDate.text = it.date.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }

        observe(viewModel.outputs.raceItems) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

//        bnvType.selectedItemId = R.id.nav_race
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialiseLottie(lottieView: LottieAnimationView) {
        val colorFilter = SimpleColorFilter(theme.getColor(R.attr.f1Loading))
        val keyPath = KeyPath("**")
        val callback = LottieValueCallback<ColorFilter>(colorFilter)
        lottieView.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
    }

    private fun updateDisplay(raceMode: RaceDisplayMode) {
        animate(networkError, raceMode == NO_NETWORK)
        animate(notFound, raceMode == NOT_FOUND)
        animate(skeleton, raceMode == LOADING)
        animate(rvContent, raceMode == LIST)
    }

    private fun animate(layout: View, animateIn: Boolean) {
        layout.animate()
            .setDuration(duration)
            .alpha(if (animateIn) 1.0f else 0.0f)
            .start()
    }

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        viewModel.inputs.orderBy(adapterType)
    }

    //endregion
}