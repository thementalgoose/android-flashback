package tmg.flashback.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.admin.lockout.LockoutActivity
import tmg.flashback.base.BaseActivity
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.list.HomeAdapter
import tmg.flashback.home.season.*
import tmg.flashback.minimumSupportedYear
import tmg.flashback.news.NewsActivity
import tmg.flashback.race.RaceActivity
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.collapse
import tmg.utilities.extensions.hidden
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import kotlin.coroutines.coroutineContext


class HomeActivity : BaseActivity(), SeasonRequestedCallback {

    private lateinit var adapter: HomeAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var seasonAdapter: SeasonListAdapter

    private val viewModel: HomeViewModel by viewModel()
    private val seasonViewModel: SeasonViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HomeAdapter(
            trackClicked = { track ->
                dataList.alpha = 0.5f

                val intent = RaceActivity.intent(
                    context = this,
                    season = track.season,
                    round = track.round,
                    country = track.raceCountry,
                    raceName = track.raceName,
                    trackName = track.circuitName,
                    countryISO = track.raceCountryISO,
                    date = track.date
                )
                startActivity(intent)
            }
        )
        dataList.adapter = adapter
        dataList.layoutManager = LinearLayoutManager(this)

        menu.setOnNavigationItemSelectedListener {
            val shouldUpdateTab = when (it.itemId) {
                R.id.nav_calendar -> {
                    dataList.alpha = 0.5f
                    viewModel.inputs.clickItem(HomeMenuItem.CALENDAR)
                    true
                }
                R.id.nav_drivers -> {
                    dataList.alpha = 0.5f
                    viewModel.inputs.clickItem(HomeMenuItem.DRIVERS)
                    true
                }
                R.id.nav_constructor -> {
                    dataList.alpha = 0.5f
                    viewModel.inputs.clickItem(HomeMenuItem.CONSTRUCTORS)
                    true
                }
                R.id.nav_seasons -> {
                    viewModel.inputs.clickItem(HomeMenuItem.SEASONS)
                    false
                }
//                R.id.nav_news -> {
//                    startActivity(Intent(this, NewsActivity::class.java))
//                    viewModel.inputs.clickItem(HomeMenuItem.NEWS)
//                    true
//                }
                else -> false
            }
            return@setOnNavigationItemSelectedListener shouldUpdateTab
        }

        setupBottomSheet()

        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        overlay.setOnClickListener {
            bottomSheetBehavior.hidden()
        }

        //region HomeViewModel

        observe(viewModel.outputs.list) {
            dataList.alpha = 1.0f
            adapter.list = it
        }

        observe(viewModel.outputs.currentSeason) {
            season.text = it.toString()
        }

        observeEvent(viewModel.outputs.openSeasonList) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        observeEvent(viewModel.outputs.openAppLockout) {
            startActivity(Intent(this, LockoutActivity::class.java))
            finishAffinity()
        }

        observe(viewModel.outputs.openAppBanner) { banner ->
            banner?.let {
                Snackbar
                    .make(dataList, it, 5000)
                    .setAnchorView(menu)
                    .show()
            }
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            ReleaseBottomSheetFragment()
                .show(supportFragmentManager, "RELEASE_NOTES")
        }

        //endregion

        //region SeasonViewModel

        observe(seasonViewModel.outputs.list) {
            seasonAdapter.list = it
        }

        observeEvent(seasonViewModel.outputs.showSeasonEvent) {
            bottomSheetBehavior.hide()
            viewModel.inputs.selectSeason(it)
        }

        //endregion
    }

    override fun onResume() {
        super.onResume()
        dataList.alpha = 1.0f
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.hide()
        }
        else {
            super.onBackPressed()
        }
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.hidden()
        bottomSheetBehavior.halfExpandedRatio = 0.6f
        bottomSheetBehavior.addBottomSheetCallback(BottomSheetFader(overlay, "seasons"))
        bottomSheetBehavior.addBottomSheetCallback(SeasonBottomSheetCallback(
                expanded = { seasonExpand() },
                collapsed = { seasonCollapse() }
        ))

        seasonAdapter = SeasonListAdapter(
            featureToggled = {
                seasonViewModel.inputs.toggleHeader(it)
            },
            favouriteToggled = {
                seasonViewModel.inputs.toggleFavourite(it)
            },
            seasonClicked = {
                seasonViewModel.inputs.clickSeason(it)
            }
        )
        optionsList.adapter = seasonAdapter
        optionsList.layoutManager = LinearLayoutManager(this)

        fastscroller.setupWithRecyclerView(optionsList, {
            when (val item = seasonAdapter.list[it]) {
                SeasonListItem.Top -> FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_current)
                is SeasonListItem.Season -> {
                    when (item.fixed) {
                        HeaderType.CURRENT -> FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_current)
                        HeaderType.FAVOURITED -> FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_favourite)
                        HeaderType.ALL -> {
                            if (item.season != minimumSupportedYear) {
                                FastScrollItemIndicator.Text("${item.season.toString().substring(2, 3)}0")
                            }
                            else {
                                FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_start)
                            }
                        }
                    }
                }
                is SeasonListItem.Header -> {
                    when (item.type) {
                        HeaderType.CURRENT -> FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_current)
                        HeaderType.FAVOURITED -> FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_favourite)
                        HeaderType.ALL -> null
                    }
                }
            }
        })
        fastscrollerThumb.setupWithFastScroller(fastscroller)

        fastscroller.translationX = dimensionPx(R.dimen.bottomSheetFastScrollWidth)
    }

    private fun seasonExpand() {
        fastscroller.animate()
            .translationX(0.0f)
            .setDuration(bottomSheetFastScrollDuration.toLong())
            .start()
        seasonAdapter.setToggle(true)
    }

    private fun seasonCollapse() {
        fastscroller.animate()
            .translationX(dimensionPx(R.dimen.bottomSheetFastScrollWidth))
            .setDuration(bottomSheetFastScrollDuration.toLong())
            .start()
        seasonAdapter.setToggle(false)
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        bottomSheet.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        menu.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
        dataList.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    //region SeasonRequestedCallback

    override fun seasonRequested(season: Int) {
        viewModel.inputs.selectSeason(season)
    }

    //endregion

    class LockableLinearLayoutManager(
        context: Context,
        val canScrollVertical: () -> Boolean
    ): LinearLayoutManager(context) {
        override fun canScrollVertically(): Boolean {
            return canScrollVertical()
        }
    }

    fun BottomSheetBehavior<*>.hide() {
        this.hidden()
        optionsList.scrollToPosition(0)
        seasonCollapse()
    }
}