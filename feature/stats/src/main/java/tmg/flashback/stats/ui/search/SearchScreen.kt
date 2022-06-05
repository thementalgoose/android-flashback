package tmg.flashback.stats.ui.search

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Badge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.components.header.Header

@Composable
fun SearchScreenVM(
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SearchViewModel>()


    val list = viewModel.outputs.results.observeAsState(emptyList())
    SearchScreen(
        actionUpClicked = actionUpClicked,
        list = list.value
    )
}

@Composable
fun SearchScreen(
    actionUpClicked: () -> Unit,
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
                    ButtonTertiary(text = stringResource(id = R.string.search_category_driver), onClick = {  }, enabled = true)
                    Spacer(Modifier.width(8.dp))
                    ButtonTertiary(text = stringResource(id = R.string.search_category_constructors), onClick = {  })
                    Spacer(Modifier.width(8.dp))
                    ButtonTertiary(text = stringResource(id = R.string.search_category_races), onClick = {  })
                    Spacer(Modifier.width(8.dp))
                    ButtonTertiary(text = stringResource(id = R.string.search_category_circuits), onClick = {  })
                    Spacer(Modifier.width(16.dp))
                }
            }
            val text = remember { mutableStateOf(TextFieldValue()) }
            InputPrimary(
                modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium),
                text = text,
                placeholder = "INFO"
            )
        }
        items(list, key = { it.id }) {
            when (it) {
                is SearchItem.Circuit -> ResultCircuit(it)
                is SearchItem.Constructor -> ResultConstructor(it)
                is SearchItem.Driver -> ResultDriver(it)
                is SearchItem.Race -> ResultRace(it)
                SearchItem.Advert -> TODO()
                SearchItem.ErrorItem -> TODO()
                SearchItem.Placeholder -> TODO()
            }
        }
    })
}

@Composable
private fun ResultCircuit(
    model: SearchItem.Circuit
) {
    TextBody1("Circuit")
}

@Composable
private fun ResultRace(
    model: SearchItem.Race
) {
    TextBody1("Race")
}

@Composable
private fun ResultDriver(
    model: SearchItem.Driver
) {
    TextBody1("Driver")
}

@Composable
private fun ResultConstructor(
    model: SearchItem.Constructor
) {
    TextBody1("Constructor")
}