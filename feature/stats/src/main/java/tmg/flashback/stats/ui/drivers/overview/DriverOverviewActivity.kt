package tmg.flashback.stats.ui.drivers.overview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.stats.ui.constructors.overview.ConstructorOverviewActivity
import tmg.flashback.stats.ui.constructors.overview.ConstructorOverviewScreenVM
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import java.sql.Driver

class DriverOverviewActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val driverId: String = intent.extras?.getString(keyDriverId)!!
        val driverName: String = intent.extras?.getString(keyDriverName)!!

        setContent {
            AppTheme {
                DriverOverviewScreenVM(
                    driverId = driverId,
                    driverName = driverName
                )
            }
        }
    }

    companion object {

        private const val keyDriverId: String = "driverId"
        private const val keyDriverName: String = "driverName"

        fun intent(context: Context, driverId: String, driverName: String): Intent {
            return Intent(context, DriverOverviewActivity::class.java).apply {
                putExtra(keyDriverId, driverId)
                putExtra(keyDriverName, driverName)
            }
        }
    }
}