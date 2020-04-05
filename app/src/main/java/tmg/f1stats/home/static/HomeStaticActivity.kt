package tmg.f1stats.home.static

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_home_static.*
import kotlinx.android.synthetic.main.bottom_sheet_track_picker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.format.DateTimeFormatter
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.home.trackpicker.TrackPickerRoundAdapter
import tmg.f1stats.home.trackpicker.TrackPickerYearAdapter
import tmg.f1stats.season.race.RaceAdapter
import tmg.f1stats.season.race.RaceAdapterCallback
import tmg.f1stats.season.race.RaceAdapterType
import tmg.f1stats.season.race.RaceViewModel
import tmg.f1stats.utils.BottomSheetFader
import tmg.f1stats.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.*

class HomeStaticActivity : BaseActivity(), RaceAdapterCallback {

    private val viewModel: HomeStaticViewModel by viewModel()
    private val raceViewModel: RaceViewModel by viewModel()

    private lateinit var raceAdapter: RaceAdapter

    private lateinit var trackBottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var seasonAdapter: TrackPickerYearAdapter
    private lateinit var roundAdapter: TrackPickerRoundAdapter

    override fun layoutId(): Int = R.layout.activity_home_static

    override fun initViews() {

        initToolbar(R.id.toolbar)
        supportActionBar?.title = "F1 Stats"

        raceAdapter = RaceAdapter(this)
        rvData.adapter = raceAdapter
        rvData.layoutManager = LinearLayoutManager(this)

        // Bottom Sheet

        trackBottomSheetBehavior = BottomSheetBehavior.from(bsTrackPicker)
        trackBottomSheetBehavior.isHideable = true
        trackBottomSheetBehavior.peekHeight = 300f.dpToPx(resources).toInt()
        trackBottomSheetBehavior.hidden()
        trackBottomSheetBehavior.addBottomSheetCallback(BottomSheetFader(vBackground, "Track") { id ->
            trackBottomSheetBehavior.hidden()
        })

        seasonAdapter = TrackPickerYearAdapter()
        seasonAdapter.addListener { year ->
            viewModel.inputs.browse(year)
        }
        rvSeasons.adapter = seasonAdapter
        rvSeasons.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        roundAdapter = TrackPickerRoundAdapter()
        roundAdapter.addListener { model ->
            viewModel.inputs.select(model.season, model.round)
            trackBottomSheetBehavior.hidden()
        }
        rvRounds.adapter = roundAdapter
        rvRounds.layoutManager = LinearLayoutManager(this)
    }

    override fun observeViewModel() {

        // Inputs

        fabTrackList
            .click()
            .subscribeNoError {
                viewModel.inputs.clickTrackList()
            }
            .autoDispose()

        imgbtnClose
            .click()
            .subscribeNoError {
                viewModel.inputs.closeTrackList()
            }
            .autoDispose()

        // Outputs

        viewModel.outputs
            .openTrackList()
            .subscribeNoError {
                if (it) {
                    trackBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                } else {
                    trackBottomSheetBehavior.hidden()
                }
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

        viewModel.outputs
            .switchYearList()
            .subscribeNoError {
                seasonAdapter.list = it
            }
            .autoDispose()

        viewModel.outputs
            .switchTrackList()
            .subscribeNoError {
                roundAdapter.list = it
            }
            .autoDispose()

        // TODO: Think about this - Race view model reuse

        raceViewModel.outputs
            .items()
            .subscribeNoError { (adapterType, list) ->
                raceAdapter.update(adapterType, list)
            }
            .autoDispose()

        raceViewModel.outputs
            .date()
            .subscribeNoError {
                tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            }
            .autoDispose()

        raceViewModel.outputs
            .time()
            .subscribeNoError {

            }
            .autoDispose()
    }

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        raceViewModel.inputs.orderBy(adapterType)
    }

    //endregion

}