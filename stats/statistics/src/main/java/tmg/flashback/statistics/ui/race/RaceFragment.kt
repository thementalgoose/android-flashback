package tmg.flashback.statistics.ui.race

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.statistics.databinding.FragmentRaceBinding
import tmg.flashback.statistics.ui.race_old.RaceModel
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.views.invisible

class RaceFragment: BaseFragment<FragmentRaceBinding>() {

    private val viewModel: RaceViewModel by inject()

    private lateinit var raceData: RaceData

    private lateinit var raceAdapter: RaceAdapter

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

        raceAdapter = RaceAdapter()
        binding.dataList.adapter = raceAdapter
        binding.dataList.layoutManager = LinearLayoutManager(context)
        binding.progress.invisible()

        raceAdapter.list = listOf(
            RaceItem.Overview(
                raceName = raceData.raceName,
                country = raceData.country,
                countryISO = raceData.countryISO,
                circuitId = raceData.circuitId,
                circuitName = raceData.trackName,
                round = raceData.round,
                season = raceData.season,
                raceDate = raceData.date,
                wikipedia = null
            )
        )
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
}