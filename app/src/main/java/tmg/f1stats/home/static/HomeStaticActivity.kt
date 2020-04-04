package tmg.f1stats.home.static

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home_static.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.home.trackpicker.TrackPickerBottomSheetFragment
import tmg.f1stats.season.race.RaceAdapter
import tmg.f1stats.season.race.RaceAdapterCallback
import tmg.f1stats.season.race.RaceAdapterType
import tmg.f1stats.season.race.RaceViewModel
import tmg.f1stats.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.click
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.subscribeNoError

class HomeStaticActivity: BaseActivity(), RaceAdapterCallback {

    private val viewModel: HomeStaticViewModel by viewModel()
    private val raceViewModel: RaceViewModel by viewModel()

    private lateinit var raceAdapter: RaceAdapter

    override fun layoutId(): Int = R.layout.activity_home_static

    override fun initViews() {

        initToolbar(R.id.toolbar)
        supportActionBar?.title = "F1 Stats"

        raceAdapter = RaceAdapter(this)
        rvData.adapter = raceAdapter
        rvData.layoutManager = LinearLayoutManager(this)
    }

    override fun observeViewModel() {

        // Inputs

        fabTrackList
            .click()
            .subscribeNoError {
                viewModel.clickTrackList()
            }
            .autoDispose()

        // Outputs

        viewModel.outputs
            .openTrackList()
            .subscribeNoError { (season, round) ->
                TrackPickerBottomSheetFragment
                    .instance(season, round)
                    .show(supportFragmentManager, "TrackPicker")
            }

        viewModel.outputs
            .circuitInfo()
            .subscribeNoError {
                imgCountry.setImageResource(getFlagResourceAlpha3(it.countryKey))
                tvCountry.text = it.country
                tvTrackName.text = it.circuitName
            }
            .autoDispose()

        viewModel.outputs
            .loadSeasonRound()
            .subscribeNoError { (season, round) ->
                raceViewModel.inputs.initialise(season, round)
            }
            .autoDispose()

        // TODO: Think about this - Race view model reuse

        raceViewModel.outputs
            .items()
            .subscribeNoError {
                raceAdapter.update(RaceAdapterType.RACE, it)
            }
            .autoDispose()
    }

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {

    }

    //endregion

}