package tmg.flashback.standings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_standings.*
import tmg.flashback.R
import tmg.flashback.base.BaseActivity

class StandingsActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_standings

    private lateinit var adapter: StandingsAdapter
    private var season: Int = -1

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        season = bundle.getInt(keySeason)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = StandingsAdapter()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)


    }

    companion object {

        private const val keySeason: String = "SEASON"

        fun intent(context: Context, season: Int): Intent {
            val intent: Intent = Intent(context, StandingsActivity::class.java)
            intent.putExtra(keySeason, season)
            return intent
        }
    }
}