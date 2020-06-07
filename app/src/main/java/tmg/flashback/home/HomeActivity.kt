package tmg.flashback.home

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.home.list.HomeAdapter
import tmg.flashback.home.season.SeasonBottomSheetFragment
import tmg.flashback.home.season.SeasonRequestedCallback
import tmg.flashback.race.RaceActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent


class HomeActivity : BaseActivity(), SeasonRequestedCallback {

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var adapter: HomeAdapter

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

        setupLayout()

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.currentSeason) {
            season.text = it.toString()
        }

        observeEvent(viewModel.outputs.openSeasonList) {
            SeasonBottomSheetFragment().show(supportFragmentManager, "SEASON")
        }

        observeEvent(viewModel.outputs.openRace) { (season, round) ->
            startActivity(RaceActivity.intent(this, season,  round))
        }
    }

    private fun setupLayout() {

        // Make the status bar transparent
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        // Set the status bar padding
        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            menu.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            dataList.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    //region SeasonRequestedCallback

    override fun seasonRequested(season: Int) {
        viewModel.inputs.selectSeason(season)
    }

    //endregion

}