package tmg.flashback.stats.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Badge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.shared.DriverImage
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.header.Header

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
        list = list.value
    )
}

@Composable
fun SearchScreen(
    actionUpClicked: () -> Unit,
    searchCategory: SearchCategory?,
    searchCategoryUpdated: (SearchCategory) -> Unit,
    searchInputUpdated: (String) -> Unit,
    list: List<SearchItem>
) {
    LazyColumn(content = {
        item(key = "header") {
            Header(
                text = stringResource(id = R.string.search_title),
                icon = painterResource(id = R.drawable.ic_back),
                iconContentDescription = stringResource(id = R.string.ab_back),
                actionUpClicked = actionUpClicked
            )
        }
        item(key = "search") {
            Column(Modifier.fillMaxWidth()) {
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
            }
            val text = remember { mutableStateOf(TextFieldValue("")) }
            InputPrimary(
                modifier = Modifier.padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingSmall
                ),
                text = text,
                onValueChange = {
                    text.value = it
                    searchInputUpdated(it.text)
                },
                placeholder = "INFO"
            )

        }
        items(list, key = { it.id }) {
            when (it) {
                is SearchItem.Circuit -> ResultCircuit(it)
                is SearchItem.Constructor -> ResultConstructor(it)
                is SearchItem.Driver -> ResultDriver(it)
                is SearchItem.Race -> ResultRace(it)
                SearchItem.Advert -> {}
                SearchItem.ErrorItem -> {}
                SearchItem.Placeholder -> {}
            }
        }
    })
}

@Composable
private fun ResultCircuit(
    model: SearchItem.Circuit,
    modifier: Modifier = Modifier
) {
    Row { 
        
    }
    TextBody1("Circuit ${model.name}")
}

@Composable
private fun ResultRace(
    model: SearchItem.Race,
    modifier: Modifier = Modifier
) {
    TextBody1("Race ${model.raceName}")
}

@Composable
private fun ResultDriver(
    model: SearchItem.Driver,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(
        vertical = AppTheme.dimensions.paddingSmall,
        horizontal = AppTheme.dimensions.paddingMedium
    )) {
        Box(Modifier
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
                text = model.name,
                modifier = Modifier.fillMaxWidth()
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
    modifier: Modifier = Modifier
) {
    TextBody1("Constructor ${model.name}")
}