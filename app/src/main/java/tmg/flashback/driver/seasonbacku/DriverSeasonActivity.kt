package tmg.flashback.driver.seasonbacku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_driver_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.driver.seasonbacku.list.DriverSeasonAdapter
import tmg.flashback.race.RaceActivity
import tmg.utilities.extensions.observe

class DriverSeasonActivity: BaseActivity() {

    private var season: Int = -1
    private lateinit var driverId: String
    private var driverFirstname: String? = null
    private var driverLastname: String? = null

    private val viewModel: DriverSeasonViewModel by viewModel()

    private lateinit var adapter: DriverSeasonAdapter

    override fun layoutId() = R.layout.activity_driver_season

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        season = bundle.getInt(keySeason)
        driverId = bundle.getString(keyDriverId)!!
        driverFirstname = bundle.getString(keyFirstname)
        driverLastname = bundle.getString(keyLastname)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = "$driverFirstname $driverLastname - $season"

        swipeContainer.isEnabled = false

        adapter = DriverSeasonAdapter(
            itemClicked = { result ->
                val intent: Intent = RaceActivity.intent(this,
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
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        back.setOnClickListener {
            onBackPressed()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        viewModel.inputs.initialise(season, driverId)
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        list.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    companion object {

        private const val keySeason: String = "keySeason"
        private const val keyDriverId: String = "keyDriverId"
        private const val keyFirstname: String = "keyFirstname"
        private const val keyLastname: String = "keyLastname"

        fun intent(
                context: Context,
                season: Int,
                driverId: String,
                firstName: String? = null,
                lastName: String? = null
        ): Intent {
            val intent = Intent(context, DriverSeasonActivity::class.java)
            intent.putExtra(keySeason, season)
            intent.putExtra(keyDriverId, driverId)
            firstName?.let { intent.putExtra(keyFirstname, it) }
            lastName?.let { intent.putExtra(keyLastname, it) }
            return intent
        }
    }
}