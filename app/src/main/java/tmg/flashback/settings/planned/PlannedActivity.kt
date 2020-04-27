package tmg.flashback.settings.planned

import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_planned.*
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.planned
import tmg.utilities.extensions.initToolbar

class PlannedActivity: BaseActivity() {

    private lateinit var adapter: PlannedAdapter

    override fun layoutId(): Int = R.layout.activity_planned

    override fun initViews() {

        initToolbar(R.id.toolbar, true, R.drawable.ic_back)

        adapter = PlannedAdapter()
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

        adapter.list = planned
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}