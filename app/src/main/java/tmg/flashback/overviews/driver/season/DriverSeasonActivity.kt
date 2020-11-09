package tmg.flashback.overviews.driver.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import kotlinx.android.synthetic.main.activity_driver_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.utilities.extensions.loadFragment

class DriverSeasonActivity: BaseActivity() {

    private val viewModel: DriverSeasonViewModel by viewModel()

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

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
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