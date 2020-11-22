package tmg.flashback.rss.settings

import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings_news.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.rss.configure.RSSConfigureActivity
import tmg.flashback.settings.SettingsAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.toEnum

class RSSSettingsActivity: BaseActivity() {

    private val viewModel: RSSSettingsViewModel by viewModel()

    private lateinit var adapter: SettingsAdapter

    override fun layoutId(): Int = R.layout.activity_settings_news

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = getString(R.string.settings_rss_title)

        adapter = SettingsAdapter(
            prefClicked = viewModel.inputs::clickPref,
            prefSwitchClicked = viewModel.inputs::updatePref
        )
        rvSettings.adapter = adapter
        rvSettings.layoutManager = LinearLayoutManager(this)

        back.setOnClickListener { onBackPressed() }

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goToConfigure) {
            startActivity(RSSConfigureActivity.intent(this))
        }
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        rvSettings.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
    }
}