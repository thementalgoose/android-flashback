@file:OptIn(ExperimentalPagerApi::class, ExperimentalPagerApi::class)

package tmg.flashback.sandbox.core.presentation.styleguide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import tmg.flashback.sandbox.core.presentation.styleguide.tabs.ButtonTabScreen
import tmg.flashback.sandbox.core.presentation.styleguide.tabs.ColourTabScreen
import tmg.flashback.sandbox.core.presentation.styleguide.tabs.InputTabScreen
import tmg.flashback.sandbox.core.presentation.styleguide.tabs.TextTabScreen
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonPrimary

@Composable
internal fun StyleGuideComposeLayout(
    modifier: Modifier = Modifier,
    startingTab: StyleGuideTabs? = null,
    actionUpClicked: () -> Unit,
    changeNightMode: () -> Unit,
) {
    val currentTab: MutableState<StyleGuideTabs?> = rememberSaveable { mutableStateOf(startingTab) }
    Scaffold(
        content = {
            Column(modifier = modifier
                .fillMaxSize()
                .padding(it)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                    IconButton(
                        onClick = {
                            changeNightMode()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Theme",
                                tint = AppTheme.colors.contentPrimary
                            )
                        }
                    )
                }
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
                    StyleGuideTabs.COLOURS -> {
                        ColourTabScreen()
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
    )
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(StyleGuideTabProvider::class) tab: StyleGuideTabs
) {
    AppThemePreview {
        StyleGuideComposeLayout(
            startingTab = tab,
            actionUpClicked = { },
            changeNightMode = { }
        )
    }
}

internal class StyleGuideTabProvider: PreviewParameterProvider<StyleGuideTabs> {
    override val values: Sequence<StyleGuideTabs> = StyleGuideTabs.values().asSequence()
}