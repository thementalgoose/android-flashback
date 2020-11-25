package tmg.flashback.rss.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings_news.*
import tmg.flashback.rss.R
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.rss.base.RSSBaseActivity
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.rss.ui.configure.RSSConfigureActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class RSSSettingsActivity : RSSBaseActivity() {

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

    companion object {

        fun intent(context: Context): Intent {
            val intent = Intent(context, RSSSettingsActivity::class.java)
            return intent
        }
    }
}