package tmg.flashback.statistics.ui.overview.driver.season

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.statistics.databinding.FragmentDriverSeasonBinding
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.statistics.ui.race.RaceData
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DriverSeasonFragment: BaseFragment<FragmentDriverSeasonBinding>() {

    private val viewModel: DriverSeasonViewModel by viewModel()

    private lateinit var driverId: String
    private lateinit var driverName: String
    private var season: Int = -1
    private lateinit var seasonAdapter: DriverSeasonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            driverName = it.getString(keyDriverName)!!
            driverId = it.getString(keyDriverId)!!
            season = it.getInt(keySeason)
            viewModel.inputs.setup(driverId, season)
        }
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDriverSeasonBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Driver Season Overview", mapOf(
                "driver_id" to driverId,
                "driver_name" to driverName,
                "season" to season.toString()
        ))

        seasonAdapter = DriverSeasonAdapter(
                itemClicked = viewModel.inputs::clickSeasonRound
        )
        binding.dataList.adapter = seasonAdapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.inputs.refresh()
        }

        observe(viewModel.outputs.list) {
            seasonAdapter.list = it
        }

        observe(viewModel.outputs.isLoading) {
            binding.swipeRefresh.isRefreshing = it
        }

        observeEvent(viewModel.outputs.openSeasonRound) { result ->
            context?.let {
                val intent: Intent = RaceActivity.intent(it, RaceData(
                        season = result.season,
                        round = result.round,
                        circuitId = result.circuitId,
                        country = result.raceCountry,
                        raceName = result.raceName,
                        trackName = result.circuitName,
                        countryISO = result.raceCountryISO,
                        date = result.date
                ))
                startActivity(intent)
            }
        }
    }

    companion object {

        private const val keySeason: String = "season"
        private const val keyDriverId: String = "driverId"
        private const val keyDriverName: String = "driverName"

        fun bundle(season: Int, driverId: String, driverName: String): Bundle {
            return bundleOf(
                keySeason to season,
                keyDriverId to driverId,
                keyDriverName to driverName
            )
        }

        @Deprecated("Should be accessed via. a NavGraph")
        fun instance(season: Int, driverId: String, driverName: String): DriverSeasonFragment {
            return DriverSeasonFragment().apply {
                arguments = bundleOf(
                        keyDriverName to driverName,
                        keySeason to season,
                        keyDriverId to driverId
                )
            }
        }
    }
}