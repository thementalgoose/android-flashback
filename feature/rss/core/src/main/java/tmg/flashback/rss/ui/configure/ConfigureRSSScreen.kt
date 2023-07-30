@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package tmg.flashback.rss.ui.configure

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.input.InputSwitch
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.bottomsheet.ModalSheet
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Pref
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting

private val badgeSize: Dp = 42.dp

@Composable
fun ConfigureRSSScreenVM(
    actionUpClicked: () -> Unit,
    viewModel: ConfigureRSSViewModel = hiltViewModel()
) {
    ScreenView(screenName = "RSS Configure")
    
    val showDescriptionEnabled = viewModel.outputs.showDescriptionEnabled.collectAsState(true)
    val showCustomAdd = viewModel.outputs.showAddCustom.collectAsState(false)
    val sources = viewModel.outputs.rssSources.collectAsState(emptyList())

    ConfigureRSSScreen(
        actionUpClicked = actionUpClicked,
        showDescriptionEnabled = showDescriptionEnabled.value,
        showDescriptionClicked = viewModel.inputs::clickShowDescription,
        sourceAdded = {
            viewModel.inputs.addItem(it, isChecked = true)
        },
        sourceRemoved = {
            viewModel.inputs.addItem(it, isChecked = false)
        },
        contactLinkClicked = { source ->
            source.supportedArticleSource?.let { viewModel.inputs.visitWebsite(it) }
        },
        showCustomAdd = showCustomAdd.value,
        sources = sources.value,
    )
}

@Composable
fun ConfigureRSSScreen(
    actionUpClicked: () -> Unit,
    showDescriptionEnabled: Boolean,
    showDescriptionClicked: (newState: Boolean) -> Unit,
    sourceAdded: (rssLink: String) -> Unit,
    sourceRemoved: (rssLink: String) -> Unit,
    contactLinkClicked: (link: RSSSource) -> Unit,
    showCustomAdd: Boolean,
    sources: List<RSSSource>
) {
    val customRssBox = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_rss_configure_sources_title),
                    action = HeaderAction.BACK,
                    actionUpClicked = actionUpClicked
                )
            }
            Header(title = R.string.settings_rss_appearance_title)
            Switch(
                model = Setting.Switch(
                    _key = "show_description",
                    title = R.string.settings_rss_show_description_title,
                    subtitle = R.string.settings_rss_show_description_description,
                    isChecked = showDescriptionEnabled
                ),
                onClick = {
                    showDescriptionClicked(!showDescriptionEnabled)
                }
            )

            if (showCustomAdd) {
                Header(title = R.string.settings_rss_add_custom)
                Pref(
                    model = Setting.Pref(
                        _key = "add_custom",
                        title = R.string.settings_rss_add_title,
                        subtitle = R.string.settings_rss_add_description,
                    ),
                    onClick = {
                        customRssBox.value = true
                    }
                )
            }

            Header(title = R.string.settings_rss_configure)
            items(sources, key = { it.url }) {
                Source(
                    model = it,
                    sourceAdded = sourceAdded,
                    sourceRemoved = sourceRemoved,
                    contactLink = {
                        contactLinkClicked(it)
                    }
                )
            }

            Footer()
        }
    )

    val keyboardController = LocalSoftwareKeyboardController.current
    ModalSheet(
        visible = customRssBox.value,
        onVisibleChange = {
            if (!it) {
                keyboardController?.hide()
                customRssBox.value = false
            }
        },
        cancelable = true
    ) {
        AddRSSScreen(
            sourceAdded = {
                keyboardController?.hide()
                sourceAdded(it)
            },
            closeSheet = {
                keyboardController?.hide()
                customRssBox.value = false
            }
        )
    }
}

@Composable
private fun Source(
    modifier: Modifier = Modifier,
    model: RSSSource,
    sourceAdded: (rssLink: String) -> Unit,
    sourceRemoved: (rssLink: String) -> Unit,
    contactLink: (link: RSSSource) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = {
            if (model.isChecked) {
                sourceRemoved(model.url)
            } else {
                sourceAdded(model.url)
            }
        })
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        SourceBadge(
            title = model.supportedArticleSource?.sourceShort ?:
                model.url.substring(0..2),
            textColour = model.supportedArticleSource?.textColour?.toColorInt()?.let { Color(it) } ?: Color.White,
            colour = model.supportedArticleSource?.colour?.toColorInt()?.let { Color(it) } ?: AppTheme.colors.primary
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = AppTheme.dimens.medium)
        ) {
            TextTitle(
                text = model.supportedArticleSource?.source ?: model.urlModel?.let { "${it.protocol}://${it.host}"} ?: model.url
            )
            TextBody2(
                text = model.url,
                modifier = Modifier.padding(top = 4.dp)
            )
            model.supportedArticleSource?.let {
                ButtonSecondary(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(id = R.string.settings_rss_contact_link),
                    onClick = {
                        contactLink(model)
                    }
                )
            }
        }
        InputSwitch(isChecked = model.isChecked)
    }
}


@Composable
internal fun SourceBadge(
    title: String,
    textColour: Color,
    colour: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .size(badgeSize)
        .clip(CircleShape)
        .background(colour)
    ) {
        Text(
            text = title,
            maxLines = 1,
            style = AppTheme.typography.title.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Center),
            color = textColour
        )
    }
}


@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ConfigureRSSScreen(
            actionUpClicked = {},
            showDescriptionEnabled = true,
            showDescriptionClicked = { },
            sourceAdded = { },
            sourceRemoved = { },
            contactLinkClicked = { },
            showCustomAdd = true,
            sources = listOf(
                fakeRssSource
            )
        )
    }
}

private val fakeRssSource: RSSSource = RSSSource(
    url = "https://www.url.com/f1/rss.xml",
    supportedArticleSource = SupportedArticleSource(
        rssLink = "https://www.url.com/f1/rss.xml",
        sourceShort = "URL",
        source = "https://www.url.com/",
        colour = "#928284",
        textColour = "#efefef",
        title = "Motorsport API",
        contactLink = "https://www.url.com/contact",
    ),
    isChecked = true
)