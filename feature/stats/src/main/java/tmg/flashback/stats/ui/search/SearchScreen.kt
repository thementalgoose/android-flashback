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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.shared.DriverImage
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.utils.isInPreview

@Composable
fun SearchScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SearchViewModel>()

    val category = viewModel.outputs.selectedCategory.observeAsState()
    val list = viewModel.outputs.results.observeAsState(emptyList())
    SearchScreen(
        actionUpClicked = actionUpClicked,
        searchInputUpdated = viewModel.inputs::inputSearch,
        searchCategory = category.value,
        searchCategoryUpdated = viewModel.inputs::inputCategory,
        itemClicked = viewModel.inputs::clickItem,
        list = list.value
    )
}

@Composable
fun SearchScreen(
    actionUpClicked: () -> Unit,
    searchCategory: SearchCategory?,
    searchCategoryUpdated: (SearchCategory) -> Unit,
    searchInputUpdated: (String) -> Unit,
    itemClicked: (SearchItem) -> Unit,
    list: List<SearchItem>
) {
    Column(Modifier
        .navigationBarsPadding()
        .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            content = {
                item(key = "header") {
                    Header(
                        text = stringResource(id = R.string.search_title),
                        icon = painterResource(id = R.drawable.ic_back),
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

        Column(Modifier
            .fillMaxWidth()
            .imePadding()
        ) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Spacer(Modifier.width(16.dp))
                SearchCategory.values().forEach {
                    ButtonTertiary(
                        text = stringResource(it.label),
                        onClick = {
                            searchCategoryUpdated(it)
                        },
                        enabled = it == searchCategory
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Spacer(Modifier.width(16.dp))
            }
            val text = remember { mutableStateOf(TextFieldValue("")) }
            InputPrimary(
                modifier = Modifier.padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingMedium
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
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Box(
            Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        ) {
            val track = TrackLayout.getTrack(model.circuitId)
            Icon(
                tint = AppTheme.colors.contentPrimary,
                modifier = Modifier.size(64.dp),
                painter = painterResource(id = track?.icon ?: R.drawable.circuit_unknown),
                contentDescription = null
            )
        }
        Spacer(Modifier.width(AppTheme.dimensions.paddingNSmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = model.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall)
            )
            TextBody2(
                text = model.nationality,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall)
            )
            val resourceId = when (isInPreview()) {
                true -> R.drawable.gb
                false -> LocalContext.current.getFlagResourceAlpha3(model.nationalityISO)
            }
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = resourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
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
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        ) {
            val resourceId = when (isInPreview()) {
                true -> R.drawable.gb
                false -> LocalContext.current.getFlagResourceAlpha3(model.countryISO)
            }
            Image(
                modifier = Modifier
                    .size(42.dp),
                painter = painterResource(id = resourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(Modifier.width(AppTheme.dimensions.paddingNSmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = "${model.season} ${model.raceName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall)
            )
            TextBody2(
                text = model.circuitName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall)
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
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(AppTheme.colors.backgroundTertiary)
        ) {
            DriverImage(
                size = 42.dp,
                photoUrl = model.imageUrl
            )
        }
        Spacer(Modifier.width(AppTheme.dimensions.paddingNSmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = model.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall)
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
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(Color(model.colour))
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingNSmall))
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                bold = true,
                text = model.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall)
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
        vertical = AppTheme.dimensions.paddingNSmall,
        horizontal = AppTheme.dimensions.paddingMedium
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
        vertical = AppTheme.dimensions.paddingNSmall,
        horizontal = AppTheme.dimensions.paddingMedium
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