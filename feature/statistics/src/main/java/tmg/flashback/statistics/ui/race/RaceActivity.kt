package tmg.flashback.statistics.ui.race

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.koin.android.ext.android.inject
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityRaceBinding
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.loadFragment
import java.lang.NullPointerException

class RaceActivity : BaseActivity() {

    private lateinit var binding: ActivityRaceBinding

    private val crashController: CrashController by inject()

    companion object {
        private const val keyRaceData: String = "keyRaceData"

        fun intent(context: Context, raceData: RaceData): Intent {
            val intent = Intent(context, RaceActivity::class.java)
            intent.putExtra(keyRaceData, raceData)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val raceData: RaceData = intent?.extras?.getParcelable(keyRaceData) ?: run {
            crashController.logException(NullPointerException("RaceActivity resumed with no race data, finishing activity"))
            finish()
            return
        }
        loadFragment(RaceFragment.instance(raceData), R.id.container)
    }
}