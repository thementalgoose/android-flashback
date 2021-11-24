package tmg.flashback.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.discord.panels.OverlappingPanelsLayout
import com.discord.panels.PanelState
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.common.ui.releasenotes.ReleaseBottomSheetFragment
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.R
import tmg.flashback.databinding.FragmentDashboardBinding
import tmg.flashback.statistics.controllers.SearchController
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.ui.dashboard.list.ListFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragmentCallback
import tmg.flashback.statistics.ui.search.SearchActivity
import tmg.utilities.extensions.observeEvent

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(),
    OverlappingPanelsLayout.PanelStateListener, DashboardNavigationCallback,
    SeasonFragmentCallback {

    private val viewModel: DashboardViewModel by viewModel()

    private val searchController: SearchController by inject()
    private val homeController: HomeController by inject()

    private val seasonTag: String = "season"
    private val seasonFragment: SeasonFragment?
        get() = childFragmentManager.findFragmentByTag(seasonTag) as? SeasonFragment
    private val listTag: String = "list"
    private val listFragment: ListFragment?
        get() = childFragmentManager.findFragmentByTag(listTag) as? ListFragment

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDashboardBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFragment(SeasonFragment(), R.id.season, seasonTag)
        loadFragment(ListFragment(), R.id.list, listTag)
        loadFragment(Fragment(), R.id.thirdpane, "placeholder")

        binding.panels.setEndPanelLockState(OverlappingPanelsLayout.LockState.CLOSE)

        binding.panels.registerStartPanelStateListeners(this)

        if (!homeController.dashboardCalendar) {
            binding.navigation.menu.removeItem(R.id.nav_calendar)
        }
        if (searchController.enabled) {
            binding.search.show()
            binding.search.setOnClickListener {
                viewModel.inputs.clickSearch()
            }
        }

        binding.navigation.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.nav_schedule -> {
                    seasonFragment?.selectSchedule()
                    true
                }
                R.id.nav_calendar -> {
                    if (homeController.dashboardCalendar) {
                        seasonFragment?.selectCalendar()
                        true
                    } else {
                        false
                    }
                }
                R.id.nav_drivers -> {
                    seasonFragment?.selectDrivers()
                    true
                }
                R.id.nav_constructor -> {
                    seasonFragment?.selectConstructors()
                    true
                }
                else -> false
            }
        }

        observeEvent(viewModel.outputs.openSearch) {
            context?.let {
                startActivity(SearchActivity.intent(it))
            }
        }

        observeEvent(viewModel.outputs.appConfigSynced) {
            listFragment?.refresh()
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            ReleaseBottomSheetFragment()
                .show(parentFragmentManager, "RELEASE_NOTES")
        }
    }

    private fun loadFragment(frag: Fragment, @IdRes layoutRes: Int, tag: String?) {
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        if (tag != null) {
            transaction.replace(layoutRes, frag, tag)
        } else {
            transaction.replace(layoutRes, frag)
        }
        transaction.commit()
    }

    //region OverlappingPanelsLayout.PanelStateListener

    override fun onPanelStateChange(panelState: PanelState) {

        if (binding.panels.getSelectedPanel() == OverlappingPanelsLayout.Panel.CENTER) {
            binding.navigation.animate()
                .translationY(0.0f)
                .setDuration(250L)
                .start()
            if (searchController.enabled) {
                binding.search.animate()
                    .translationY(0.0f)
                    .setDuration(250L)
                    .start()
            }
        } else {
            binding.navigation.animate()
                .translationY(binding.navigation.height.toFloat())
                .setDuration(250L)
                .start()
            if (searchController.enabled) {
                binding.search.animate()
                    .translationY(binding.navigation.height.toFloat())
                    .setDuration(250L)
                    .start()
            }
        }

        when (panelState) {
            PanelState.Opening -> {
            }
            PanelState.Opened -> {
                analyticsManager.logEvent("open_dashboard")
            }
            PanelState.Closing -> {
            }
            PanelState.Closed -> {
            }
        }
    }

    //endregion

    //region DashboardNavigationCallback

    override fun openSeasonList() {
        binding.panels.openStartPanel()
    }

    override fun seasonSelected(season: Int) {
        seasonFragment?.selectSeason(season)
        closeSeasonList()
    }

    override fun closeSeasonList() {
        binding.panels.closePanels()
    }

    //endregion

    //region SeasonFragmentCallback

    override fun openMenu() {
        binding.panels.openStartPanel()
    }

    override fun scrollUp() {
        if (searchController.enabled) {
            binding.search.extend()
        }
    }

    override fun scrollDown() {
        if (searchController.enabled) {
            binding.search.shrink()
        }
    }

    override fun refresh() {
//        upNextFragment?.refresh()
    }

    //endregion
}