package tmg.flashback.presentation.aboutthisapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.aboutthisapp.AboutThisAppTheme
import tmg.aboutthisapp.Colours
import tmg.aboutthisapp.configuration.Dependency
import tmg.aboutthisapp.configuration.Link
import tmg.aboutthisapp.presentation.AboutThisAppScreen
import tmg.flashback.R
import tmg.flashback.presentation.aboutthisapp.AboutThisAppViewModel.Companion.GITHUB
import tmg.flashback.style.AppTheme
import tmg.flashback.style.darkColours
import tmg.flashback.style.lightColours
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
fun AboutThisApp(
    windowSizeClass: WindowSizeClass,
    backClicked: () -> Unit,
    viewModel: AboutThisAppViewModel = hiltViewModel(),
) {
    val dependencies = viewModel.dependencies.collectAsState()
    val guids = viewModel.guid.collectAsState()
    val applicationId = viewModel.applicationId.collectAsState()
    val contactEmail = viewModel.contactEmail.collectAsState()

    AboutThisAppTheme(
        lightColors = Colours(
            colorPrimary = lightColours.primary,
            background = lightColours.backgroundPrimary,
            surface = lightColours.backgroundSecondary,
            primary = lightColours.backgroundTertiary,
            onBackground = lightColours.contentPrimary,
            onSurface = lightColours.contentSecondary,
            onPrimary = lightColours.contentTertiary,
        ),
        darkColors = Colours(
            colorPrimary = darkColours.primary,
            background = darkColours.backgroundPrimary,
            surface = darkColours.backgroundSecondary,
            primary = darkColours.backgroundTertiary,
            onBackground = darkColours.contentPrimary,
            onSurface = darkColours.contentSecondary,
            onPrimary = darkColours.contentTertiary,
        ),
    ) {
        AboutThisApp(
            windowSizeClass = windowSizeClass,
            backClicked = backClicked,
            dependencies = dependencies.value,
            guids = "${guids.value.first}\n${guids.value.second}",
            applicationId = applicationId.value,
            contactEmail = contactEmail.value,
            copyToClipboard = viewModel::copyToClipboard,
            openUrl = viewModel::openUrl,
            openPlaystore = viewModel::openPlaystore,
            openEmail = viewModel::openEmail
        )
    }
}

@Composable
fun AboutThisApp(
    windowSizeClass: WindowSizeClass,
    backClicked: () -> Unit,
    dependencies: List<Dependency>,
    guids: String,
    applicationId: String,
    contactEmail: String,
    copyToClipboard: (String) -> Unit,
    openUrl: (String) -> Unit,
    openPlaystore: () -> Unit,
    openEmail: () -> Unit
) {
    AboutThisAppScreen(
        appIcon = R.mipmap.ic_launcher,
        appName = stringResource(id = R.string.app_name),
        dependencies = dependencies,
        dependencyClicked = {
            openUrl(it.url)
        },
        header = {
            this.Header()
        },
        footer = {
            this.Footer(
                debugGuid = guids,
                copyToClipboard = copyToClipboard
            )
        },
        isCompact = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded,
        showBack = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        backClicked = backClicked,
        contactEmail = contactEmail,
        appVersion = applicationId,
        links = listOf(
            Link.github { openUrl(GITHUB) },
            Link.play { openPlaystore() },
            Link.email { openEmail() }
        )
    )
}

@Composable
private fun ColumnScope.Header() {
    TextBody1(
        text = stringResource(id = R.string.dependency_thank_you),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.Footer(
    debugGuid: String,
    copyToClipboard: (String) -> Unit
) {
    TextBody1(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        text = stringResource(id = R.string.about_additional)
    )
    TextBody2(
        text = debugGuid,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { },
                onLongClick = { copyToClipboard(debugGuid) }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}