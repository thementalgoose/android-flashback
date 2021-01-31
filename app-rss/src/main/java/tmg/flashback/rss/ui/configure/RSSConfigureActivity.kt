package tmg.flashback.rss.ui.configure

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_rss_configure.*
import kotlinx.android.synthetic.main.activity_rss_configure.header
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.rss.R
import tmg.utilities.extensions.managerClipboard
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class RSSConfigureActivity : BaseActivity() {

    private val viewModel: RSSConfigureViewModel by viewModel()

    override val analyticsScreenName: String
        get() = "RSS Configure"

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
            customAddItem = viewModel.inputs::addCustomItem,
            visitWebsite = viewModel.inputs::visitWebsite
        )

        configuration.adapter = adapter
        configuration.layoutManager = LinearLayoutManager(this)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openWebsite) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.article.contactLink))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Snackbar.make(configuration, R.string.rss_configure_cannot_open_page, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.rss_configure_cannot_open_page_action) { view ->
                            val clipData = ClipData.newPlainText(it.article.title, it.article.contactLink ?: it.article.source)
                            applicationContext.managerClipboard.setPrimaryClip(clipData)
                        }
                        .show()
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }

    companion object {

        fun intent(context: Context): Intent {
            return Intent(context, RSSConfigureActivity::class.java)
        }
    }
}