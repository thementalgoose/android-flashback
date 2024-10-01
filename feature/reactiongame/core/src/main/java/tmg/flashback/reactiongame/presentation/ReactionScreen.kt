@file:OptIn(ExperimentalComposeUiApi::class)

package tmg.flashback.reactiongame.presentation

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.reactiongame.presentation.lights.LightPanel
import tmg.flashback.reactiongame.presentation.lights.RaceStartLights
import tmg.flashback.reactiongame.presentation.lights.StartLightState
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.foldables.isWidthExpanded

@Composable
fun ReactionScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: ReactionViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Reaction Game", args = mapOf())

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ReactionScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        state = uiState.value,
        start = viewModel::start,
        react = viewModel::react,
        reset = viewModel::reset
    )
}

@Composable
private fun ReactionScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    state: ReactionUiState,
    start: () -> Unit,
    reset: () -> Unit,
    react: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
    ) {
        Header(
            text = stringResource(string.reaction_screen_title),
            action = when (windowSizeClass.isWidthExpanded) {
                false -> HeaderAction.MENU
                true -> null
            },
            actionUpClicked = actionUpClicked
        )
        Box(modifier = Modifier
            .weight(1f)
            .background(AppTheme.colors.backgroundPrimary)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        when (state) {
                            ReactionUiState.Start -> start()
                            ReactionUiState.JumpStart -> reset()
                            ReactionUiState.Missed -> reset()
                            is ReactionUiState.Game -> react()
                            is ReactionUiState.Results -> reset()
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {}
                    MotionEvent.ACTION_UP -> {}
                    else ->  false
                }
                true
            }
        ) {
            if (windowSizeClass.widthSizeClass ==  WindowWidthSizeClass.Compact) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RaceStartLights(
                        modifier = Modifier
                            .weight(1f)
                            .padding(32.dp),
                        state = when (state) {
                            is ReactionUiState.Game -> StartLightState.StartSequence(state.lights)
                            ReactionUiState.JumpStart -> StartLightState.AbortedStart
                            ReactionUiState.Missed -> StartLightState.AbortedStart
                            is ReactionUiState.Results -> StartLightState.StartSequence(0)
                            ReactionUiState.Start -> StartLightState.Ready
                        },
                        panelType = LightPanel.HALF_HEIGHT
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        val instructions = when (state) {
                            is ReactionUiState.Game -> stringResource(string.reaction_screen_tap_input)
                            ReactionUiState.JumpStart -> stringResource(string.reaction_screen_tap_reset)
                            ReactionUiState.Missed -> stringResource(string.reaction_screen_tap_reset)
                            is ReactionUiState.Results -> stringResource(string.reaction_screen_tap_reset)
                            ReactionUiState.Start -> stringResource(string.reaction_screen_start)
                        }
                        TextBody2(
                            text = instructions,
                            modifier = Modifier.padding(AppTheme.dimens.medium),
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RaceStartLights(
                        modifier = Modifier
                            .weight(1f)
                            .padding(32.dp),
                        state = when (state) {
                            is ReactionUiState.Game -> StartLightState.StartSequence(state.lights)
                            ReactionUiState.JumpStart -> StartLightState.AbortedStart
                            ReactionUiState.Missed -> StartLightState.AbortedStart
                            is ReactionUiState.Results -> StartLightState.StartSequence(0)
                            ReactionUiState.Start -> StartLightState.Ready
                        },
                        panelType = LightPanel.HALF_HEIGHT
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        val instructions = when (state) {
                            is ReactionUiState.Game -> stringResource(string.reaction_screen_tap_input)
                            ReactionUiState.JumpStart -> stringResource(string.reaction_screen_tap_reset)
                            ReactionUiState.Missed -> stringResource(string.reaction_screen_tap_reset)
                            is ReactionUiState.Results -> stringResource(string.reaction_screen_tap_reset)
                            ReactionUiState.Start -> stringResource(string.reaction_screen_start)
                        }
                        TextBody2(
                            text = instructions,
                            modifier = Modifier
                                .padding(AppTheme.dimens.medium),
                        )
                    }
                }
            }
        }
    }
}