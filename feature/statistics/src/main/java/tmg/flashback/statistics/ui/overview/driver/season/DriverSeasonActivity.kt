package tmg.flashback.statistics.ui.overview.driver.season

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityDriverSeasonBinding
import tmg.utilities.extensions.loadFragment

class DriverSeasonActivity: BaseActivity() {

    private lateinit var binding: ActivityDriverSeasonBinding
    private val viewModel: DriverSeasonViewModel by viewModel()

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

            viewModel.inputs.setup(driverId, season)
        }

        logScreenViewed("Driver Season Overview", mapOf(
            "driver_id" to driverId,
            "driver_name" to driverName,
            "season" to season.toString()
        ))

        @SuppressLint("SetTextI18n")
        binding.header.text = "$driverName $season"

        binding.back.setOnClickListener {
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