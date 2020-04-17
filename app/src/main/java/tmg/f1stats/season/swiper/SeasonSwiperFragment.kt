package tmg.f1stats.season.swiper

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseFragment
import tmg.f1stats.utils.observe
import tmg.utilities.extensions.subscribeNoError
import tmg.utilities.extensions.views.setPageWidth
import tmg.utilities.extensions.views.syncScrolling
import kotlin.math.abs

class SeasonSwiperFragment: BaseFragment() {

    private lateinit var raceAdapterSwiper: SeasonSwiperPagerAdapter
    private lateinit var trackAdapter: SeasonTrackAdapter
    private var season: Int = -1

    private val viewModel: SeasonViewModel by viewModel()

    override fun layoutId(): Int = R.layout.fragment_season

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            season = it.getInt(keyYear)
        }
    }

    override fun initViews() {
        raceAdapterSwiper =
            SeasonSwiperPagerAdapter(this)
        vpRace.adapter = raceAdapterSwiper

        trackAdapter = SeasonTrackAdapter()
        vpTracks.adapter = trackAdapter
        vpTracks.setPageWidth(seasonTrackScreenMultiplier)

        vpTracks.syncScrolling(vpRace,
            seasonTrackScreenMultiplier
        )
        vpRace.syncScrolling(vpTracks, 1 / seasonTrackScreenMultiplier)

        vpTracks.setPageTransformer { page, position ->
            page.alpha = 1 - abs(position * seasonTrackScreenAlpha)
        }

        viewModel.inputs.initialise(season)

        observe(viewModel.outputs.seasonRounds) {
            raceAdapterSwiper.list = it
            trackAdapter.list = it
        }
    }

    companion object {

        private const val seasonTrackScreenAlpha = 0.4f
        private const val seasonTrackScreenMultiplier = 1 / 2f
        private const val keyYear: String = "keyYear"

        fun newInstance(year: Int): SeasonSwiperFragment {
            val bundle: Bundle = Bundle()
            bundle.putInt(keyYear, year)
            val instance: SeasonSwiperFragment =
                SeasonSwiperFragment()
            instance.arguments = bundle
            return instance
        }
    }
}