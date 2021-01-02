package tmg.flashback.rss.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_rss.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.base.RSSBaseActivity
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.ui.configure.RSSConfigureActivity
import tmg.flashback.rss.ui.settings.RSSSettingsActivity
import tmg.flashback.rss.web.FragmentRequestBack
import tmg.flashback.rss.web.WebFragment
import tmg.utilities.extensions.observe

class RSSActivity: RSSBaseActivity(), FragmentRequestBack {

    private val viewModel: RSSViewModel by viewModel()

    private lateinit var adapter: RSSAdapter

    override fun layoutId(): Int = R.layout.activity_rss

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.setText(R.string.title_rss)

        adapter = RSSAdapter(
            openConfigure = {
                startActivity(Intent(this, RSSConfigureActivity::class.java))
            },
            articleClicked = { article, id ->
                if (prefsRepository.newsOpenInExternalBrowser) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                    startActivity(intent)
                } else {
                    loadWebView(article)
                }
            }
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        back.setOnClickListener {
            onBackPressed()
        }

        settings.setOnClickListener {
            startActivity(Intent(this, RSSSettingsActivity::class.java))
        }

        swipeContainer.setOnRefreshListener {
            viewModel.inputs.refresh()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isRefreshing) {
            swipeContainer.isRefreshing = it
        }
    }

    override fun onBackPressed() {

        val webFrag = supportFragmentManager.findFragmentByTag("WebView") as? WebFragment
        if (webFrag != null) {
            webFrag.exitWeb()
            supportFragmentManager.popBackStack()
            slidrLock = false
        }
        else {
            super.onBackPressed()
        }
    }

    private fun loadWebView(article: Article) {
        slidrLock = true
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

        fun intent(context: Context): Intent {
            val intent = Intent(context, RSSActivity::class.java)
            return intent
        }
    }
}