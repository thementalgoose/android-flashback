package tmg.flashback.debug.core.adverts

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ads.components.NativeBanner
import tmg.flashback.debug.core.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header

@AndroidEntryPoint
class AdvertsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                DebugScreen(actionUpClicked = { finish() }) {
                    TextTitle(
                        text = "Native Banner",
                        modifier = Modifier.padding(vertical = AppTheme.dimens.medium)
                    )
                    NativeBanner()
                    TextTitle(
                        text = "Native Banner (offset)",
                        modifier = Modifier.padding(vertical = AppTheme.dimens.medium)
                    )
                    NativeBanner(badgeOffset = true)
                }
            }
        }
    }

    @Composable
    private fun DebugScreen(
        actionUpClicked: () -> Unit,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                text = "Debug",
                icon = painterResource(id = R.drawable.ic_back),
                iconContentDescription = null,
                actionUpClicked = actionUpClicked
            )
            Column(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.medium
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                this.content()
            }
        }
    }
}