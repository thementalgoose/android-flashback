package tmg.flashback.statistics.ui.overview.driver.season

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.statistics.databinding.FragmentDriverSeasonBinding
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DriverSeasonFragment: BaseFragment<FragmentDriverSeasonBinding>() {

    private val viewModel: DriverSeasonViewModel by viewModel()

    private lateinit var seasonAdapter: DriverSeasonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        seasonAdapter = DriverSeasonAdapter(
                itemClicked = viewModel.inputs::clickSeasonRound
        )

        arguments?.let {
            val driverId: String = it.getString(keyDriverId)!!
            val season: Int = it.getInt(keySeason)
            viewModel.inputs.setup(driverId, season)
        }
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDriverSeasonBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.adapter = seasonAdapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.list) {
            seasonAdapter.list = it
        }

        observeEvent(viewModel.outputs.openSeasonRound) { result ->
            context?.let {
                val intent: Intent = RaceActivity.intent(it,
                        season = result.season,
                        round = result.round,
                        circuitId = result.circuitId,
                        country = result.raceCountry,
                        raceName = result.raceName,
                        trackName = result.circuitName,
                        countryISO = result.raceCountryISO,
                        date = result.date
                )
                startActivity(intent)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.list) { _, inset ->
            binding.list.setPadding(0, 0, 0, inset.systemWindowInsetBottom)
            inset
        }
    }

    companion object {

        private const val keySeason: String = "keySeason"
        private const val keyDriverId: String = "keyDriverId"

        fun instance(season: Int, driverId: String): DriverSeasonFragment {
            val fragment = DriverSeasonFragment()
            val bundle = Bundle().apply {
                putInt(keySeason, season)
                putString(keyDriverId, driverId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}