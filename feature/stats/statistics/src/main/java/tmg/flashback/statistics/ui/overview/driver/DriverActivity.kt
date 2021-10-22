package tmg.flashback.statistics.ui.overview.driver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityDriverBinding
import tmg.utilities.extensions.loadFragment

class DriverActivity : BaseActivity() {

    private lateinit var binding: ActivityDriverBinding

    private lateinit var driverId: String
    private lateinit var driverName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            driverId = it.getString(keyDriverId)!!
            driverName = it.getString(keyDriverName)!!
        }

        loadFragment(DriverFragment.instance(driverId, driverName), R.id.container)
    }

    companion object {

        private const val keyDriverId: String = "keyDriverId"
        private const val keyDriverName: String = "keyDriverName"

        fun intent(context: Context, driverId: String, driverName: String): Intent {
            val intent = Intent(context, DriverActivity::class.java)
            intent.putExtra(keyDriverId, driverId)
            intent.putExtra(keyDriverName, driverName)
            return intent
        }
    }
}