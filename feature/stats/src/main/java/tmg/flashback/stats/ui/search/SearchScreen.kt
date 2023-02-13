package tmg.flashback.stats.ui.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.shared.DriverImage
import tmg.flashback.stats.ui.shared.Flag
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh

@Composable
fun SearchScreenVM(
    showMenu: Boolean = true,
    actionUpClicked: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Search")

    val category = viewModel.outputs.selectedCategory.observeAsState()
    val list = viewModel.outputs.results.observeAsState(emptyList())

    val isLoading = viewModel.outputs.isLoading.observeAsState(false)
    SwipeRefresh(
        isLoading = isLoading.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        SearchScreen(
            showMenu = showMenu,
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
    actionUpClicked: () -> Unit,
    searchCategory: SearchCategory?,
    searchCategoryUpdated: (SearchCategory) -> Unit,
    searchInputUpdated: (String) -> Unit,
    itemClicked: (SearchItem) -> Unit,
    list: List<SearchItem>
) {
    Column(
        Modifier
            .background(AppTheme.colors.backgroundPrimary)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
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
                        icon = if (showMenu) painterResource(id = R.drawable.ic_menu) else null,
                        iconContentDescription = stringResource(id = R.string.ab_back),
                        actionUpClicked = actionUpClicked
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
//                            TextBody1(text = "Advert")
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

        Column(
            Modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            Row(modifier = Modifier
                .padding(vertical = AppTheme.dimens.small)
                .horizontalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.width(AppTheme.dimens.medium))
                SearchCategory.values().forEach {
                    ButtonTertiary(
                        text = stringResource(it.label),
                        onClick = {
                            searchCategoryUpdated(it)
                        },
                        enabled = it == searchCategory
                    )
                    Spacer(Modifier.width(AppTheme.dimens.medium))
                }
                Spacer(Modifier.width(AppTheme.dimens.medium))
            }
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
                painter = painterResource(id = track?.icon ?: R.drawable.circuit_unknown),
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
            DriverImage(
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