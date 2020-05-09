package tmg.flashback.dashboard

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.InterceptResult
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.swiping.season.DashboardSeasonFragment
import tmg.flashback.dashboard.year.DashboardYearAdapter
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.observe

class DashboardActivity : BaseActivity() {

    private val viewModel: DashboardViewModel by viewModel()

    private lateinit var adapter: DashboardYearAdapter

    override fun layoutId(): Int = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        irvMain.expandablePage = eplMain
        irvMain.tintPainter = TintPainter.uncoveredArea(Color.WHITE, opacity = 0.65f)

        adapter = DashboardYearAdapter(
            itemClicked = { model, itemId ->
                irvMain.expandItem(itemId)
            }
        )
        irvMain.layoutManager = LinearLayoutManager(this)
        irvMain.adapter = adapter

        observe(viewModel.outputs.years) {
            adapter.list = it
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
        } else {
            super.onBackPressed()
        }
    }
}