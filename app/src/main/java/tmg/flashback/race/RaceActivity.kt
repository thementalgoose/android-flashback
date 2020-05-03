package tmg.flashback.race

import android.content.Context
import android.content.Intent
import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.layout_race_network.view.*
import kotlinx.android.synthetic.main.layout_race_not_found.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.ext.android.inject
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.race.RaceDisplayMode.*
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.observe

private const val duration: Long = 200L

class RaceActivity : BaseActivity(), RaceAdapterCallback {

    private val viewModel: RaceViewModel by inject()
    private lateinit var raceAdapter: RaceAdapter

    private var initialCountry: String = ""
    private var initialCountryISO: String = ""
    private var initialTrackName: String = ""

    companion object {

        private const val keySeason: String = "season"
        private const val keyRound: String = "round"

        private const val keyCountry: String = "keyCountry"
        private const val keyTrackName: String = "keyTrackName"
        private const val keyCountryISO: String = "keyCountryISO"

        fun intent(
            context: Context,
            season: Int,
            round: Int,
            country: String? = null,
            trackName: String? = null,
            countryISO: String? = null
        ): Intent {
            return Intent(context, RaceActivity::class.java).apply {
                putExtra(keySeason, season)
                putExtra(keyRound, round)
                putExtra(keyCountry, country)
                putExtra(keyTrackName, trackName)
                putExtra(keyCountryISO, countryISO)
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
        initialTrackName = bundle.getString(keyTrackName, "")
    }

    override fun initViews() {

        // Setup

        initToolbar(R.id.toolbar, true, R.drawable.ic_back)

        raceAdapter = RaceAdapter(this)
        rvContent.adapter = raceAdapter
        rvContent.layoutManager = LinearLayoutManager(this)

        notFound.alpha = 0.0f
        networkError.alpha = 0.0f

        toolbarLayout.header.text = initialCountry
        tvCircuitName.text = initialTrackName
        if (initialCountryISO.isNotEmpty()) {
            imgCountry.setImageResource(getFlagResourceAlpha3(initialCountryISO))
        }

        val layout = findViewById<SkeletonLayout>(R.id.skeleton)
        layout.showSkeleton()

        initialiseLottie(notFound.lottieNoData)

        bnvType.setOnNavigationItemSelectedListener {
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

                    false
                }
                else -> false
            }
        }

        // Observe

        observe(viewModel.outputs.raceDisplayMode) {
            updateDisplay(it)
        }

        observe(viewModel.outputs.seasonRoundData) { (season, round) ->
            tvRoundInfo.text = getString(R.string.race_round_format, round.toString(), season.toString()).fromHtml()
        }

        observe(viewModel.outputs.circuitInfo) {
            imgCountry.setImageResource(getFlagResourceAlpha3(it.circuit.countryISO))
            tvCircuitName.text = it.circuit.name
            toolbarLayout.header.text = it.circuit.country
        }

        observe(viewModel.outputs.raceItems) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        bnvType.selectedItemId = R.id.nav_race
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