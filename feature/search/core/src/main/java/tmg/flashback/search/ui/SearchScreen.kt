package tmg.flashback.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.search.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh

@Composable
fun SearchScreenVM(
    showMenu: Boolean = true,
    actionUpClicked: () -> Unit,
    advertProvider: AdvertProvider,
    viewModel: SearchViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Search")

    val category = viewModel.outputs.selectedCategory.collectAsState()
    val list = viewModel.outputs.results.collectAsState(emptyList())

    val isLoading = viewModel.outputs.isLoading.collectAsState(false)
    SwipeRefresh(
        isLoading = isLoading.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        SearchScreen(
            showMenu = showMenu,
            advertProvider = advertProvider,
            actionUpClicked = actionUpClicked,
            searchInputUpdated = viewModel.inputs::inputSearch,
            searchCategory = category.value,
            searchCategoryUpdated = viewModel.inputs::inputCategory,
            itemClicked = viewModel.inputs::clickItem,
            list = list.value
        )
    }
}

@Composable
fun SearchScreen(
    showMenu: Boolean,
    advertProvider: AdvertProvider?,
    actionUpClicked: () -> Unit,
    searchCategory: SearchCategory?,
    searchCategoryUpdated: (SearchCategory) -> Unit,
    searchInputUpdated: (String) -> Unit,
    itemClicked: (SearchItem) -> Unit,
    list: List<SearchItem>
) {
    
    val showCategoryPicker = remember { mutableStateOf(searchCategory == null) }
    
    Column(
        Modifier
            .background(AppTheme.colors.backgroundPrimary)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier.weight(1f),
        ) {
            LazyColumn(
                content = {
                    item(key = "header") {
                        Header(
                            text = when (searchCategory) {
                                SearchCategory.DRIVER -> stringResource(id = R.string.search_category_driver)
                                SearchCategory.CONSTRUCTOR -> stringResource(id = R.string.search_category_constructors)
                                SearchCategory.CIRCUIT -> stringResource(id = R.string.search_category_circuits)
                                SearchCategory.RACE -> stringResource(id = R.string.search_category_races)
                                null -> stringResource(id = R.string.search_title)
                            },
                            action = if (showMenu) HeaderAction.MENU else null,
                            actionUpClicked = actionUpClicked,
                            overrideIcons = @Composable {
                                IconButton(onClick = {
                                    showCategoryPicker.value = true
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_search_placeholder),
                                        contentDescription = stringResource(id = R.string.search_category_option_title),
                                        tint = AppTheme.colors.contentPrimary
                                    )
                                }
                            }
                        )
                    }
                    items(list, key = { it.id }) {
                        when (it) {
                            is SearchItem.Circuit -> ResultCircuit(
                                model = it,
                                clicked = itemClicked
                            )
                            is SearchItem.Constructor -> ResultConstructor(
                                model = it,
                                clicked = itemClicked
                            )
                            is SearchItem.Driver -> ResultDriver(
                                model = it,
                                clicked = itemClicked
                            )
                            is SearchItem.Race -> ResultRace(
                                model = it,
                                clicked = itemClicked
                            )
                            SearchItem.Advert -> {
                                advertProvider?.NativeBanner(
                                    horizontalPadding = true,
                                    badgeOffset = true
                                )
                            }
                            SearchItem.ErrorItem -> {
                                NotFound()
                            }
                            SearchItem.Placeholder -> {
                                Placeholder()
                            }
                        }
                    }
                }
            )
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(AppTheme.dimens.large)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, AppTheme.colors.backgroundPrimary)))
            )
        }
        
        Column(
            Modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            Spacer(Modifier.height(AppTheme.dimens.small))
            val text = remember { mutableStateOf(TextFieldValue("")) }
            InputPrimary(
                modifier = Modifier.padding(
                    start = AppTheme.dimens.medium,
                    end = AppTheme.dimens.medium,
                    bottom = AppTheme.dimens.medium
                ),
                text = text,
                onValueChange = {
                    text.value = it
                    searchInputUpdated(it.text)
                },
                placeholder = stringResource(id = R.string.search_title)
            )
        }
    }
    
    SearchCategoryBottomSheet(
        visible = showCategoryPicker.value,
        dismiss = { showCategoryPicker.value = false },
        selected = searchCategory,
        categoryClicked = searchCategoryUpdated
    )
}

@Composable
private fun ResultCircuit(
    model: SearchItem.Circuit,
    clicked: (SearchItem.Circuit) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = { clicked(model) })
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Box(
            Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
        ) {
            val track = TrackLayout.getTrack(model.circuitId)
            Icon(
                tint = AppTheme.colors.contentPrimary,
                modifier = Modifier.size(64.dp),
                painter = painterResource(id = track?.getDefaultIcon() ?: R.drawable.circuit_unknown),
                contentDescription = null
            )
        }
        Spacer(Modifier.width(AppTheme.dimens.nsmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = model.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall)
            )
            TextBody2(
                text = model.nationality,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall)
            )
            Flag(
                iso = model.nationalityISO,
                nationality = model.nationality,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun ResultRace(
    model: SearchItem.Race,
    clicked: (SearchItem.Race) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = { clicked(model) })
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
        ) {
            Flag(
                iso = model.countryISO,
                nationality = model.country,
                modifier = Modifier
                    .size(42.dp),
            )
        }
        Spacer(Modifier.width(AppTheme.dimens.nsmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = "${model.season} ${model.raceName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall)
            )
            TextBody2(
                text = model.circuitName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall)
            )
            TextBody2(
                text = "${model.country}, ${stringResource(id = R.string.weekend_race_round, model.round)}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ResultDriver(
    model: SearchItem.Driver,
    clicked: (SearchItem.Driver) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = { clicked(model) })
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
                .background(AppTheme.colors.backgroundTertiary)
        ) {
            tmg.flashback.ui.components.drivers.DriverImage(
                size = 42.dp,
                photoUrl = model.imageUrl
            )
        }
        Spacer(Modifier.width(AppTheme.dimens.nsmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = model.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall)
            )
            TextBody2(
                text = model.nationality,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ResultConstructor(
    model: SearchItem.Constructor,
    clicked: (SearchItem.Constructor) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = { clicked(model) })
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
                .background(Color(model.colour))
        ) {
            if (model.photoUrl != null) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    model = model.photoUrl,
                    contentDescription = null
                )
            }
        }
        Spacer(Modifier.width(AppTheme.dimens.nsmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = model.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall)
            )
            TextBody2(
                text = model.nationality,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun NotFound(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(
        vertical = AppTheme.dimens.nsmall,
        horizontal = AppTheme.dimens.medium
    )) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error_not_available),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.width(16.dp))
        TextBody1(
            text = stringResource(id = R.string.error_search_not_found)
        )
    }
}

@Composable
private fun Placeholder(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(
        vertical = AppTheme.dimens.nsmall,
        horizontal = AppTheme.dimens.medium
    )) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search_placeholder),
            contentDescription = null,
            tint = AppTheme.colors.contentSecondary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.width(16.dp))
        TextBody1(
            text = stringResource(id = R.string.search_placeholder)
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SearchScreen(
            showMenu = true,
            advertProvider = null,
            actionUpClicked = { },
            searchCategory = SearchCategory.CONSTRUCTOR,
            searchCategoryUpdated = { },
            searchInputUpdated = { },
            itemClicked = { } ,
            list = listOf(SearchItem.Placeholder)
        )
    }
}