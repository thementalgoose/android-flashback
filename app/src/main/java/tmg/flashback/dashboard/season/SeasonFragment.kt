package tmg.flashback.dashboard.season

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_dashboard_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.flashback.dashboard.DashboardNavigationCallback
import tmg.flashback.home.HomeMenuItem
import tmg.flashback.rss.ui.RSSActivity
import tmg.utilities.extensions.observeEvent

class SeasonFragment: BaseFragment() {

    private var dashboardNavigation: DashboardNavigationCallback? = null

    private val viewModel: SeasonViewModel by viewModel()

    override fun layoutId() = R.layout.fragment_dashboard_season

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardNavigationCallback) {
            dashboardNavigation = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navigation.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.nav_rss -> {
                    activity?.let { activity ->
                        startActivity(RSSActivity.intent(activity))
                    }
                    false
                }
                R.id.nav_calendar -> {
//                    viewModel.inputs.clickItem(HomeMenuItem.CALENDAR)
                    true
                }
                R.id.nav_drivers -> {
//                    viewModel.inputs.clickItem(HomeMenuItem.DRIVERS)
                    true
                }
                R.id.nav_constructor -> {
//                    viewModel.inputs.clickItem(HomeMenuItem.CONSTRUCTORS)
                    true
                }
                else -> false
            }
        }

        menu.setOnClickListener {
            viewModel.inputs.clickMenu()
        }

        settings.setOnClickListener {
            viewModel.inputs.clickSettings()
        }


        observeEvent(viewModel.outputs.openMenu) {
            dashboardNavigation?.openSeasonList()
        }

        observeEvent(viewModel.outputs.openSettings) {

        }
    }

    /**
     * Publicly accessible method for changing which season this fragment displays
     *  Called from DashboardActivity as a result of an external season selection
     */
    fun selectSeason(season: Int) {
        viewModel.inputs.selectSeason(season)
        Toast.makeText(context, "SELECTING SEASON $season", Toast.LENGTH_LONG).show()
    }
}