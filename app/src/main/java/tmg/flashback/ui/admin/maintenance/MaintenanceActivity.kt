package tmg.flashback.ui.admin.maintenance

import android.os.Bundle
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.ActivityMaintenanceBinding
import tmg.flashback.shared.ui.base.BaseActivity

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