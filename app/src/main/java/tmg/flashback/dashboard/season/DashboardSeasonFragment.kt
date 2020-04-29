package tmg.flashback.dashboard.season

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard_season.*
import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.utilities.extensions.observe

class DashboardSeasonFragment: BaseFragment() {

    private val viewModel: DashboardSeasonViewModel by inject()
    private lateinit var adapter: DashboardSeasonAdapter

    override fun layoutId(): Int = R.layout.fragment_dashboard_season

    override fun arguments(bundle: Bundle) {
        val season: Int = bundle.getInt(keySeason, 0)
        viewModel.inputs.load(season)
    }

    override fun initViews() {

        adapter = DashboardSeasonAdapter()
        rvDashboardSeason.adapter = adapter
        rvDashboardSeason.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }
    }

    companion object {

        private const val keySeason: String = "season"

        fun instance(season: Int): DashboardSeasonFragment {
            val instance: DashboardSeasonFragment = DashboardSeasonFragment()
            instance.arguments = Bundle().apply {
                putInt(keySeason, season)
            }
            return instance
        }
    }
}