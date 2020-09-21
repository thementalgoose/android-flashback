package tmg.flashback.driver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_driver.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.utilities.extensions.observe

class DriverActivity: BaseActivity() {

    private val viewModel: DriverViewModel by viewModel()

    private var tabLayoutMediator: TabLayoutMediator? = null

    private lateinit var pagerAdapter: DriverPagerAdapter
    private lateinit var driverId: String
    private var passthroughDriverName: String? = null

    override fun layoutId(): Int = R.layout.activity_driver

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)

        driverId = bundle.getString(keyDriverId)!!
        passthroughDriverName = bundle.getString(keyDriverName)
        viewModel.inputs.setup(driverId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pagerAdapter = DriverPagerAdapter(driverId, this)
        viewPager.adapter = pagerAdapter

        header.text = passthroughDriverName
        back.setOnClickListener {
            finish()
        }

        observe(viewModel.outputs.seasons) { list ->
            tabLayoutMediator?.detach()
            pagerAdapter.seasons = list
                .map { Pair(driverId, it) }
            tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.driver_overview_title_overview)
                    else -> tab.text = "${list.getOrNull(position - 1) ?: ""}"
                }
            }
            tabLayoutMediator?.attach()
        }

        swipeContainer.isEnabled = false

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "DriverID: $driverId", Toast.LENGTH_LONG).show()
        }
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        viewPager.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
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