package tmg.flashback.dashboard

import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.year.DashboardYearAdapter
import tmg.flashback.dashboard.year.DashboardYearModel
import tmg.flashback.extensions.setContentMultiplierForFullWidthPager
import tmg.utilities.extensions.dpToPx
import tmg.utilities.extensions.views.syncScrolling

// Multiplier of the width of the display the years will take up
const val yearScreenWidthMultiplier: Float = 0.3f

class DashboardActivity: BaseActivity() {

    private val viewModel: DashboardViewModel by inject()
    private val seasonAdapter: DashboardFragmentAdapter = DashboardFragmentAdapter(this)
    private val allAdapter: DashboardYearAdapter = DashboardYearAdapter()

    override fun layoutId(): Int = R.layout.activity_dashboard

    override fun initViews() {

        setupViewPagers()
    }

    private fun setupViewPagers() {

        vpSeason.adapter = seasonAdapter
        vpAll.adapter = allAdapter
        vpAll.setContentMultiplierForFullWidthPager(this, 16f.dpToPx(resources).toInt(),  yearScreenWidthMultiplier)

        vpSeason.syncScrolling(vpAll, 1f / yearScreenWidthMultiplier)
        vpAll.syncScrolling(vpSeason, yearScreenWidthMultiplier)

        allAdapter.list = List(19) { DashboardYearModel(it) }
    }
}