package tmg.flashback.news

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class NewsActivity: BaseActivity() {

    private val viewModel: NewsViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_news

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(viewModel.outputs.news) {
            test.text = it.map { it.toString() }.joinToString(separator = "\n\n")
        }
    }
}