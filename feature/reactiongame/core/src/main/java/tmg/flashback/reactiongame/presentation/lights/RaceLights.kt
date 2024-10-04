package tmg.flashback.reactiongame.presentation.lights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview


private val columnColor: Color = Color(0xFF202020)
private val panelBarColor: Color = Color(0xFF0A0A0A)
private val lightOffColor: Color = Color(0xFF282828)
private val lightShadeColor: Color = Color(0xFF080808)

private val panelBarHeight = 16.dp

sealed class StartLightState {
    data object AbortedStart: StartLightState()
    data object Ready: StartLightState()
    data class StartSequence(
        val redLights: Int
    ): StartLightState()
}

enum class LightPanel {
    FULL_HEIGHT,
    FULL_HEIGHT_DOUBLE_HEIGHT_RED,
    HALF_HEIGHT
}

@Composable
internal fun RaceStartLights(
    modifier: Modifier = Modifier,
    state: StartLightState,
    panelType: LightPanel = LightPanel.FULL_HEIGHT
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(if (panelType == LightPanel.HALF_HEIGHT) 2.2f else 1.4f),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.weight(2f))
            Box(
                modifier
                    .fillMaxWidth()
                    .height(panelBarHeight)
                    .background(panelBarColor)
            )
            Spacer(Modifier.weight(2.2f))
            Box(
                modifier
                    .fillMaxWidth()
                    .height(panelBarHeight)
                    .background(panelBarColor)
            )
            Spacer(Modifier.weight(1f))
        }
        if (panelType == LightPanel.HALF_HEIGHT) {
            Lights10(state = state)
        } else {
            Lights20(
                state = state,
                dualRedLights = panelType == LightPanel.FULL_HEIGHT_DOUBLE_HEIGHT_RED
            )
        }
    }
}

@Composable
private fun BoxScope.Lights20(
    state: StartLightState,
    dualRedLights: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LightFourColumn(
            modifier = Modifier.weight(1f),
            dualRedLights = dualRedLights,
            green = state is StartLightState.Ready,
            amber = state is StartLightState.AbortedStart,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 1
        )
        LightFourColumn(
            modifier = Modifier.weight(1f),
            dualRedLights = dualRedLights,
            green = state is StartLightState.Ready,
            amber = false,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 2
        )
        LightFourColumn(
            modifier = Modifier.weight(1f),
            dualRedLights = dualRedLights,
            green = state is StartLightState.Ready,
            amber = state is StartLightState.AbortedStart,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 3
        )
        LightFourColumn(
            modifier = Modifier.weight(1f),
            dualRedLights = dualRedLights,
            green = state is StartLightState.Ready,
            amber = false,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 4
        )
        LightFourColumn(
            modifier = Modifier.weight(1f),
            dualRedLights = dualRedLights,
            green = state is StartLightState.Ready,
            amber = state is StartLightState.AbortedStart,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 5
        )
    }
}

@Composable
private fun BoxScope.Lights10(
    state: StartLightState
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LightTwoColumn(
            modifier = Modifier.weight(1f),
            isGreen = false,
            green = state is StartLightState.Ready,
            amber = state is StartLightState.AbortedStart,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 1
        )
        LightTwoColumn(
            modifier = Modifier.weight(1f),
            isGreen = true,
            green = state is StartLightState.Ready,
            amber = false,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 2
        )
        LightTwoColumn(
            modifier = Modifier.weight(1f),
            isGreen = false,
            green = state is StartLightState.Ready,
            amber = state is StartLightState.AbortedStart,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 3
        )
        LightTwoColumn(
            modifier = Modifier.weight(1f),
            isGreen = true,
            green = state is StartLightState.Ready,
            amber = false,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 4
        )
        LightTwoColumn(
            modifier = Modifier.weight(1f),
            isGreen = false,
            green = state is StartLightState.Ready,
            amber = state is StartLightState.AbortedStart,
            red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 5
        )
    }
}

@Composable
private fun RowScope.LightFourColumn(
    modifier: Modifier = Modifier,
    dualRedLights: Boolean = true,
    green: Boolean = false,
    amber: Boolean = false,
    red: Boolean = false
) {
    Column(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .background(columnColor)
        .padding(8.dp)
    ) {
        Light(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            lightOnColor = AppTheme.colors.f1StartLightGreen,
            lit = green
        )
        Light(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            lightOnColor = AppTheme.colors.f1StartLightAmber,
            lit = amber
        )
        Light(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            lightOnColor = AppTheme.colors.f1StartLightRed,
            lit = red && dualRedLights
        )
        Light(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            lightOnColor = AppTheme.colors.f1StartLightRed,
            lit = red
        )
    }
}

@Composable
private fun RowScope.LightTwoColumn(
    modifier: Modifier = Modifier,
    isGreen: Boolean = false,
    green: Boolean = false,
    amber: Boolean = false,
    red: Boolean = false
) {
    Column(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .background(columnColor)
        .padding(8.dp)
    ) {
        Light(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            lightOnColor = when (isGreen) {
                true -> AppTheme.colors.f1StartLightGreen
                false -> AppTheme.colors.f1StartLightAmber
            },
            lit = when (isGreen) {
                true -> green
                else -> amber
            }
        )
        Light(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            lightOnColor = AppTheme.colors.f1StartLightRed,
            lit = red
        )
    }
}

@Composable
private fun ColumnScope.Light(
    modifier: Modifier = Modifier,
    lightOnColor: Color,
    lit: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(lightShadeColor)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(top = 8.dp)
                .clip(CircleShape)
                .background(if (lit) lightOnColor else lightOffColor)
        )
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun Preview10(
    @PreviewParameter(StartLightStateProvider::class) state: StartLightState
) {
    AppThemePreview {
        RaceStartLights(
            state = state,
            panelType = LightPanel.HALF_HEIGHT
        )
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun Preview20(
    @PreviewParameter(StartLightStateProvider::class) state: StartLightState
) {
    AppThemePreview {
        RaceStartLights(
            state = state,
            panelType = LightPanel.FULL_HEIGHT
        )
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun Preview20HalfHeight(
    @PreviewParameter(StartLightStateProvider::class) state: StartLightState
) {
    AppThemePreview {
        RaceStartLights(
            state = state,
            panelType = LightPanel.FULL_HEIGHT_DOUBLE_HEIGHT_RED
        )
    }
}

internal class StartLightStateProvider: PreviewParameterProvider<StartLightState> {
    override val values: Sequence<StartLightState> = sequenceOf(
        StartLightState.AbortedStart,
        StartLightState.Ready,
        StartLightState.StartSequence(0),
        StartLightState.StartSequence(1),
        StartLightState.StartSequence(2),
        StartLightState.StartSequence(3),
        StartLightState.StartSequence(4),
        StartLightState.StartSequence(5)
    )
}
