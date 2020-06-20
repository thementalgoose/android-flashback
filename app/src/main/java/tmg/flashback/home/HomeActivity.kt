package tmg.flashback.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet_season.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.admin.lockout.LockoutActivity
import tmg.flashback.base.BaseActivity
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.list.HomeAdapter
import tmg.flashback.home.season.*
import tmg.flashback.minimumSupportedYear
import tmg.flashback.race.RaceActivity
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.flashback.utils.FragmentRequestBack
import tmg.flashback.utils.TextAdapter
import tmg.flashback.web.WebFragment
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*
import tmg.utilities.extensions.views.hide
import tmg.utilities.extensions.views.show

@FlowPreview
@ExperimentalCoroutinesApi
class HomeActivity : BaseActivity(), SeasonRequestedCallback, PageStateChangeCallbacks,
    FragmentRequestBack {

    private lateinit var adapter: HomeAdapter
    private lateinit var seasonBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var seasonAdapter: SeasonListAdapter

    private val viewModel: HomeViewModel by viewModel()
    private val seasonViewModel: SeasonViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        expandablePageLayout.pullToCollapseEnabled = false
        expandablePageLayout.addStateChangeCallbacks(this)

        loadFragment(Fragment(), R.id.expandablePageLayout, "WebView")

        adapter = HomeAdapter(
            articleClicked = { article, id ->
                dataList.expandItem(id)
                loadFragment(WebFragment.instance(article.title, article.link), R.id.expandablePageLayout, "WebView")
            },
            trackClicked = { track ->
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
        dataList.tintPainter = TintPainter.uncoveredArea(Color.WHITE, opacity = 0.65f)
        dataList.expandablePage = expandablePageLayout
        dataList.adapter = adapter
        dataList.layoutManager = LinearLayoutManager(this)

        menu.setOnNavigationItemSelectedListener {
            val shouldUpdateTab = when (it.itemId) {
                R.id.nav_news -> {
                    viewModel.inputs.clickItem(HomeMenuItem.NEWS)
                    swipeContainer.isEnabled = true
                    true
                }
                R.id.nav_calendar -> {
                    viewModel.inputs.clickItem(HomeMenuItem.CALENDAR)
                    swipeContainer.isEnabled = false
                    true
                }
                R.id.nav_drivers -> {
                    viewModel.inputs.clickItem(HomeMenuItem.DRIVERS)
                    swipeContainer.isEnabled = false
                    true
                }
                R.id.nav_constructor -> {
                    viewModel.inputs.clickItem(HomeMenuItem.CONSTRUCTORS)
                    swipeContainer.isEnabled = false
                    true
                }
                R.id.nav_seasons -> {
                    viewModel.inputs.clickItem(HomeMenuItem.SEASONS)
                    swipeContainer.isEnabled = false
                    false
                }
                else -> false
            }
            return@setOnNavigationItemSelectedListener shouldUpdateTab
        }

        setupBottomSheetSeason()

        swipeContainer.setOnRefreshListener {
            viewModel.inputs.refreshNews()
        }

        skeleton.showSkeleton()
        skeleton.showShimmer = true

        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        overlay.setOnClickListener {
            seasonBottomSheetBehavior.hidden()
        }

        //region HomeViewModel

        observe(viewModel.outputs.list) {
            swipeContainer.isRefreshing = false
            adapter.list = it
        }

        observe(viewModel.outputs.label) {
            season.text = it
        }

        observeEvent(viewModel.outputs.openSeasonList) {
            seasonBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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

        observe(viewModel.outputs.showLoading) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        //endregion

        //region SeasonViewModel

        observe(seasonViewModel.outputs.list) {
            seasonAdapter.list = it
        }

        observeEvent(seasonViewModel.outputs.showSeasonEvent) {
            seasonBottomSheetBehavior.hide()
            viewModel.inputs.selectSeason(it)
        }

        //endregion

        showLoading()
        menu.isEnabled = false
    }

    override fun onBackPressed() {
        when {
            seasonBottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN -> {
                seasonBottomSheetBehavior.hide()
            }
            expandablePageLayout.isExpandedOrExpanding -> {
                // Pipe back exit events through to fragment, let it handle the back event
                val webFrag = supportFragmentManager.findFragmentByTag("WebView") as? WebFragment
                webFrag?.exitWeb()
                dataList.collapse()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        bottomSheet.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        expandablePageLayout.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
        menu.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
        dataList.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    //region SeasonRequestedCallback

    override fun seasonRequested(season: Int) {
        viewModel.inputs.selectSeason(season)
        if (menu.selectedItemId != R.id.nav_calendar) {
            menu.selectedItemId = R.id.nav_calendar
        }
    }

    //endregion

    //region PageStateChangeCallbacks

    override fun onPageAboutToCollapse(collapseAnimDuration: Long) {
        menu.animate()
            .translationY(0f)
            .setDuration(collapseAnimDuration)
            .start()
    }

    override fun onPageAboutToExpand(expandAnimDuration: Long) {
        menu.animate()
            .translationY(menu.height.toFloat())
            .setDuration(expandAnimDuration)
            .start()
    }

    override fun onPageCollapsed() { }

    override fun onPageExpanded() { }

    //endregion

    //region FragmentRequestBack

    override fun fragmentBackPressed() {
        onBackPressed()
    }

    //endregion

    private fun BottomSheetBehavior<*>.hide() {
        this.hidden()
        optionsList.scrollToPosition(0)
        seasonCollapse()
    }

    private fun setupBottomSheetSeason() {
        seasonBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        seasonBottomSheetBehavior.isHideable = true
        seasonBottomSheetBehavior.hidden()
        seasonBottomSheetBehavior.halfExpandedRatio = 0.7f
        seasonBottomSheetBehavior.addBottomSheetCallback(BottomSheetFader(overlay, "seasons"))
        seasonBottomSheetBehavior.addBottomSheetCallback(SeasonBottomSheetCallback(
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
                when (menu.selectedItemId) {
                    R.id.nav_calendar,
                    R.id.nav_drivers,
                    R.id.nav_constructor -> {}
                    else -> menu.selectedItemId = R.id.nav_calendar
                }
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

    private fun showLoading() {
        loadingContainer.show()
    }

    private fun hideLoading() {
        loadingContainer.hide()
        menu.isEnabled = true
    }
}
