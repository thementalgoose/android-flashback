package tmg.flashback.home

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.home.list.HomeAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class HomeActivity: BaseActivity() {

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var adapter: HomeAdapter

    override fun layoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HomeAdapter()
        dataList.adapter = adapter
        dataList.layoutManager = LinearLayoutManager(this)

        menu.setOnNavigationItemSelectedListener {
            val shouldUpdateTab = when (it.itemId) {
                R.id.nav_calendar -> {
                    viewModel.inputs.clickItem(HomeMenuItem.CALENDAR)
                    true
                }
                R.id.nav_drivers -> {
                    viewModel.inputs.clickItem(HomeMenuItem.DRIVERS)
                    true
                }
                R.id.nav_constructor -> {
                    viewModel.inputs.clickItem(HomeMenuItem.CONSTRUCTORS)
                    true
                }
                R.id.nav_seasons -> {
                    viewModel.inputs.clickItem(HomeMenuItem.SEASONS)
                    false
                }
                else -> false
            }

            return@setOnNavigationItemSelectedListener shouldUpdateTab
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.currentSeason) {
            season.text = it.toString()
        }

        observeEvent(viewModel.outputs.openSeasonList) {
            Toast.makeText(this, "Open season list", Toast.LENGTH_SHORT).show()
        }
    }

}