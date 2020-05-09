package tmg.flashback.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.InterceptResult
import me.saket.inboxrecyclerview.page.PullToCollapseListener
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.swiping.season.DashboardSeasonAdapter
import tmg.flashback.dashboard.swiping.season.DashboardSeasonFragment
import tmg.flashback.dashboard.year.DashboardYearAdapter
import tmg.flashback.race.RaceActivity
import tmg.flashback.settings.SettingsActivity
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DashboardActivity : BaseActivity() {

    private val viewModel: DashboardViewModel by viewModel()

    private lateinit var adapter: DashboardYearAdapter
    private lateinit var seasonAdapter: DashboardSeasonAdapter

    override fun layoutId(): Int = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = DashboardYearAdapter(
            itemClicked = { model, itemId ->
                irvMain.expandItem(itemId)
                viewModel.inputs.clickSeason(model.year)
            },
            settingsClicked = viewModel.inputs::clickSettings
        )
        irvMain.expandablePage = eplMain
        irvMain.tintPainter = TintPainter.uncoveredArea(Color.WHITE, opacity = 0.65f)
        irvMain.layoutManager = LinearLayoutManager(this)
        irvMain.adapter = adapter

        seasonAdapter = DashboardSeasonAdapter(
            itemClickedCallback = {
                startActivity(RaceActivity.intent(this, it))
            },
            listClosed = {
                irvMain.collapse()
            }
        )
        seasonList.adapter = seasonAdapter
        seasonList.layoutManager = LinearLayoutManager(this)

        observe(viewModel.outputs.years) {
            adapter.list = it
        }

        observe(viewModel.outputs.seasonList) {
            seasonAdapter.list = it
        }

        observeEvent(viewModel.outputs.openSettings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        eplMain.pullToCollapseInterceptor = { downX, downY, upwardPull ->
            val directionInt = if (upwardPull) +1 else -1
            val canScrollFurther = nsvMain.canScrollVertically(directionInt)
            if (canScrollFurther) InterceptResult.INTERCEPTED else InterceptResult.IGNORED
        }
    }

    override fun onBackPressed() {
        if (eplMain.isExpandedOrExpanding) {
            irvMain.collapse()
            seasonAdapter.list = emptyList()
        } else {
            super.onBackPressed()
        }
    }
}