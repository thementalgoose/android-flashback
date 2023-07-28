package tmg.flashback.rss.ui.feed

import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.ads.ads.components.fakeAdvertProvider
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.messages.Message
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.utilities.extensions.fromHtml

private val badgeSize: Dp = 40.dp

@Composable
fun RSSScreenVM(
    showMenu: Boolean = true,
    advertProvider: AdvertProvider,
    actionUpClicked: () -> Unit,
    viewModel: RSSViewModel = hiltViewModel()
) {
    ScreenView(screenName = "RSS")

    val uiState = viewModel.outputs.uiState.collectAsState()
    val isLoading = viewModel.outputs.isLoading.collectAsState(false)

    SwipeRefresh(
        isLoading = isLoading.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        RSSScreen(
            showMenu = showMenu,
            advertProvider = advertProvider,
            uiState = uiState.value,
            itemClicked = viewModel.inputs::clickArticle,
            configureSources = viewModel.inputs::configure,
            actionUpClicked = actionUpClicked
        )
    }
}

@Composable
fun RSSScreen(
    showMenu: Boolean,
    advertProvider: AdvertProvider,
    uiState: RSSViewModel.UiState,
    itemClicked: (Article) -> Unit,
    configureSources: () -> Unit,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                Header(
                    text = stringResource(id = R.string.title_rss),
                    icon = if (showMenu) painterResource(id = R.drawable.ic_menu) else null,
                    iconContentDescription = stringResource(id = R.string.ab_menu),
                    overrideIcons = {
                        IconButton(onClick = configureSources) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_rss_settings),
                                contentDescription = stringResource(id = R.string.ab_rss_settings),
                                tint = AppTheme.colors.contentPrimary
                            )
                        }
                    },
                    actionUpClicked = actionUpClicked
                )
            }
            when (uiState) {
                is RSSViewModel.UiState.SourcesDisabled -> {
                    item(key = "sourcedisabled") {
                        SourcesDisabled(sourceClicked = configureSources)
                    }
                }
                is RSSViewModel.UiState.NoNetwork -> {
                    item(key = "nonetwork") {
                        NetworkError(error = NetworkError.NETWORK_ERROR)
                    }
                }
                is RSSViewModel.UiState.Data -> {
                    if (uiState.lastUpdated != null) {
                        item(key = "updated") {
                            Message(stringResource(id = R.string.home_last_updated, uiState.lastUpdated))
                        }
                    }
                    if (uiState.showAdvert) {
                        item(key = "advert") {
                            advertProvider.NativeBanner(
                                horizontalPadding = true,
                                badgeOffset = true
                            )
                        }
                    }
                    items(uiState.rssItems, key = { it.link }) {
                        Item(it, itemClicked)
                    }
                }
            }
        }
    )
}

@Composable
private fun Item(
    model: Article,
    clickItem: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = {
            clickItem(model)
        })
        .padding(
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        SourceBadge(
            title = model.source.shortSource
                ?: model.source.source.substring(0..2),
            textColour = Color(model.source.textColor.toColorInt()),
            colour = Color(model.source.colour.toColorInt())
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                text = model.title,
                bold = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            model.description?.let { desc ->
                AndroidView(factory = {
                    TextView(it).apply {
                        this.text = desc.split("<br").firstOrNull()
                            ?.trim()
                            ?.fromHtml() ?: ""
                        this.layoutParams = ViewGroup.LayoutParams(
                             ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                        )
                        this.setTextColor(when (AppTheme.isLight) {
                            true -> android.graphics.Color.BLACK
                            false -> android.graphics.Color.WHITE
                        })
                    }
                })
            }
            TextCaption(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                text = "${model.source.source} - ${model.date?.format(DateTimeFormatter.ofPattern("HH:mm 'at' dd MMM"))}",
            )
        }
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

@Composable
private fun SourcesDisabled(
    sourceClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = sourceClicked)
        .padding(
            vertical = AppTheme.dimens.medium,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_rss_icon_no_sources),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.width(16.dp))
        TextBody1(
            text = stringResource(id = R.string.rss_no_articles)
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        RSSScreen(
            showMenu = true,
            advertProvider = fakeAdvertProvider,
            uiState = RSSViewModel.UiState.Data(
                rssItems = listOf(fakeArticle)
            ),
            itemClicked = {},
            configureSources = {},
            actionUpClicked = {}
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewArticle() {
    AppThemePreview {
        RSSScreen(
            showMenu = true,
            advertProvider = fakeAdvertProvider,
            uiState = RSSViewModel.UiState.Data(
                rssItems = listOf(fakeArticle),
                articleSelected = fakeArticle
            ),
            itemClicked = {},
            configureSources = {},
            actionUpClicked = {}
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewArticleTablet() {
    AppThemePreview {
        RSSScreen(
            showMenu = false,
            advertProvider = fakeAdvertProvider,
            uiState = RSSViewModel.UiState.Data(
                rssItems = listOf(fakeArticle),
                articleSelected = fakeArticle
            ),
            itemClicked = {},
            configureSources = {},
            actionUpClicked = {}
        )
    }
}

private val fakeArticle: Article get() = Article(
    id = "id",
    title = "Title",
    description = "This is a long description of the content of the website for the reader to confume",
    link = "https://www.link.com",
    date = LocalDateTime.now(),
    source = fakeArticleSource,
)

private val fakeArticleSource: ArticleSource get() = ArticleSource(
    title = "title",
    colour = "#123123",
    textColor = "#ffffff",
    rssLink = "https://www.rsslink.com/link",
    source = "https://www.rsslink.com",
    shortSource = "SO",
    contactLink = "https://www.rsslink.com/contact",
)