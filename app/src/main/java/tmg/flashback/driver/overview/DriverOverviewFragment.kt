package tmg.flashback.driver.overview

import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseFragment

class DriverOverviewFragment: BaseFragment() {

    private val viewModel: DriverOverviewViewModel by viewModel()

    override fun layoutId(): Int = R.layout.fragment_driver_season

}