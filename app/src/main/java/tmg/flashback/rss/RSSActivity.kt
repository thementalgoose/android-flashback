package tmg.flashback.rss

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_rss.*
import me.saket.inboxrecyclerview.dimming.TintPainter
import me.saket.inboxrecyclerview.page.PageStateChangeCallbacks
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.rss.configure.RSSConfigureActivity
import tmg.flashback.rss.settings.RSSSettingsActivity
import tmg.flashback.utils.FragmentRequestBack
import tmg.flashback.web.WebFragment
import tmg.utilities.extensions.loadFragment
import tmg.utilities.extensions.observe

class RSSActivity: BaseActivity(), PageStateChangeCallbacks, FragmentRequestBack {

    private val viewModel: RSSViewModel by viewModel()

    private lateinit var adapter: RSSAdapter

    override fun layoutId(): Int = R.layout.activity_rss

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.setText(R.string.title_rss)

        loadFragment(WebFragment.instance(getString(R.string.app_name), "about"), R.id.expandablePageLayout, "WebView")

        expandablePageLayout.pullToCollapseEnabled = false

        adapter = RSSAdapter(
            openConfigure = {
                startActivity(Intent(this, RSSConfigureActivity::class.java))
            },
            articleClicked = { article, id ->
                if (prefsDB.newsOpenInExternalBrowser) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                    startActivity(intent)
                }
                else {
                    list.expandItem(id)
                    loadFragment(
                        WebFragment.instance(article.title, article.link),
                        R.id.expandablePageLayout,
                        "WebView"
                    )
                }
            }
        )
        list.tintPainter = TintPainter.uncoveredArea(Color.WHITE, opacity = 0.65f)
        list.expandablePage = expandablePageLayout
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
        when {
            expandablePageLayout.isExpandedOrExpanding -> {
                // Pipe back exit events through to fragment, let it handle the back event
                val webFrag = supportFragmentManager.findFragmentByTag("WebView") as? WebFragment
                webFrag?.exitWeb()
                list.collapse()
            }
            else -> super.onBackPressed()
        }
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        list.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
        expandablePageLayout.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
    }

    //region FragmentRequestBack

    override fun fragmentBackPressed() {
        onBackPressed()
    }

    //endregion

    //region PageStateChangeCallbacks

    override fun onPageAboutToCollapse(collapseAnimDuration: Long) { }

    override fun onPageAboutToExpand(expandAnimDuration: Long) { }

    override fun onPageCollapsed() { }

    override fun onPageExpanded() { }

    //endregion
}