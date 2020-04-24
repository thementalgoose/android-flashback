package tmg.flashback.home.trackpicker

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.bottom_sheet_track_picker.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.lifecycle.rx.RxBottomSheetFragment

class TrackPickerBottomSheetFragment: RxBottomSheetFragment() {

    private val viewModel: TrackPickerViewModel by viewModel()
    private var trackPickerCallback: TrackPickerCallback? = null

    private lateinit var seasonAdapter: TrackPickerYearAdapter
    private lateinit var roundAdapter: TrackPickerRoundAdapter

    override fun layoutId(): Int = R.layout.bottom_sheet_track_picker

    override fun arguments(bundle: Bundle) {
        val season: Int = bundle.getInt(keySeason)
        val round: Int = bundle.getInt(keyRound)
        viewModel.inputs.initialSeasonRound(season, round)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TrackPickerCallback) {
            trackPickerCallback = context
        }
    }

    override fun initViews() {
        seasonAdapter = TrackPickerYearAdapter()
        seasonAdapter.addListener {
            viewModel.inputs.clickSeason(it)
        }
        rvSeasons.adapter = seasonAdapter
        rvSeasons.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        roundAdapter = TrackPickerRoundAdapter()
        roundAdapter.addListener {
            viewModel.inputs.clickRound(it.round)
        }
        rvRounds.adapter = roundAdapter
        rvRounds.layoutManager = LinearLayoutManager(context)

        imgbtnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun observeViewModel() {
        observe(viewModel.outputs.yearList) {
            seasonAdapter.list = it
        }

        observe(viewModel.outputs.trackList) {
            roundAdapter.list = it
        }

        observeEvent(viewModel.outputs.selected) { (season, round) ->
            trackPickerCallback?.updateSelected(season, round)
            dismiss()
        }
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