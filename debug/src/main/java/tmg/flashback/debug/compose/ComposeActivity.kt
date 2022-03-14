package tmg.flashback.debug.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.components.layouts.Screen

class ComposeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Screen(
                    title = "TEST"
                ) {
                    items(17) {
                        TextBody1(
                            text = "I'm item $it",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}