@file:OptIn(ExperimentalPagerApi::class, ExperimentalPagerApi::class)

package tmg.flashback.debug.core.styleguide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import tmg.flashback.debug.core.styleguide.tabs.ButtonTabScreen
import tmg.flashback.debug.core.styleguide.tabs.InputTabScreen
import tmg.flashback.debug.core.styleguide.tabs.TextTabScreen
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonPrimary

@Composable
internal fun StyleGuideComposeLayout(
    modifier: Modifier = Modifier,
    actionUpClicked: () -> Unit
) {
    val currentTab: MutableState<StyleGuideTabs?> = remember { mutableStateOf(null) }

    Column(modifier = modifier.fillMaxSize()) {
        IconButton(
            onClick = {
                if (currentTab.value != null) {
                    currentTab.value = null
                } else {
                    actionUpClicked()
                }
            },
            content = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = AppTheme.colors.contentPrimary
                )
            }
        )
        when (currentTab.value) {
            StyleGuideTabs.TEXT -> {
                TextTabScreen()
            }
            StyleGuideTabs.INPUT -> {
                InputTabScreen()
            }
            StyleGuideTabs.BUTTON -> {
                ButtonTabScreen()
            }
            null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppTheme.dimens.medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StyleGuideTabs.values().forEach { tab ->
                        ButtonPrimary(
                            text = tab.label,
                            onClick = { currentTab.value = tab }
                        )
                    }
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        StyleGuideComposeLayout(
            actionUpClicked = { }
        )
    }
}
