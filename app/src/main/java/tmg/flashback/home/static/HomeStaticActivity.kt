package tmg.flashback.home.static

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import kotlinx.android.synthetic.main.activity_home_static.*
import kotlinx.android.synthetic.main.layout_header.view.*
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
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.visible

class HomeStaticActivity : BaseActivity(), RaceAdapterCallback, TrackPickerCallback {

    private val viewModel: HomeStaticViewModel by viewModel()
    private val raceViewModel: RaceViewModel by viewModel()

    private val RECYCLER_ALPHA: Float = 0.15f
    private val RECYCLER_VIEW_DURATION: Long = 200

    private lateinit var raceAdapter: RaceAdapter

    override fun layoutId(): Int = R.layout.activity_home_static

    override fun initViews() {

        raceAdapter = RaceAdapter(this)
        rvData.adapter = raceAdapter
        rvData.layoutManager = LinearLayoutManager(this)

        initialiseLottie(lottieLoading)
        initialiseLottie(tryAgainView)

        layoutHeader.tvTitle.text = getString(R.string.app_name)

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
            layoutHeader.imgCountry.setImageResource(getFlagResourceAlpha3(it.countryKey))
            layoutHeader.tvCountry.text = it.country
            layoutHeader.tvTrackName.text = it.circuitName
        }

        observe(viewModel.outputs.showSeasonRound) { (season, round) ->
            localLog("Showing season round $season $round")
            raceViewModel.inputs.initialise(season, round)
        }

        observe(raceViewModel.outputs.items) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        observe(raceViewModel.outputs.date) {
            layoutHeader.tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }

        observe(raceViewModel.outputs.loading) {
            if (it) {
                showLoading()
            } else {
                showData()
            }
        }

        observeEvent(viewModel.outputs.showAppLockoutMessage) {
            startActivityClearStack(Intent(this, LockoutActivity::class.java))
        }
    }

    private fun showError() {

        animateRv(false)
        animateLoading(false)
        animateTryAgain(true)
    }

    private fun showLoading() {
        animateRv(false)
        animateLoading(true)
        animateTryAgain(false)
    }

    private fun showData() {
        animateRv(true)
        animateLoading(false)
        animateTryAgain(false)
    }

    private fun animateTryAgain(into: Boolean) {
        if (into) {
            clTryAgain.visible()
            tryAgainView.playAnimation()
        }
        else {
            clTryAgain.gone()
            tryAgainView.progress = 0.0f
        }
    }

    private fun animateLoading(into: Boolean) {
        if (into) {
            clLoading.visible()
            clLoading.animate()
                .alpha(1.0f)
                .setDuration(RECYCLER_VIEW_DURATION)
                .start()
        }
        else {
            clLoading.gone()
            clLoading.alpha = 0.0f
        }
    }

    private fun animateRv(into: Boolean) {
        rvData.animate()
            .alpha(if (into) 1.0f else RECYCLER_ALPHA)
            .setDuration(RECYCLER_VIEW_DURATION)
            .start()
    }

    private fun initialiseLottie(lottieView: LottieAnimationView) {

        val colorFilter: SimpleColorFilter = SimpleColorFilter(theme.getColor(R.attr.f1Loading))
        val keyPath: KeyPath = KeyPath("**")
        val callback = LottieValueCallback<ColorFilter>(colorFilter)
        lottieView.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
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