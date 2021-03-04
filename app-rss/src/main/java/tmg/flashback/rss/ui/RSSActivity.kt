package tmg.flashback.rss.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.ActivityRssBinding
import tmg.flashback.rss.prefs.RSSRepository
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.ui.settings.InitialScreen
import tmg.flashback.rss.ui.settings.RSSSettingsActivity
import tmg.flashback.rss.web.FragmentRequestBack
import tmg.flashback.rss.web.WebFragment
import tmg.utilities.extensions.observe

class RSSActivity: BaseActivity(), FragmentRequestBack {

    private lateinit var binding: ActivityRssBinding
    private val viewModel: RSSViewModel by viewModel()

    private val repository: RSSRepository by inject()

    override val analyticsScreenName: String
        get() = "RSS Feed"

    private lateinit var adapter: RSSAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRssBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.setText(R.string.title_rss)

        adapter = RSSAdapter(
            openConfigure = {
                startActivityForResult(RSSSettingsActivity.intent(this, InitialScreen.CONFIGURE), REQUEST_CODE)
            },
            articleClicked = { article, id ->
                if (repository.newsOpenInExternalBrowser) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                    startActivity(intent)
                } else {
                    loadWebView(article)
                }
            }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.settings.setOnClickListener {
            startActivity(RSSSettingsActivity.intent(this, InitialScreen.SETTINGS))
        }

        binding.swipeContainer.setOnRefreshListener {
            viewModel.inputs.refresh()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isRefreshing) {
            binding.swipeContainer.isRefreshing = it
        }
    }

    override fun onBackPressed() {
        val webFrag = supportFragmentManager.findFragmentByTag("WebView") as? WebFragment
        if (webFrag != null) {
            webFrag.exitWeb()
            supportFragmentManager.popBackStack()
            swipeDismissLock = false
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            viewModel.inputs.refresh()
        }
    }

    private fun loadWebView(article: Article) {
        swipeDismissLock = true
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.in_from_bottom, -1, -1, R.anim.out_to_bottom)
                .replace(R.id.webContainer, WebFragment.instance(article.title, article.link), "WebView")
                .addToBackStack(null)
                .commit()
    }

    //region FragmentRequestBack

    override fun fragmentBackPressed() {
        onBackPressed()
    }

    //endregion

    companion object {

        private const val REQUEST_CODE = 1001

        fun intent(context: Context): Intent {
            val intent = Intent(context, RSSActivity::class.java)
            return intent
        }
    }
}