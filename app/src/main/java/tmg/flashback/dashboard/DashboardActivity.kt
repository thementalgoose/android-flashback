package tmg.flashback.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.bottom_sheet_view_type.*
import kotlinx.android.synthetic.main.view_bottom_sheet_item.*
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.InterceptResult
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.admin.lockout.LockoutActivity
import tmg.flashback.base.BaseActivity
import tmg.flashback.dashboard.season.DashboardSeasonAdapter
import tmg.flashback.dashboard.year.DashboardYearAdapter
import tmg.flashback.dashboard.year.DashboardYearItem
import tmg.flashback.race.RaceActivity
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.flashback.standings.StandingsActivity
import tmg.flashback.utils.AnimatorListener
import tmg.flashback.utils.bottomsheet.BottomSheetAdapter
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*
import tmg.utilities.extensions.views.show

class DashboardActivity : BaseActivity() {

    private val viewModel: DashboardViewModel by viewModel()

    private lateinit var adapter: DashboardYearAdapter
    private lateinit var seasonAdapter: DashboardSeasonAdapter
    private lateinit var menuAdapter: BottomSheetAdapter
    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    override fun layoutId(): Int = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(R.id.bottombar, true, R.drawable.ic_menu)

        setupYearListAndDetails()

        setupBottomSheetMenu()



        observe(viewModel.outputs.years) {
            adapter.list = it
        }

        observe(viewModel.outputs.seasonList) {
            seasonAdapter.list = it
        }

        observe(viewModel.outputs.menuList) {
            menuAdapter.list = it
        }



        observeEvent(viewModel.outputs.openMenu) {
            bottomSheet.expand()
        }

        observeEvent(viewModel.outputs.openSettings) {
            bottomSheet.hidden()
            startActivity(Intent(this, SettingsActivity::class.java))
        }


        observeEvent(viewModel.outputs.showStandings) {
            startActivity(StandingsActivity.intent(this, it))
        }


        observeEvent(viewModel.outputs.showAppLockoutMessage) {
            startActivityClearStack(Intent(this, LockoutActivity::class.java))
        }

        observeEvent(viewModel.outputs.showReleaseNotes) {
            val instance = ReleaseBottomSheetFragment()
            instance.show(supportFragmentManager, "Release Notes")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            viewModel.inputs.clickMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            bottomSheet.state != BottomSheetBehavior.STATE_HIDDEN -> {
                bottomSheet.hidden()
            }
            eplMain.isExpandedOrExpanding -> {
                irvMain.collapse()
                seasonAdapter.list = emptyList()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }



    private fun setupYearListAndDetails() {

        adapter = DashboardYearAdapter(
            itemClicked = { model, itemId ->
                irvMain.expandItem(itemId)
                viewModel.inputs.clickSeason(model.year)
            },
            settingsClicked = viewModel.inputs::clickSettings
        )
        adapter.list = listOf(
            DashboardYearItem.Header,
            DashboardYearItem.Placeholder
        )
        irvMain.expandablePage = eplMain
        irvMain.tintPainter = TintPainter.uncoveredArea(Color.WHITE, opacity = 0.8f)
        irvMain.layoutManager = LinearLayoutManager(this)
        irvMain.adapter = adapter

        seasonAdapter = DashboardSeasonAdapter(
            itemClickedCallback = {
                startActivity(RaceActivity.intent(this, it))
            },
            standingsClickedCallback = viewModel.inputs::clickStandings,
            listClosed = {
                irvMain.collapse()
            }
        )
        seasonList.adapter = seasonAdapter
        seasonList.layoutManager = LinearLayoutManager(this)

        eplMain.pullToCollapseInterceptor = { downX, downY, upwardPull ->
            val directionInt = if (upwardPull) +1 else -1
            val canScrollFurther = nsvMain.canScrollVertically(directionInt)
            if (canScrollFurther) InterceptResult.INTERCEPTED else InterceptResult.IGNORED
        }

        eplMain.addStateChangeCallbacks(object : PageStateChangeCallbacks {
            override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
                animateBottomBarIn(collapseAnimDuration)
            }

            override fun onPageAboutToExpand(expandAnimDuration: Long) {
                animateBottomBarOut(expandAnimDuration)
            }

            override fun onPageExpanded() { }

            override fun onPageCollapsed() { }
        })
    }

    private fun animateBottomBarIn(duration: Long) {
        bottombar.animate()
            .alpha(1.0f)
            .setDuration(duration)
            .setListener(AnimatorListener(
                start = {
                    bottombar.show(true)
                }
            ))
            .start()
    }

    private fun animateBottomBarOut(duration: Long) {
        bottombar.animate()
            .alpha(0.0f)
            .setDuration(duration)
            .setListener(AnimatorListener(
                start = {
                    bottombar.show(true)
                },
                end = {
                    bottombar.show(false)
                }
            ))
            .start()
    }

    private fun setupBottomSheetMenu() {
        menuSubtitle.text = getString(R.string.app_version_version_name, BuildConfig.VERSION_NAME)

        bottomSheet = BottomSheetBehavior.from(viewTypeLayout)
        bottomSheet.isHideable = true
        bottomSheet.hidden()
        bottomSheet.addBottomSheetCallback(BottomSheetFader(cover, "cover"))
        cover.setOnClickListener { bottomSheet.hidden() }

        menuAdapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.clickMenuItem(DashboardMenuItem.values()[it.id])
            }
        )
        viewTypeList.adapter = menuAdapter
        viewTypeList.layoutManager = LinearLayoutManager(this)

        container.setOnClickListener {
            viewModel.inputs.clickSettings()
        }
        menuItemIcon.setImageResource(R.drawable.nav_settings)
        menuItemLabel.setText(R.string.settings_title)
    }
}