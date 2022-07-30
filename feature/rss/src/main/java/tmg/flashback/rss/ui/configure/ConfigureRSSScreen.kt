package tmg.flashback.rss.ui.configure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.ui.feed.SourceBadge
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.utils.hexToColor

@Composable
fun SettingsRSSConfigureScreenVM(
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Configure RSS")
    
    val viewModel = viewModel<ConfigureRSSViewModel>()
    val list = viewModel.outputs.list.observeAsState(emptyList())

    SettingsRSSConfigureScreen(
        list = list.value,
        actionUpClicked = actionUpClicked,
        addQuickItemClicked = viewModel.inputs::addQuickItem,
        websiteClicked = viewModel.inputs::visitWebsite,
        removeItem = viewModel.inputs::removeItem,
        addCustomItem = viewModel.inputs::addCustomItem
    )
}

@Composable
private fun SettingsRSSConfigureScreen(
    list: List<RSSConfigureItem>,
    addQuickItemClicked: (SupportedArticleSource) -> Unit,
    websiteClicked: (SupportedArticleSource) -> Unit,
    removeItem: (String) -> Unit,
    addCustomItem: (String) -> Unit,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.settings_rss_configure),
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.id }) {
                when (it) {
                    is RSSConfigureItem.Header -> { Header(it) }
                    is RSSConfigureItem.Item -> Item(
                        model = it,
                        websiteLinkClicked = { item ->
                            item.supportedArticleSource?.let { websiteClicked(it) }
                        },
                        removeClicked = { item -> removeItem(item.url) }
                    )
                    RSSConfigureItem.NoItems -> { NoItems() }
                    is RSSConfigureItem.QuickAdd -> { QuickAdd(
                        model = it,
                        websiteLinkClicked = { item -> websiteClicked(item.supportedArticleSource) },
                        addClicked = { item -> addQuickItemClicked(item.supportedArticleSource) }
                    )}
                    RSSConfigureItem.Add -> {
                        Add(addUrl = addCustomItem)
                    }
                }
            }
        }
    )
}

@Composable
private fun Header(
    model: RSSConfigureItem.Header,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(
            horizontal = AppTheme.dimensions.paddingMedium,
            vertical = AppTheme.dimensions.paddingSmall
        )
    ) {
        TextBody1(
            modifier = Modifier.fillMaxWidth(),
            bold = true,
            text = stringResource(model.text)
        )

        model.subtitle?.let {
            TextBody2(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(it)
            )
        }
    }
}

@Composable
private fun NoItems(
    modifier: Modifier = Modifier
) {
    TextCaption(
        text = stringResource(id = R.string.rss_configure_header_items_no_items),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimensions.paddingXSmall,
                horizontal = AppTheme.dimensions.paddingMedium
            )
    )
}

@Composable
private fun Item(
    model: RSSConfigureItem.Item,
    modifier: Modifier = Modifier,
    websiteLinkClicked: (RSSConfigureItem.Item) -> Unit,
    removeClicked: (RSSConfigureItem.Item) -> Unit,
) {
    Row(modifier = modifier
        .padding(
            start = AppTheme.dimensions.paddingMedium,
            end = AppTheme.dimensions.paddingSmall,
            top = AppTheme.dimensions.paddingSmall,
            bottom = AppTheme.dimensions.paddingSmall
        )
    ) {
        SourceBadge(
            title = model.supportedArticleSource?.sourceShort ?: "...",
            textColour = model.supportedArticleSource?.textColour?.hexToColor() ?: AppTheme.colors.contentSecondary,
            colour = model.supportedArticleSource?.colour?.hexToColor() ?: AppTheme.colors.backgroundTertiary
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                text = model.urlModel?.let { "${it.protocol}://${it.host}" } ?: model.url,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1
            )
            TextBody2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                text = model.url
            )
        }
        Spacer(Modifier.width(4.dp))
        if (model.supportedArticleSource != null) {
            ButtonTertiary(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.rss_configure_visit_website),
                onClick = { websiteLinkClicked(model) }
            )
            Spacer(Modifier.width(4.dp))
        }
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { removeClicked(model) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_rss_configure_remove),
                contentDescription = stringResource(id = R.string.ab_rss_configure_supported_remove),
                tint = AppTheme.colors.rssRemove
            )
        }
    }
}

@Composable
private fun QuickAdd(
    model: RSSConfigureItem.QuickAdd,
    modifier: Modifier = Modifier,
    websiteLinkClicked: (RSSConfigureItem.QuickAdd) -> Unit,
    addClicked: (RSSConfigureItem.QuickAdd) -> Unit,
) {
    Row(modifier = modifier
        .padding(
            start = AppTheme.dimensions.paddingMedium,
            end = AppTheme.dimensions.paddingSmall,
            top = AppTheme.dimensions.paddingSmall,
            bottom = AppTheme.dimensions.paddingSmall
        )
    ) {
        SourceBadge(
            title = model.supportedArticleSource.sourceShort,
            textColour = model.supportedArticleSource.textColour.hexToColor() ?: AppTheme.colors.contentSecondary,
            colour = model.supportedArticleSource.colour.hexToColor() ?: AppTheme.colors.backgroundTertiary
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                text = model.supportedArticleSource.source,
                modifier = Modifier.fillMaxWidth()
            )
            TextBody2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                text = model.supportedArticleSource.rssLink
            )
        }
        Spacer(Modifier.width(4.dp))
        ButtonTertiary(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.rss_configure_visit_website),
            onClick = { websiteLinkClicked(model) }
        )
        Spacer(Modifier.width(4.dp))
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { addClicked(model) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_rss_configure_add),
                contentDescription = stringResource(id = R.string.ab_rss_configure_supported_add),
                tint = AppTheme.colors.rssAdd
            )
        }
    }
}

@Composable
private fun Add(
    addUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .padding(
            start = AppTheme.dimensions.paddingMedium,
            end = AppTheme.dimensions.paddingSmall,
            top = AppTheme.dimensions.paddingSmall,
            bottom = AppTheme.dimensions.paddingSmall
        )
    ) {
        val text = remember { mutableStateOf(TextFieldValue("")) }
        InputPrimary(
            text = text,
            modifier = Modifier.weight(1f),
            placeholder = stringResource(id = R.string.rss_configure_hint)
        )
        Spacer(Modifier.width(4.dp))
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { addUrl(text.value.text) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_rss_configure_add),
                contentDescription = stringResource(id = R.string.ab_rss_configure_supported_add),
                tint = AppTheme.colors.rssAdd
            )
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .imePadding())
}