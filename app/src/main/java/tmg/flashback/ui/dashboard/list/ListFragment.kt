package tmg.flashback.ui.dashboard.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.ui.navigation.NavigationProvider
import tmg.flashback.DebugController
import tmg.flashback.databinding.FragmentDashboardListBinding
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.statistics.R
import tmg.flashback.ui.dashboard.DashboardNavigationCallback
import tmg.flashback.ui.settings.SettingsAllActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ListFragment: BaseFragment<FragmentDashboardListBinding>() {

    private val viewModel: ListViewModel by viewModel()

    private val navigationProvider: NavigationProvider by inject()

    private val debugController: DebugController by inject()

    private var adapter: ListAdapter? = null
    private val dashboardNavigationCallback: DashboardNavigationCallback?
        get() = parentFragment as? DashboardNavigationCallback

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDashboardListBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter(
                featureToggled = viewModel.inputs::toggleHeader,
                favouriteToggled = viewModel.inputs::toggleFavourite,
                seasonClicked = viewModel.inputs::clickSeason,
                setDefaultClicked = viewModel.inputs::clickSetDefaultSeason,
                clearDefaultClicked = viewModel.inputs::clickClearDefaultSeason,
                buttonClicked = {
                     when (it) {
                         "rss" -> viewModel.inputs.clickRss()
                         "settings" -> viewModel.inputs.clickSettings()
                         "contact" -> viewModel.inputs.clickContact()
                         "up_next_moved" -> dashboardNavigationCallback?.openNow()
                         "debug_list_item" -> {
                             context?.let { debugController.goToDebugActivity(it) }
                         }
                     }
                }
        )
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        observe(viewModel.outputs.list) {
            adapter?.list = it
        }

        observeEvent(viewModel.outputs.showSeasonEvent) { season ->
            dashboardNavigationCallback?.let {
                it.seasonSelected(season)
                it.closeSeasonList()
            }
        }

        observeEvent(viewModel.outputs.openSettings) {
            context?.let {
                startActivity(Intent(it, SettingsAllActivity::class.java))
            }
        }

        observeEvent(viewModel.outputs.openRss) {
            context?.let {
                startActivity(Intent(it, RSSActivity::class.java))
            }
        }

        observeEvent(viewModel.outputs.openContact) {
            context?.let {
                startActivity(navigationProvider.aboutAppIntent(it))
            }
        }

        observeEvent(viewModel.outputs.defaultSeasonUpdated) {
            when (it) {
                null -> Snackbar
                        .make(binding.list, getString(R.string.dashboard_season_list_default_banner_automatic), Snackbar.LENGTH_LONG)
                        .show()
                else -> Snackbar
                        .make(binding.list, getString(R.string.dashboard_season_list_default_banner_user, it), Snackbar.LENGTH_LONG)
                        .setAction(R.string.dashboard_season_list_default_banner_revert) {
                            viewModel.inputs.clickClearDefaultSeason()
                        }
                        .show()
            }
        }
    }

    fun refresh() {
        viewModel.inputs.refresh()
    }
}