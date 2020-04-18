package tmg.flashback.home.static

import android.content.Intent
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_home_static.*
import kotlinx.android.synthetic.main.bottom_sheet_track_picker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.home.trackpicker.TrackPickerBottomSheetFragment
import tmg.flashback.home.trackpicker.TrackPickerCallback
import tmg.flashback.home.trackpicker.TrackPickerRoundAdapter
import tmg.flashback.home.trackpicker.TrackPickerYearAdapter
import tmg.flashback.season.race.RaceAdapter
import tmg.flashback.season.race.RaceAdapterCallback
import tmg.flashback.season.race.RaceAdapterType
import tmg.flashback.season.race.RaceViewModel
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.flashback.utils.*
import tmg.utilities.extensions.*
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible

class HomeStaticActivity : BaseActivity(), RaceAdapterCallback, TrackPickerCallback {

    private val viewModel: HomeStaticViewModel by viewModel()
    private val raceViewModel: RaceViewModel by viewModel()

    private lateinit var raceAdapter: RaceAdapter

    private lateinit var trackBottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var seasonAdapter: TrackPickerYearAdapter
    private lateinit var roundAdapter: TrackPickerRoundAdapter

    private lateinit var skeleton: Skeleton

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

        // Bottom Sheet

        trackBottomSheetBehavior = BottomSheetBehavior.from(bsTrackPicker)
        trackBottomSheetBehavior.isHideable = true
        trackBottomSheetBehavior.peekHeight = 300f.dpToPx(resources).toInt()
        trackBottomSheetBehavior.hidden()
        trackBottomSheetBehavior.addBottomSheetCallback(BottomSheetFader(vBackground, "Track") { id ->
            trackBottomSheetBehavior.hidden()
        })

        seasonAdapter = TrackPickerYearAdapter()
        seasonAdapter.addListener { year ->
            viewModel.inputs.browse(year)
        }
        rvSeasons.adapter = seasonAdapter
        rvSeasons.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        roundAdapter = TrackPickerRoundAdapter()
        roundAdapter.addListener { model ->
            Log.i("F1", "Selecting ${model.season} / ${model.round}")
            viewModel.inputs.select(model.season, model.round)
            trackBottomSheetBehavior.hidden()
        }
        rvRounds.adapter = roundAdapter
        rvRounds.layoutManager = LinearLayoutManager(this)

        // Skeleton Views

//        skeleton = rvData.applySkeleton(R.layout.skeleton_race, 1, cornerRadius = 2f.dpToPx(resources))
//        skeleton.showSkeleton()

        observeViewModel()
    }

    fun observeViewModel() {

        // Inputs

        fabTrackList.setOnClickListener {
            viewModel.inputs.clickTrackList()
        }

        imgbtnClose.setOnClickListener {
            viewModel.inputs.closeTrackList()
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

        observe(viewModel.outputs.switchYearList) {
            seasonAdapter.list = it
        }

        observe(viewModel.outputs.switchTrackList) {
            roundAdapter.list = it
        }

        observe(viewModel.outputs.showSeasonRound) { (season, round) ->
            raceViewModel.inputs.initialise(season, round)
        }

        observe(raceViewModel.outputs.items) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        observe(raceViewModel.outputs.date) {
            tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }

        observeEvent(raceViewModel.outputs.loading) {
            if (it) {
                progressBar.visible()
//                skeleton.showSkeleton()
            } else {
                progressBar.invisible()
//                skeleton.showOriginal()
            }
        }
    }

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        raceViewModel.inputs.orderBy(adapterType)
    }

    //endregion

    //region TrackPickerCallback

    override fun updateSelected(season: Int, round: Int) {

    }

    //endregion

}