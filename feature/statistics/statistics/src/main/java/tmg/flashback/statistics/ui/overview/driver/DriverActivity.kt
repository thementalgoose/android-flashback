package tmg.flashback.statistics.ui.overview.driver

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.statistics.constants.ViewType
import tmg.flashback.statistics.databinding.ActivityDriverBinding
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryAdapter
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonActivity

class DriverActivity: BaseActivity() {

    private lateinit var binding: ActivityDriverBinding
    private val viewModel: DriverViewModel by viewModel()

    override val screenAnalytics get() = ScreenAnalytics(
        screenName = "Driver Overview",
        attributes = mapOf(
            "extra_driver_id" to driverId
        )
    )

    private lateinit var driverId: String
    private lateinit var driverName: String
    private lateinit var adapter: DriverSummaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            driverId = it.getString(keyDriverId)!!
            driverName = it.getString(keyDriverName)!!

            analyticsController.logEvent(ViewType.DRIVER, mapOf(
                "driver_id" to driverId,
                "driver_name" to driverName
            ))

            viewModel.inputs.setup(driverId)
        }

        binding.header.text = driverName

        adapter = DriverSummaryAdapter(
                openUrl = viewModel.inputs::openUrl,
                seasonClicked = viewModel.inputs::openSeason
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.back.setOnClickListener { 
            onBackPressed()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openSeason) { (driverId, season) ->
            val intent = DriverSeasonActivity.intent(this, driverId, driverName, season)
            startActivity(intent)
        }

        observeEvent(viewModel.outputs.openUrl) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
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