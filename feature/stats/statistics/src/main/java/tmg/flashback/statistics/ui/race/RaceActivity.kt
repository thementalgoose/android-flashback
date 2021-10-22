package tmg.flashback.statistics.ui.race

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.core.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityRaceBinding
import tmg.utilities.extensions.*

class RaceActivity : BaseActivity() {

    private lateinit var binding: ActivityRaceBinding

    private lateinit var raceData: RaceData

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

        intent?.extras?.let {
            raceData = it.getParcelable(keyRaceData)!!
        }

        loadFragment(RaceFragment.instance(raceData), R.id.container)
    }
}