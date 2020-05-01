package tmg.flashback.race

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_race.*
import kotlinx.android.synthetic.main.activity_race.imgCountry
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.view_dashboard_season_track.*
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.observe

class RaceActivity : BaseActivity(), RaceAdapterCallback {

    private val viewModel: RaceViewModel by inject()
    private lateinit var raceAdapter: RaceAdapter

    override fun layoutId(): Int = R.layout.activity_race

    override fun arguments(bundle: Bundle) {
        val year = bundle.getInt(keyYear)
        val round = bundle.getInt(keyRound)

        viewModel.inputs.initialise(year, round)
    }

    override fun initViews() {

        initToolbar(R.id.toolbar, true, R.drawable.ic_back)

        raceAdapter = RaceAdapter(this)
        rvContent.adapter = raceAdapter
        rvContent.layoutManager = LinearLayoutManager(this)

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

        observe(viewModel.outputs.seasonRoundData) { (season, round) ->
            tvRoundInfo.text = getString(R.string.race_round_format, season.toString(), round.toString())
        }

        observe(viewModel.outputs.circuitInfo) {
            imgCountry.setImageResource(getFlagResourceAlpha3(it.circuit.countryISO))
            tvCircuitName.text = it.circuit.name
            toolbarLayout.header.text = it.circuit.country
        }

        observe(viewModel.outputs.raceItems) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val keyYear: String = "year"
        private const val keyRound: String = "round"

        fun intent(context: Context, year: Int, round: Int): Intent {
            return Intent(context, RaceActivity::class.java).apply {
                putExtra(keyYear, year)
                putExtra(keyRound, round)
            }
        }
    }

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        viewModel.inputs.orderBy(adapterType)
    }

    //endregion
}