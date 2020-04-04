package tmg.f1stats.home.trackpicker

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.bottom_sheet_track_picker.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.utilities.extensions.subscribeNoError
import tmg.utilities.lifecycle.rx.RxBottomSheetFragment

class TrackPickerBottomSheetFragment: RxBottomSheetFragment() {

    private val viewModel: TrackPickerViewModel by viewModel()

    private lateinit var seasonAdapter: TrackPickerYearAdapter
    private lateinit var roundAdapter: TrackPickerRoundAdapter

    override fun layoutId(): Int = R.layout.bottom_sheet_track_picker

    override fun arguments(bundle: Bundle) {
        val season: Int = bundle.getInt(keySeason)
        val round: Int = bundle.getInt(keyRound)
        viewModel.inputs.initialSeasonRound(season, round)
    }

    override fun initViews() {

        seasonAdapter = TrackPickerYearAdapter()
        rvSeasons.adapter = seasonAdapter
        rvSeasons.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        roundAdapter = TrackPickerRoundAdapter()
        rvRounds.adapter = roundAdapter
        rvRounds.layoutManager = LinearLayoutManager(context)
    }

    override fun observeViewModel() {

        viewModel.outputs
            .yearList()
            .subscribeNoError {
                seasonAdapter.list = it
            }
            .autoDispose()

        viewModel.outputs
            .trackList()
            .subscribeNoError {
                roundAdapter.list = it
            }
            .autoDispose()

    }

    companion object {

        const val keySeason: String = "SEASON"
        const val keyRound: String = "ROUND"

        fun instance(season: Int, round: Int): TrackPickerBottomSheetFragment {
            val instance = TrackPickerBottomSheetFragment()
            instance.arguments = Bundle().apply {
                putInt(keySeason, season)
                putInt(keyRound, round)
            }
            return instance
        }
    }

}