package tmg.flashback.season.race

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_season_race.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.flashback.utils.observe

class RaceFragment: BaseFragment(), RaceAdapterCallback {

    private var season: Int = -1
    private var round: Int = -1

    private val viewModel: RaceViewModel by viewModel()

    private lateinit var adapter: RaceAdapter

    override fun layoutId(): Int = R.layout.fragment_season_race

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            season = it.getInt(keySeason)
            round = it.getInt(keyRound)
        }
    }

    override fun initViews() {
        adapter = RaceAdapter(this)
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(context)

        viewModel.inputs.initialise(season, round)

        observe(viewModel.outputs.items) { (adapterType, list) ->
            adapter.update(adapterType, list)
        }
    }

    //region SeasonRaceAdapterCallback

    override fun orderBy(adapterType: RaceAdapterType) {
        viewModel.inputs.orderBy(adapterType)
    }

    //endregion

    companion object {

        private const val keySeason: String = "keySeason"
        private const val keyRound: String = "keyRound"

        fun instance(season: Int, round: Int): RaceFragment {
            val instance: RaceFragment =
                RaceFragment()
            val bundle: Bundle = Bundle()
            bundle.putInt(keySeason, season)
            bundle.putInt(keyRound, round)
            instance.arguments = bundle
            return instance
        }
    }
}