package tmg.f1stats.home.season

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseFragment
import tmg.utilities.extensions.subscribeNoError
import tmg.utilities.extensions.views.setPageWidth
import tmg.utilities.extensions.views.syncScrolling
import kotlin.math.abs

class SeasonFragment: BaseFragment() {

    private lateinit var raceAdapter: SeasonPagerAdapter
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
        raceAdapter = SeasonPagerAdapter(this)
        vpRace.adapter = raceAdapter

        trackAdapter = SeasonTrackAdapter()
        vpTracks.adapter = trackAdapter
        vpTracks.setPageWidth(seasonTrackScreenMultiplier)

        vpTracks.syncScrolling(vpRace, seasonTrackScreenMultiplier)
        vpRace.syncScrolling(vpTracks, 1 / seasonTrackScreenMultiplier)

        vpTracks.setPageTransformer { page, position ->
            page.alpha = 1 - abs(position * seasonTrackScreenAlpha)
        }
    }

    override fun observeViewModel() {

        viewModel.outputs.seasonRounds()
            .subscribeNoError {
                println(it)
                raceAdapter.list = it
                trackAdapter.list = it
            }
            .autoDispose()

        viewModel.inputs.initialise(season)

    }

    companion object {

        private const val seasonTrackScreenAlpha = 0.4f
        private const val seasonTrackScreenMultiplier = 2 / 5f
        private const val keyYear: String = "keyYear"

        fun newInstance(year: Int): SeasonFragment {
            val bundle: Bundle = Bundle()
            bundle.putInt(keyYear, year)
            val instance: SeasonFragment = SeasonFragment()
            instance.arguments = bundle
            return instance
        }
    }
}