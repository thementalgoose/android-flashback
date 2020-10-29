package tmg.flashback.driver

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_driver.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.driver.overview.DriverOverviewAdapter
import tmg.flashback.driver.season.DriverSeasonActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DriverActivity: BaseActivity() {

    private val viewModel: DriverViewModel by viewModel()

    private lateinit var driverId: String
    private lateinit var driverName: String
    private lateinit var adapter: DriverOverviewAdapter

    override fun layoutId(): Int = R.layout.activity_driver

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        driverId = bundle.getString(keyDriverId)!!
        driverName = bundle.getString(keyDriverName)!!

        viewModel.inputs.setup(driverId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = driverName

        adapter = DriverOverviewAdapter(
            openUrl = viewModel.inputs::openUrl,
            seasonClicked = viewModel.inputs::openSeason
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        back.setOnClickListener {
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

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        list.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
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