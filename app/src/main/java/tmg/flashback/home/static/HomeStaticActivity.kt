package tmg.flashback.home.static

import android.content.Intent
import android.graphics.ColorFilter
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import kotlinx.android.synthetic.main.activity_home_static.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.layout_home_error.view.*
import kotlinx.android.synthetic.main.layout_home_loading.view.*
import kotlinx.android.synthetic.main.layout_home_unavailable.view.*
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
import tmg.flashback.settings.SettingsActivity
import tmg.flashback.settings.release.ReleaseBottomSheetFragment
import tmg.flashback.utils.*
import tmg.utilities.extensions.startActivityClearStack
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible

class HomeStaticActivity : BaseActivity(), RaceAdapterCallback, TrackPickerCallback {

    private val viewModel: HomeStaticViewModel by viewModel()

    private val RECYCLER_ALPHA: Float = 0.15f
    private val RECYCLER_VIEW_DURATION: Long = 200

    private var screenState: HomeStaticScreenState = HomeStaticScreenState.LOADING
        private set(value) {
            if (value != field) {
                animateRv(value == HomeStaticScreenState.DATA)
                animateLoading(value == HomeStaticScreenState.LOADING)
                animateError(value == HomeStaticScreenState.ERROR)
                animateUnavailable(value == HomeStaticScreenState.NOT_AVAILABLE)

                if (value == HomeStaticScreenState.LOADING) {
                    rvData.smoothScrollToPosition(0)
                }
            }
            field = value
        }


    private lateinit var raceAdapter: RaceAdapter

    override fun layoutId(): Int = R.layout.activity_home_static

    override fun initViews() {

        raceAdapter = RaceAdapter(this)
        rvData.adapter = raceAdapter
        rvData.layoutManager = LinearLayoutManager(this)

        initialiseLottie(layoutLoading.lottieLoading)
        initialiseLottie(layoutError.lottieTryAgain)
        initialiseLottie(layoutUnavailable.lottieNoData)

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
                        viewModel.inputs.orderBy(RaceAdapterType.RACE)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.nav_qualifying -> {
                        viewModel.inputs.orderBy(RaceAdapterType.QUALIFYING_POS)
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
            layoutHeader.tvRound.text = getString(R.string.round_number, it.season, it.round)
        }

        observeEvent(viewModel.outputs.showAppLockoutMessage) {
            startActivityClearStack(Intent(this, LockoutActivity::class.java))
        }

        observe(viewModel.outputs.items) { (adapterType, list) ->
            raceAdapter.update(adapterType, list)
        }

        observe(viewModel.outputs.date) {
            layoutHeader.tvDate.text = it.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }

        observe(viewModel.outputs.homeScreenState) {
            screenState = it

            // TODO: This is not the place to fix this bug, look into why `items` output is not being fired
            if (it == HomeStaticScreenState.NOT_AVAILABLE) {
                raceAdapter.update(raceAdapter.viewType, emptyList())
            }
        }
    }

    private fun initialiseLottie(lottieView: LottieAnimationView) {

        val colorFilter = SimpleColorFilter(theme.getColor(R.attr.f1Loading))
        val keyPath = KeyPath("**")
        val callback = LottieValueCallback<ColorFilter>(colorFilter)
        lottieView.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
    }

    //region View animations

    private fun animateError(into: Boolean) {
        if (into) {
            layoutError.visible()
            layoutError.lottieTryAgain.playAnimation()
        }
        else {
            layoutError.gone()
            layoutError.lottieTryAgain.progress = 0.0f
        }
    }

    private fun animateLoading(into: Boolean) {
        if (into) {
            layoutLoading.visible()
            layoutLoading.animate()
                .alpha(1.0f)
                .setDuration(RECYCLER_VIEW_DURATION)
                .start()
        }
        else {
            layoutLoading.gone()
            layoutLoading.alpha = 0.0f
        }
    }

    private fun animateRv(into: Boolean) {
        rvData.animate()
            .alpha(if (into) 1.0f else RECYCLER_ALPHA)
            .setDuration(RECYCLER_VIEW_DURATION)
            .start()
    }

    private fun animateUnavailable(into: Boolean) {
        if (into) {
            layoutUnavailable.visible()
            layoutUnavailable.lottieNoData.playAnimation()
        }
        else {
            layoutUnavailable.gone()
            layoutUnavailable.lottieNoData.progress = 0.0f
        }
    }

    //endregion

    //region RaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        viewModel.inputs.orderBy(adapterType)
    }

    //endregion

    //region TrackPickerCallback

    override fun updateSelected(season: Int, round: Int) {
        viewModel.inputs.select(season, round)
    }

    //endregion

}