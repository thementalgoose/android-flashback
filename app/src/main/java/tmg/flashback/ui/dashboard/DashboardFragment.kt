package tmg.flashback.ui.dashboard

import android.content.Intent
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
import tmg.core.ui.base.BaseFragment
import tmg.flashback.R
import tmg.flashback.databinding.FragmentDashboardBinding
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.ui.admin.maintenance.MaintenanceActivity
import tmg.flashback.ui.dashboard.list.ListFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragment
import tmg.flashback.statistics.ui.dashboard.season.SeasonFragmentCallback
import tmg.utilities.extensions.observeEvent

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(),
    OverlappingPanelsLayout.PanelStateListener, DashboardNavigationCallback, SeasonFragmentCallback {

    private val viewModel: DashboardViewModel by viewModel()

    private val seasonController: SeasonController by inject()

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
        loadFragment(Fragment(), R.id.other, "rightPane")

        binding.panels.setEndPanelLockState(lockState = OverlappingPanelsLayout.LockState.CLOSE)
        binding.panels.registerStartPanelStateListeners(this)

        if (!seasonController.dashboardCalendar) {
            binding.navigation.menu.removeItem(R.id.nav_calendar)
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

        observeEvent(viewModel.outputs.openAppLockout) {
            activity?.let {
                startActivity(Intent(it, MaintenanceActivity::class.java))
                it.finishAffinity()
            }
        }

        observeEvent(viewModel.outputs.appConfigSynced) {
            listFragment?.refresh()
            seasonFragment?.refresh()
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
        when (panelState) {
            PanelState.Opened, PanelState.Opening -> binding.navigation.animate()
                .translationY(binding.navigation.height.toFloat())
                .setDuration(250L)
                .start()

            else -> binding.navigation.animate()
                .translationY(0.0f)
                .setDuration(250L)
                .start()
        }

        when (panelState) {
            PanelState.Opening -> {}
            PanelState.Opened -> {
                analyticsManager.logEvent("open_dashboard")
            }
            PanelState.Closing -> { }
            PanelState.Closed -> { }
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

    //endregion
}