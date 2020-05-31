package tmg.flashback.standings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_standings.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.standings.StandingsTabType.CONSTRUCTOR
import tmg.flashback.standings.StandingsTabType.DRIVER
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class StandingsActivity: BaseActivity() {

    private val viewModel: StandingsViewModel by viewModel()
    private lateinit var adapter: StandingsAdapter
    private var season: Int = -1

    override fun layoutId(): Int = R.layout.activity_standings

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        season = bundle.getInt(keySeason)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = StandingsAdapter(
            closed = viewModel.inputs::clickBack
        )
        adapter.list = listOf(
            StandingsItem.Header(season, 0, 0),
            StandingsItem.Skeleton
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_driver -> viewModel.inputs.clickType(DRIVER)
                R.id.nav_constructor -> viewModel.inputs.clickType(CONSTRUCTOR)
            }
            return@setOnNavigationItemSelectedListener true
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }

        viewModel.inputs.initForSeason(season)
    }

    companion object {

        private const val keySeason: String = "SEASON"

        fun intent(context: Context, season: Int): Intent {
            val intent = Intent(context, StandingsActivity::class.java)
            intent.putExtra(keySeason, season)
            return intent
        }
    }
}