package tmg.flashback.rss.ui.web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.ActivityWebBinding
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
fun WebScreenVM(
    title: String,
    url: String,
    actionUpClicked: () -> Unit,
    shareClicked: (url: String) -> Unit,
    openInBrowser: (url: String) -> Unit,
) {
    WebScreen(
        title = title,
        url = url,
        actionUpClicked = actionUpClicked,
        shareClicked = shareClicked,
        openInBrowser = openInBrowser
    )
}

@Composable
fun WebScreen(
    title: String,
    url: String,
    actionUpClicked: () -> Unit,
    shareClicked: (url: String) -> Unit,
    openInBrowser: (url: String) -> Unit,
) {
    Column(Modifier
        .background(AppTheme.colors.backgroundPrimary)
        .fillMaxSize()
    ) {
        val titleValue = remember { mutableStateOf(title) }
        val urlValue = remember { mutableStateOf(url) }

        Row(Modifier.padding(
            vertical = 4.dp
        )) {
            IconButton(
                onClick = actionUpClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_web_close),
                    contentDescription = stringResource(id = R.string.ab_close),
                    tint = AppTheme.colors.contentPrimary
                )
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = AppTheme.dimensions.paddingMedium
                )
            ) {
                TextBody1(
                    modifier = Modifier.padding(top = 4.dp),
                    text = titleValue.value,
                    maxLines = 1
                )
                TextBody2(
                    text = urlValue.value,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            IconButton(onClick = { shareClicked(urlValue.value) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rss_share),
                    contentDescription = stringResource(id = R.string.ab_rss_share),
                    tint = AppTheme.colors.contentPrimary
                )
            }

            IconButton(onClick = { openInBrowser(urlValue.value) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rss_open_in_browser),
                    contentDescription = stringResource(id = R.string.ab_rss_open_in_browser),
                    tint = AppTheme.colors.contentPrimary
                )
            }
        }

        AndroidViewBinding(
            factory = ActivityWebBinding::inflate,
            modifier = Modifier.weight(1f)
        ) {
            val frag = container.getFragment<WebFragment>()
            frag.load(pageTitle = title, url = url)
            frag.callback = object : WebUpdated {
                override fun domainChanged(domain: String) {
                    urlValue.value = domain
                }
                override fun titleChanged(title: String) {
                    titleValue.value = title
                }
            }
        }
    }
}