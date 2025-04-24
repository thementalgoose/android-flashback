package tmg.flashback.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.search.R
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
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.utilities.extensions.toEnum

@Composable
fun SearchScreenTab(
    paddingValues: PaddingValues,
    actionUpClicked: () -> Unit,
    isRoot: (Boolean) -> Unit,
    viewModel: SearchViewModel,
    advertProvider: AdvertProvider
) {
    val uiState = viewModel.outputs.uiState.collectAsState()

    SearchScreenTab(
        actionUpClicked = actionUpClicked,
        paddingValues = paddingValues,
        isRoot = isRoot,
        tabClicked = viewModel.inputs::selectType,
        searchTermUpdated = viewModel.inputs::searchTermUpdated,
        searchTermClear = viewModel.inputs::searchTermClear,
        uiState = uiState.value,
        advertProvider = advertProvider
    )
}

@Composable
private fun SearchScreenTab(
    actionUpClicked: () -> Unit,
    isRoot: (Boolean) -> Unit,
    paddingValues: PaddingValues,
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

    val systemNavigationBarHeight = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary)
    ) {
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.weight(1f),
            content = {
                item("header") {
                    SearchHeader(
                        actionUpClicked = actionUpClicked,
                        searchTerm = uiState.searchTerm,
                        searchTermUpdated = searchTermUpdated,
                        searchTermClear = searchTermClear
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
        NavigationBar(
            bottomPadding = systemNavigationBarHeight,
            list = listOf(
                NavigationItem(
                    id = DRIVERS.name,
                    label = string.search_category_driver,
                    icon = R.drawable.ic_search_driver,
                    isSelected = uiState.category == DRIVERS
                ),
                NavigationItem(
                    id = CONSTRUCTORS.name,
                    label = string.search_category_constructors,
                    icon = R.drawable.ic_search_constructor,
                    isSelected = uiState.category == CONSTRUCTORS
                ),
                NavigationItem(
                    id = CIRCUITS.name,
                    label = string.search_category_circuits,
                    icon = R.drawable.ic_search_circuits,
                    isSelected = uiState.category == CIRCUITS
                )
            ),
            itemClicked = {
                when (it.id.toEnum<SearchScreenStateCategory>()) {
                    DRIVERS -> tabClicked(DRIVERS)
                    CONSTRUCTORS -> tabClicked(CONSTRUCTORS)
                    CIRCUITS -> tabClicked(CIRCUITS)
                    null -> {}
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
    searchTermClear: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = AppTheme.dimens.small)
    ) {
        Header(
            actionUpClicked = actionUpClicked,
            text = stringResource(string.search_title),
            action = HeaderAction.MENU
        )
        Spacer(Modifier.height(AppTheme.dimens.small))
        val text = remember { mutableStateOf(TextFieldValue(searchTerm)) }
        InputPrimary(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.medium)
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
    }
}