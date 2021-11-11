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
import tmg.common.ui.releasenotes.ReleaseBottomSheetFragment
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.R
import tmg.flashback.databinding.FragmentDashboardBinding
import tmg.flashback.statistics.controllers.SearchController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.ui.dashboard.list.ListFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragmentCallback
import tmg.flashback.statistics.ui.search.SearchActivity
import tmg.flashback.upnext.ui.dashboard.UpNextFragment
import tmg.flashback.upnext.ui.onboarding.OnboardingNotificationBottomSheetFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(),
    OverlappingPanelsLayout.PanelStateListener, DashboardNavigationCallback,
    SeasonFragmentCallback {

    private val viewModel: DashboardViewModel by viewModel()

    private val searchController: SearchController by inject()
    private val seasonController: SeasonController by inject()

    private val seasonTag: String = "season"
    private val seasonFragment: SeasonFragment?
        get() = childFragmentManager.findFragmentByTag(seasonTag) as? SeasonFragment
    private val listTag: String = "list"
    private val listFragment: ListFragment?
        get() = childFragmentManager.findFragmentByTag(listTag) as? ListFragment
    private val upNextTag: String = "list"
    private val upNextFragment: UpNextFragment?
        get() = childFragmentManager.findFragmentByTag(upNextTag) as? UpNextFragment

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDashboardBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFragment(SeasonFragment(), R.id.season, seasonTag)
        loadFragment(ListFragment(), R.id.list, listTag)
        loadFragment(UpNextFragment(), R.id.upnext, upNextTag)

        binding.panels.registerStartPanelStateListeners(this)
        binding.panels.registerEndPanelStateListeners(this)

        if (!seasonController.dashboardCalendar) {
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
                    if (seasonController.dashboardCalendar) {
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

        observe(viewModel.outputs.showUpNext) {
//            seasonFragment?.showUpNext(it)
        }

        observeEvent(viewModel.outputs.openSearch) {
            context?.let {
                startActivity(SearchActivity.intent(it))
            }
        }

        observeEvent(viewModel.outputs.appConfigSynced) {
            listFragment?.refresh()
//            seasonFragment?.refresh()
            upNextFragment?.refresh()
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

    override fun openNow() {
        binding.panels.openEndPanel()
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

    //endregion
}