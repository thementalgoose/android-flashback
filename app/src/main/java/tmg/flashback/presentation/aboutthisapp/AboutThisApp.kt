package tmg.flashback.presentation.aboutthisapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.aboutthisapp.AboutThisAppTheme
import tmg.aboutthisapp.configuration.AboutThisAppColors
import tmg.aboutthisapp.configuration.Dependency
import tmg.aboutthisapp.configuration.Link
import tmg.aboutthisapp.presentation.AboutThisAppScreen
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
fun AboutThisApp(
    windowSizeClass: WindowSizeClass,
    backClicked: () -> Unit,
    viewModel: AboutThisAppViewModel = hiltViewModel(),
) {
    val config = viewModel.config.collectAsState()

    AboutThisAppTheme(
        lightColors = AboutThisAppColors(config.value.lightColors!!),
        darkColors = AboutThisAppColors(config.value.darkColors!!),
    ) {
        AboutThisApp(
            windowSizeClass = windowSizeClass,
            backClicked = backClicked,
            dependencies = config.value.dependencies,
            guids = config.value.debugInfo ?: "",
            applicationId = config.value.appPackageName,
            contactEmail = config.value.email ?: "",
            copyToClipboard = viewModel::copyToClipboard,
            openUrl = viewModel::openUrl,
            openGithub = {
                config.value.github?.let {
                    viewModel.openUrl(it)
                }
            },
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
    openGithub: () -> Unit,
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
            Link.github { openGithub() },
            Link.play { openPlaystore() },
            Link.email { openEmail() }
        )
    )
}

@Composable
private fun ColumnScope.Header() {
    TextBody1(
        text = stringResource(id = string.dependency_thank_you),
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
        text = stringResource(id = string.about_additional)
    )
    TextBody2(
        text = debugGuid,
        modifier = Modifier
            .alpha(0.7f)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { },
                onLongClick = { copyToClipboard(debugGuid) }
            )
            .padding(horizontal = 16.dp, vertical = 18.dp),
    )
}