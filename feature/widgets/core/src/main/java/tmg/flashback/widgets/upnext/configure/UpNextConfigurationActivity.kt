package tmg.flashback.widgets.upnext.configure

import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.observe

@AndroidEntryPoint
class UpNextConfigurationActivity: BaseActivity() {

    private val appWidgetId by lazy {
        intent?.extras?.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID) ?: INVALID_APPWIDGET_ID
    }

    private val viewModel: UpNextConfigurationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val value = Intent().putExtra(EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, value)

        viewModel.inputs.load(appWidgetId)

        setContent {
            AppTheme {
                UpNextConfigurationScreenVM(
                    viewModel = viewModel,
                    actionUpClicked = { finish() },
                    saveClicked = {
                        val resultValue = Intent().putExtra(EXTRA_APPWIDGET_ID, appWidgetId)
                        setResult(RESULT_OK, resultValue)
                        finish()
                    }
                )
            }
        }
    }

    override fun onStop() {
        viewModel.inputs.update()
        super.onStop()
    }
}