package tmg.flashback.home.static

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home_static.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.admin.lockout.LockoutActivity
import tmg.flashback.base.BaseActivity
import tmg.flashback.home.trackpicker.TrackPickerBottomSheetFragment
import tmg.flashback.home.trackpicker.TrackPickerCallback
import tmg.flashback.season.race.RaceAdapter
import tmg.flashback.season.race.RaceAdapterCallback
import tmg.flashback.season.race.RaceAdapterType
import tmg.flashback.season.race.RaceViewModel
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.flashback.utils.*
import tmg.utilities.extensions.initToolbar
import tmg.utilities.extensions.startActivityClearStack
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible

class HomeStaticActivity : BaseActivity(), RaceAdapterCallback, TrackPickerCallback {

    private val viewModel: HomeStaticViewModel by viewModel()
    private val raceViewModel: RaceViewModel by viewModel()

    private val RECYCLER_ALPHA: Float = 0.5f
    private val RECYCLER_VIEW_DURATION: Long = 200

    private lateinit var raceAdapter: RaceAdapter

    override fun layoutId(): Int = R.layout.activity_home_static

    override fun initViews() {

        initToolbar(R.id.toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        raceAdapter = RaceAdapter(this)
        rvData.adapter = raceAdapter
        rvData.layoutManager = LinearLayoutManager(this)

        rvData.onScroll {
            if (it && !fabTrackList.isOrWillBeShown) {
                fabTrackList.show()
            }
            else if (!it && !fabTrackList.isOrWillBeHidden) {
                fabTrackList.hide()
            }
        }

        // VM

        observeViewModel()
    }

    fun observeViewModel() {

        // Inputs

        fabTrackList.setOnClickListener {
            viewModel.inputs.clickTrackList()
        }

        bnvNavigation
            .setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_race -> {
                        raceViewModel.inputs.orderBy(RaceAdapterType.RACE)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.nav_qualifying -> {
                        raceViewModel.inputs.orderBy(RaceAdapterType.QUALIFYING_POS)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.nav_settings -> {
                        startActivity(Intent(applicationContext, SettingsActivity::class.java))
                        return@setOnNavigationItemSelectedListener false
                    }
                    else -> { }
                }
                return@setOnNavigationItemSelectedListener false

            }

        // Outputs

        observeEvent(viewModel.outputs.showReleaseNotes) {
            val instance = ReleaseBottomSheetFragment()
            instance.show(supportFragmentManager, "Release Notes")
        }

        observeEvent(viewModel.outputs.openTrackList) { (season, round) ->
            val bsFragment = TrackPickerBottomSheetFragment.instance(season, round)
            bsFragment.show(supportFragmentManager, "TRACK LIST")
        }

        observe(viewModel.outputs.circuitInfo) {
            imgCountry.setImageResource(getFlagResourceAlpha3(it.countryKey))
            tvCountry.text = it.country
            tvTrackName.text = it.circuitName
        }

        observe(viewModel.outputs.showSeasonRound) { (season, round) ->
            localLog("Showing season round $season $round")
            raceViewModel.inputs.initialise(season, round)
        }

        observe(raceViewModel.outputs.items) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        observe(raceViewModel.outputs.date) {
            tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }

        observe(raceViewModel.outputs.loading) {
            if (it) {
                progressBar.visible()
                rvData.animate()
                    .alpha(RECYCLER_ALPHA)
                    .setDuration(RECYCLER_VIEW_DURATION)
                    .start()
            } else {
                progressBar.invisible()
                rvData.animate()
                    .alpha(1.0f)
                    .setDuration(RECYCLER_VIEW_DURATION)
                    .start()
            }
        }

        observeEvent(viewModel.outputs.showAppLockoutMessage) {
            startActivityClearStack(Intent(this, LockoutActivity::class.java))
        }
    }

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        raceViewModel.inputs.orderBy(adapterType)
    }

    //endregion

    //region TrackPickerCallback

    override fun updateSelected(season: Int, round: Int) {
        viewModel.inputs.select(season, round)
    }

    //endregion

}