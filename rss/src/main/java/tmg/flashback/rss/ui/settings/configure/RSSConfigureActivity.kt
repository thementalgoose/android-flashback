package tmg.flashback.rss.ui.settings.configure

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class RSSConfigureActivity: BaseActivity() {

    private val viewModel: RSSConfigureViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val content = viewModel.outputs.list.observeAsState()
                RSSConfigureLayout(
                    state = content.value ?: emptyList(),
                    removeItem = viewModel.inputs::removeItem,
                    addQuickLink = viewModel.inputs::addQuickItem,
                    addCustom = viewModel.inputs::addCustomItem,
                    websiteClicked = viewModel.inputs::visitWebsite
                )
            }
        }
    }
}