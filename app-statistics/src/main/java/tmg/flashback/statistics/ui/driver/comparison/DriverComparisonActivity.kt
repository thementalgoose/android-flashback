package tmg.flashback.statistics.ui.driver.comparison

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityDriverComparisonBinding

class DriverComparisonActivity: BaseActivity() {

    private lateinit var binding: ActivityDriverComparisonBinding

    override val analyticsScreenName: String
        get() = "Driver Comparison"
    override val analyticsCustomAttributes: Map<String, String>
        get() = mapOf(
                "extra_season" to "$season",
                "extra_round" to "$round",
                "extra_driver1" to driverId1,
                "extra_driver2" to driverId2
        )

    private var season: Int = -1
    private var round: Int = -1
    private lateinit var driverId1: String
    private lateinit var driverId2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverComparisonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            season = it.getInt(keySeason)
            round = it.getInt(keyRound)
            driverId1 = it.getString(keyDriver1)!!
            driverId2 = it.getString(keyDriver2)!!
        }
    }

    companion object {

        private const val keySeason: String = "season"
        private const val keyRound: String = "round"
        private const val keyDriver1: String = "driver1"
        private const val keyDriver2: String = "driver2"

        fun intent(context: Context, season: Int, round: Int, driver1: String, driver2: String): Intent {
            val intent = Intent(context, DriverComparisonActivity::class.java)
            intent.putExtra(keySeason, season)
            intent.putExtra(keyRound, round)
            intent.putExtra(keyDriver1, driver1)
            intent.putExtra(keyDriver2, driver2)
            return intent
        }
    }
}