package tmg.f1stats.home.season.race

import android.os.Bundle
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseFragment

class SeasonRaceFragment: BaseFragment() {

    private var season: Int = -1
    private var round: Int = -1

    private val viewModel: SeasonRaceViewModel by viewModel()

    override fun layoutId(): Int = R.layout.fragment_season_race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            season = it.getInt(keySeason)
            round = it.getInt(keyRound)
        }
    }

    override fun observeViewModel() {

    }

    companion object {

        private const val keySeason: String = "keySeason"
        private const val keyRound: String = "keyRound"

        fun instance(season: Int, round: Int): SeasonRaceFragment {
            val instance: SeasonRaceFragment =
                SeasonRaceFragment()
            val bundle: Bundle = Bundle()
            bundle.putInt(keySeason, season)
            bundle.putInt(keyRound, round)
            instance.arguments = bundle
            return instance
        }
    }
}