package tmg.flashback.driver.season

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_driver_season.*
import kotlinx.android.synthetic.main.fragment_driver_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.flashback.race.RaceActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DriverSeasonFragment: BaseFragment() {

    private val viewModel: DriverSeasonViewModel by viewModel()

    private lateinit var seasonAdapter: DriverSeasonAdapter

    override fun layoutId(): Int = R.layout.fragment_driver_season

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        val driverId: String = bundle.getString(keyDriverId)!!
        val season: Int = bundle.getInt(keySeason)
        viewModel.inputs.setup(driverId, season)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        seasonAdapter = DriverSeasonAdapter(
            itemClicked = viewModel.inputs::clickSeasonRound
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = seasonAdapter
        list.layoutManager = LinearLayoutManager(context)

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

        ViewCompat.setOnApplyWindowInsetsListener(list) { _, inset ->
            list.setPadding(0, 0, 0, inset.systemWindowInsetBottom)
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