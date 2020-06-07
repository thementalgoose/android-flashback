package tmg.flashback.home

import android.content.Context
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.list.HomeAdapter
import tmg.flashback.home.season.*
import tmg.flashback.minimumSupportedYear
import tmg.flashback.race.RaceActivity
import tmg.utilities.bottomsheet.BottomSheetFader
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
            trackClicked = viewModel.inputs::clickTrack
        )
        dataList.adapter = adapter
        dataList.layoutManager = LinearLayoutManager(this)

        menu.setOnNavigationItemSelectedListener {
            val shouldUpdateTab = when (it.itemId) {
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
                    false
                }
                else -> false
            }

            return@setOnNavigationItemSelectedListener shouldUpdateTab
        }

        setupBottomSheet()
        setupLayout()

        overlay.setOnClickListener {
            bottomSheetBehavior.hidden()
        }

        //region HomeViewModel

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.currentSeason) {
            season.text = it.toString()
        }

        observeEvent(viewModel.outputs.openSeasonList) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        observeEvent(viewModel.outputs.openRace) { (season, round) ->
            startActivity(RaceActivity.intent(this, season,  round))
        }

        //endregion

        //region SeasonViewModel

        observe(seasonViewModel.outputs.list) {
            seasonAdapter.list = it
        }

        observeEvent(seasonViewModel.outputs.showSeasonEvent) {
            bottomSheetBehavior.hidden()
            optionsList.scrollToPosition(0)
            viewModel.inputs.selectSeason(it)
        }

        //endregion

    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.hidden()
        bottomSheetBehavior.halfExpandedRatio = 0.4f
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
                SeasonListItem.Top -> null
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

    private fun setupLayout() {
        val appBarBehavior = (appbar.layoutParams as? CoordinatorLayout.LayoutParams)?.behavior as? AppBarLayout.Behavior
        appBarBehavior?.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN
            }
        })
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
}