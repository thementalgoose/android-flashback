package tmg.flashback.statistics.ui.overview.driver.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.core.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityDriverSeasonBinding
import tmg.utilities.extensions.loadFragment

class DriverSeasonActivity : BaseActivity() {

    private lateinit var binding: ActivityDriverSeasonBinding

    private lateinit var driverId: String
    private lateinit var driverName: String
    private var season: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverSeasonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            driverId = it.getString(keyDriverId)!!
            driverName = it.getString(keyDriverName)!!
            season = it.getInt(keyDriverSeason)
        }

        loadFragment(DriverSeasonFragment.instance(season, driverId, driverName), R.id.container)
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