package tmg.flashback.rss.ui.configure

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_rss_configure.*
import kotlinx.android.synthetic.main.activity_rss_configure.header
import kotlinx.android.synthetic.main.activity_rss_configure.titlebar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.base.RSSBaseActivity
import tmg.utilities.extensions.observe

class RSSConfigureActivity : RSSBaseActivity() {

    private val viewModel: RSSConfigureViewModel by viewModel()

    private lateinit var adapter: RSSConfigureAdapter

    override fun layoutId() = R.layout.activity_rss_configure

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = getString(R.string.rss_configure_title)

        back.setOnClickListener {
            onBackPressed()
        }

        adapter = RSSConfigureAdapter(
            quickAddItem = viewModel.inputs::addQuickItem,
            removeItem = viewModel.inputs::removeItem,
            customAddItem = viewModel.inputs::addCustomItem
        )

        configuration.adapter = adapter
        configuration.layoutManager = LinearLayoutManager(this)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        configuration.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, RSSConfigureActivity::class.java)
        }
    }
}