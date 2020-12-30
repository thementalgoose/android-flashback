package tmg.flashback.dashboard.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.flashback.dashboard.DashboardNavigationCallback
import tmg.flashback.settings.SettingsActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ListFragment: BaseFragment() {

    private val viewModel: ListViewModel by viewModel()

    private lateinit var adapter: ListAdapter
    private var dashboardNavigationCallback: DashboardNavigationCallback? = null

    override fun layoutId() = R.layout.fragment_dashboard_list

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardNavigationCallback) {
            dashboardNavigationCallback = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter(
            settingsClicked = viewModel.inputs::clickSettings,
            featureToggled = viewModel.inputs::toggleHeader,
            favouriteToggled = viewModel.inputs::toggleFavourite,
            seasonClicked = viewModel.inputs::clickSeason
        )
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.showSeasonEvent) { season ->
            dashboardNavigationCallback?.let {
                it.seasonSelected(season)
                it.closeSeasonList()
            }
        }

        observeEvent(viewModel.outputs.openSettings) {
            context?.let {
                startActivity(Intent(it, SettingsActivity::class.java))
            }
        }
    }
}