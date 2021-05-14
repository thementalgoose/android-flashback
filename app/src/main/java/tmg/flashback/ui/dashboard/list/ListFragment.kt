package tmg.flashback.ui.dashboard.list

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.databinding.FragmentDashboardListBinding
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.R
import tmg.flashback.ui.settings.SettingsAllActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ListFragment: BaseFragment<FragmentDashboardListBinding>() {

    private val viewModel: ListViewModel by viewModel()

    private var adapter: ListAdapter? = null
    private val dashboardNavigationCallback: tmg.flashback.ui.dashboard.DashboardNavigationCallback? get() = parentFragment as? tmg.flashback.ui.dashboard.DashboardNavigationCallback

    @Suppress("RedundantNullableReturnType")
    private val tickReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (BuildConfig.DEBUG) {
                Log.i("Flashback", "Broadcast Receiver tick for time update")
            }
            adapter?.refreshUpNext()
        }
    }

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
                     }
                },
                timeDisplayFormatClicked = viewModel.inputs::clickTimeDisplayType
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

    override fun onResume() {
        super.onResume()
        adapter?.refreshUpNext()
        context?.registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        super.onPause()
        if (tickReceiver != null) {
            context?.unregisterReceiver(tickReceiver)
        }
    }

    fun refresh() {
        viewModel.inputs.refresh()
    }
}