package tmg.flashback.ui.overviews.driver.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_driver_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.loadFragment

class DriverSeasonActivity: BaseActivity() {

    private val viewModel: DriverSeasonViewModel by viewModel()

    override val analyticsScreenName: String
        get() = "Driver Season Overview"
    override val analyticsCustomAttributes: Map<String, String>
        get() = mapOf(
                "driverId" to driverId,
                "driverName" to driverName,
                "season" to "$season"
        )

    private lateinit var driverId: String
    private lateinit var driverName: String
    private var season: Int = -1

    override fun layoutId(): Int = R.layout.activity_driver_season

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        driverId = bundle.getString(keyDriverId)!!
        driverName = bundle.getString(keyDriverName)!!
        season = bundle.getInt(keyDriverSeason)

        viewModel.inputs.setup(driverId, season)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = "$driverName $season"

        back.setOnClickListener {
            onBackPressed()
        }

        loadFragment(DriverSeasonFragment.instance(season, driverId), R.id.fragment)
    }

    companion object {

        private const val keyDriverId: String = "driverId"
        private const val keyDriverName: String = "driverName"
        private const val keyDriverSeason: String = "season"

        fun intent(context: Context, driverId: String, driverName: String, season: Int): Intent {
            val intent = Intent(context, DriverSeasonActivity::class.java)
            intent.putExtra(keyDriverId, driverId)
            intent.putExtra(keyDriverName, driverName)
            intent.putExtra(keyDriverSeason, season)
            return intent
        }
    }
}