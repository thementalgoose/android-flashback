package tmg.flashback.home

import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet_season.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.admin.lockout.LockoutActivity
import tmg.flashback.base.BaseActivity
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.overviews.constructor.ConstructorActivity
import tmg.flashback.overviews.driver.DriverActivity
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.list.HomeAdapter
import tmg.flashback.home.season.*
import tmg.flashback.minimumSupportedYear
import tmg.flashback.race.RaceActivity
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*

@FlowPreview
@ExperimentalCoroutinesApi
class HomeActivity : BaseActivity(), SeasonRequestedCallback {

    private lateinit var adapter: HomeAdapter
    private lateinit var seasonBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var seasonAdapter: SeasonListAdapter

    private val viewModel: HomeViewModel by viewModel()
    private val seasonViewModel: SeasonViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        swipeContainer.isEnabled = false

        adapter = HomeAdapter(
                trackClicked = { track ->
                    val intent = RaceActivity.intent(
                            context = this,
                            season = track.season,
                            round = track.round,
                            circuitId = track.circuitId,
                            country = track.raceCountry,
                            raceName = track.raceName,
                            trackName = track.circuitName,
                            countryISO = track.raceCountryISO,
                            date = track.date,
                            defaultToRace = track.hasResults || !track.hasQualifying
                    )
                    startActivity(intent)
                },
                driverClicked = { season: Int, driverId: String, firstName: String?, lastName: String? ->
                    val intent = DriverActivity.intent(this, driverId, "$firstName $lastName")
                    startActivity(intent)
                },
                constructorClicked = { constructorId: String, constructorName: String ->
                    val intent = ConstructorActivity.intent(this, constructorId, constructorName)
                    startActivity(intent)
                }
        )
        dataList.adapter = adapter
        dataList.layoutManager = LinearLayoutManager(this)

        if (!toggleDB.isRSSEnabled) {
            menu.menu.removeItem(R.id.nav_rss)
        }
        menu.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.nav_rss -> {
                    startActivity(RSSActivity.intent(this))
                    false
                }
                R.id.nav_calendar -> {
                    viewModel.inputs.clickItem(HomeMenuItem.CALENDAR)
                    true
                }
                R.id.nav_drivers -> {
                    viewModel.inputs.clickItem(HomeMenuItem.DRIVERS)
                    true
                }
                R.id.nav_constructor -> {
                    viewModel.inputs.clickItem(HomeMenuItem.CONSTRUCTORS)
                    true
                }
                R.id.nav_seasons -> {
                    viewModel.inputs.clickItem(HomeMenuItem.SEASONS)
                    when (menu.selectedItemId) {
                        R.id.nav_calendar,
                        R.id.nav_drivers,
                        R.id.nav_constructor -> {
                        }
                        else -> menu.selectedItemId = R.id.nav_calendar
                    }
                    false
                }
                else -> false
            }
        }

        setupBottomSheetSeason()

        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        overlay.setOnClickListener {
            seasonBottomSheetBehavior.hidden()
        }

        //region HomeViewModel

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.label) {
            season.text = it.resolve(context = this)
        }

        observeEvent(viewModel.outputs.openSeasonList) {
            if (it) {
                seasonBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                seasonBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        observeEvent(viewModel.outputs.openAppLockout) {
            startActivity(Intent(this, LockoutActivity::class.java))
            finishAffinity()
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

        observeEvent(viewModel.outputs.ensureOnCalendar) {
            when (menu.selectedItemId) {
                R.id.nav_calendar,
                R.id.nav_constructor,
                R.id.nav_drivers -> {
                }
                else -> menu.selectedItemId = R.id.nav_calendar
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

        menu.selectedItemId = R.id.nav_calendar
    }

    override fun onBackPressed() {
        when {
            seasonBottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN -> {
                seasonBottomSheetBehavior.hide()
            }
            else -> {
                super.onBackPressed()
            }
        }
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
        if (menu.selectedItemId != R.id.nav_calendar) {
            menu.selectedItemId = R.id.nav_calendar
        }
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
                            } else {
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
        swipeContainer.isRefreshing = true
        menu.isEnabled = false
        dataList.alpha = 0.7f
    }

    private fun hideLoading() {
        swipeContainer.isRefreshing = false
        menu.isEnabled = true
        dataList.alpha = 1.0f
    }
}
