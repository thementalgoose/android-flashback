package tmg.f1stats.season.race

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_season_race.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.base.BaseFragment
import tmg.utilities.extensions.subscribeNoError

class SeasonRaceFragment: BaseFragment(), SeasonRaceAdapterCallback {

    private var season: Int = -1
    private var round: Int = -1

    private val viewModel: SeasonRaceViewModel by viewModel()

    private lateinit var adapter: SeasonRaceAdapter

    override fun layoutId(): Int = R.layout.fragment_season_race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            season = it.getInt(keySeason)
            round = it.getInt(keyRound)
        }
    }

    override fun initViews() {
        adapter = SeasonRaceAdapter(this)
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(context)

        viewModel.inputs.initialise(season, round)
    }

    override fun observeViewModel() {

        viewModel.outputs
            .items()
            .subscribeNoError {
                adapter.update(SeasonRaceAdapterType.RACE, it)
            }
            .autoDispose()
    }

    //region SeasonRaceAdapterCallback

    override fun orderBy(adapterType: SeasonRaceAdapterType) {
        viewModel.inputs.orderBy(adapterType)
    }

    //endregion

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