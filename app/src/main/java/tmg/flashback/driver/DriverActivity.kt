package tmg.flashback.driver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import kotlinx.android.synthetic.main.activity_driver.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity

class DriverActivity: BaseActivity() {

    private val viewModel: DriverViewModel by viewModel()

    private lateinit var pagerAdapter: DriverPagerAdapter
    private lateinit var driverId: String
    private var passthroughDriverName: String? = null
    private var passthroughDriverImg: String? = null

    override fun layoutId(): Int = R.layout.activity_driver

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)

        driverId = bundle.getString(keyDriverId)!!
        passthroughDriverName = bundle.getString(keyDriverName)
        passthroughDriverImg = bundle.getString(keyDriverImg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pagerAdapter = DriverPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        header.text = passthroughDriverName
        back.setOnClickListener {
            finish()
        }

        viewModel.inputs.setup(driverId)
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        viewPager.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    companion object {

        private const val keyDriverId: String = "keyDriverId"
        private const val keyDriverName: String = "keyDriverName"
        private const val keyDriverImg: String = "keyDriverImg"

        fun intent(context: Context, driverId: String, driverName: String? = null, driverImg: String? = null): Intent {
            val intent = Intent(context, DriverActivity::class.java)
            intent.putExtra(keyDriverId, driverId)
            intent.putExtra(keyDriverName, driverName)
            intent.putExtra(keyDriverImg, driverImg)
            return intent
        }
    }
}