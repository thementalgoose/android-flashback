package tmg.flashback.statistics.ui.admin.maintenance

import android.os.Bundle
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.statistics.databinding.ActivityMaintenanceBinding

class MaintenanceActivity: BaseActivity() {

    private lateinit var binding: ActivityMaintenanceBinding

//    override val screenAnalytics: ScreenAnalytics? = null // Opt out of analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}