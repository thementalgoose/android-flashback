package tmg.flashback.reactiongame.presentation.lights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme


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

@Composable
internal fun RaceStartLights(
    modifier: Modifier = Modifier,
    state: StartLightState
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(1.4f),
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
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LightColumn(
                modifier = Modifier.weight(1f),
                green = state is StartLightState.Ready,
                amber = state is StartLightState.AbortedStart,
                red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 1
            )
            LightColumn(
                modifier = Modifier.weight(1f),
                green = state is StartLightState.Ready,
                amber = false,
                red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 2
            )
            LightColumn(
                modifier = Modifier.weight(1f),
                green = state is StartLightState.Ready,
                amber = state is StartLightState.AbortedStart,
                red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 3
            )
            LightColumn(
                modifier = Modifier.weight(1f),
                green = state is StartLightState.Ready,
                amber = false,
                red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 4
            )
            LightColumn(
                modifier = Modifier.weight(1f),
                green = state is StartLightState.Ready,
                amber = state is StartLightState.AbortedStart,
                red = ((state as? StartLightState.StartSequence)?.redLights ?: -1) >= 5
            )
        }

    }
}

@Composable
private fun RowScope.LightColumn(
    modifier: Modifier = Modifier,
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
            lit = red
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

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(StartLightStateProvider::class) state: StartLightState
) {
    AppThemePreview {
        RaceStartLights(state = state)
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
