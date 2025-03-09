package tmg.flashback.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.search.presentation.SearchScreenStateCategory.CIRCUITS
import tmg.flashback.search.presentation.SearchScreenStateCategory.CONSTRUCTORS
import tmg.flashback.search.presentation.SearchScreenStateCategory.DRIVERS
import tmg.flashback.search.presentation.circuits.Circuits
import tmg.flashback.search.presentation.circuits.SearchCircuitViewModel
import tmg.flashback.search.presentation.constructors.Constructors
import tmg.flashback.search.presentation.constructors.SearchConstructorViewModel
import tmg.flashback.search.presentation.drivers.Drivers
import tmg.flashback.search.presentation.drivers.SearchDriverViewModel
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewThemeExpanded
import tmg.flashback.style.buttons.ButtonSecondarySegments
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.ui.components.header.Header

@Composable
fun SearchScreenList(
    actionUpClicked: () -> Unit,
    isRoot: (Boolean) -> Unit,
    viewModel: SearchViewModel,
    advertProvider: AdvertProvider
) {
    val uiState = viewModel.outputs.uiState.collectAsState()

    SearchScreenList(
        actionUpClicked = actionUpClicked,
        isRoot = isRoot,
        tabClicked = viewModel.inputs::selectType,
        searchTermUpdated = viewModel.inputs::searchTermUpdated,
        searchTermClear = viewModel.inputs::searchTermClear,
        uiState = uiState.value,
        advertProvider = advertProvider
    )
}

@Composable
fun SearchScreenList(
    actionUpClicked: () -> Unit,
    isRoot: (Boolean) -> Unit,
    tabClicked: (SearchScreenStateCategory) -> Unit,
    searchTermUpdated: (String) -> Unit,
    searchTermClear: () -> Unit,
    uiState: SearchScreenState,
    advertProvider: AdvertProvider
) {

    val driversVM: SearchDriverViewModel = hiltViewModel()
    val driversUIState = driversVM.uiState.collectAsState()
    val constructorsVM: SearchConstructorViewModel = hiltViewModel()
    val constructorsUIState = constructorsVM.uiState.collectAsState()
    val circuitsVM: SearchCircuitViewModel = hiltViewModel()
    val circuitsUIState = circuitsVM.uiState.collectAsState()

    LaunchedEffect(uiState.searchTerm) {
        circuitsVM.searchTerm(uiState.searchTerm)
        driversVM.searchTerm(uiState.searchTerm)
        constructorsVM.searchTerm(uiState.searchTerm)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colors.backgroundPrimary)
    ) {
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Adaptive(minSize = 220.dp),
            content = {
                item("header", span = { GridItemSpan(maxLineSpan) }) {
                    SearchHeader(
                        actionUpClicked = actionUpClicked,
                        searchTerm = uiState.searchTerm,
                        searchTermUpdated = searchTermUpdated,
                        searchTermClear = searchTermClear,
                        tab = uiState.category,
                        tabClicked = tabClicked
                    )
                }
                if (uiState.showAdvert && uiState.searchTerm.isBlank()) {
                    item("advert") {
                        advertProvider.NativeBanner(
                            horizontalPadding = 16.dp,
                            adIconSpacing = 16.dp,
                            adIconSize = 42.dp,
                            adIndex = 1
                        )
                    }
                }
                when (uiState.category) {
                    DRIVERS -> {
                        Drivers(
                            driverClicked = driversVM::clickDriver,
                            drivers = driversUIState.value.filtered,
                        )
                    }
                    CONSTRUCTORS -> {
                        Constructors(
                            constructorClicked = constructorsVM::clickConstructor,
                            constructors = constructorsUIState.value.filtered,
                        )
                    }
                    CIRCUITS -> {
                        Circuits(
                            circuitClicked = circuitsVM::clickCircuit,
                            circuits = circuitsUIState.value.filtered,
                        )
                    }
                    null -> { }
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchHeader(
    actionUpClicked: () -> Unit,
    searchTerm: String,
    searchTermUpdated: (String) -> Unit,
    searchTermClear: () -> Unit,
    tab: SearchScreenStateCategory?,
    tabClicked: (SearchScreenStateCategory) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = AppTheme.dimens.small)
    ) {
        Header(
            modifier = Modifier.width(350.dp),
            actionUpClicked = actionUpClicked,
            text = stringResource(string.search_title),
            action = null
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.medium)
                .clip(RoundedCornerShape(50.dp))
                .background(AppTheme.colors.backgroundTertiary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text = remember { mutableStateOf(TextFieldValue(searchTerm)) }
            InputPrimary(
                modifier = Modifier
                    .weight(1f)
                    .imeNestedScroll(),
                text = text,
                onValueChange = {
                    text.value = it
                    searchTermUpdated(it.text)
                },
                imeAction = ImeAction.Done,
                placeholder = stringResource(id = string.search_title),
                clear = searchTermClear
            )
            ButtonSecondarySegments(
                modifier = Modifier
                    .padding(horizontal = AppTheme.dimens.medium)
                    .weight(1f),
                items = listOf(DRIVERS, CONSTRUCTORS, CIRCUITS).map { it.label },
                showTick = true,
                selected = tab?.label,
                onClick = { ordinal ->
                    when (ordinal) {
                        string.search_category_driver -> tabClicked(DRIVERS)
                        string.search_category_constructors -> tabClicked(CONSTRUCTORS)
                        string.search_category_circuits -> tabClicked(CIRCUITS)
                    }
                }
            )
        }
    }
}

private val SearchScreenStateCategory.label: Int
    get() = when (this) {
        DRIVERS -> string.search_category_driver
        CONSTRUCTORS -> string.search_category_constructors
        CIRCUITS -> string.search_category_circuits
    }

@PreviewThemeExpanded
@Composable
private fun Preview() {
    AppThemePreview {
        SearchHeader(
            actionUpClicked = {},
            searchTerm = "Albon",
            searchTermUpdated = { "" },
            searchTermClear = { },
            tab = DRIVERS,
            tabClicked = { }
        )
    }
}