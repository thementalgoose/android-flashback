package tmg.flashback.reactiongame.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.reactiongame.LightsOutDelayProvider
import javax.inject.Inject

@HiltViewModel
class ReactionViewModel @Inject constructor(
    private val lightsOutDelayProvider: LightsOutDelayProvider,
    private val timeManager: TimeManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    private companion object {
        const val TIME_BETWEEN_LIGHTS = 1000L
        const val LIGHTS = 5
        const val TEST_TIMEOUT = 5000L
    }

    private var job = SupervisorJob() + ioDispatcher

    private var lightsOutTime: Long = 0

    private val _uiState: MutableStateFlow<ReactionUiState> = MutableStateFlow(ReactionUiState.Start)
    val uiState: StateFlow<ReactionUiState> = _uiState

    fun start() {

        val lightsOutDelay = lightsOutDelayProvider.getDelay()
        job = viewModelScope.launch(ioDispatcher) {
            // Count lights in
            for (i in 0..LIGHTS) {
                _uiState.value = ReactionUiState.Game(
                    lights = i
                )
                delay(TIME_BETWEEN_LIGHTS)
            }

            // Hold delay
            delay(lightsOutDelay)
            setLightsOutTime()
            _uiState.value = ReactionUiState.Game(
                lights = 0,
                hasDisplayedSequence = true
            )

            ensureActive()
            delay(TEST_TIMEOUT)

            ensureActive()
            _uiState.value = ReactionUiState.Missed
        }
    }

    fun setLightsOutTime() {
        this.lightsOutTime = timeManager.nowMillis
        println("Lights out time: " + lightsOutTime)
    }

    fun react() {
        val clickedTime = timeManager.nowMillis
        val reactionTime = clickedTime - lightsOutTime
        job.cancel()
        println("Reaction time: " + reactionTime + " (click = " + clickedTime + ", lightsOut = " + lightsOutTime + ")")

        // Clicked before lights out. Jump start
        if (this.lightsOutTime == 0L || reactionTime < 0L) {
            _uiState.value = ReactionUiState.JumpStart
            return
        }

        _uiState.value = ReactionUiState.Results(
            timeMillis = reactionTime,
            tier = when (reactionTime) {
                in 0..150 -> ReactionResultTier.SUPERHUMAN
                in 150..180 -> ReactionResultTier.GREAT
                in 180..230 -> ReactionResultTier.GOOD
                in 230..270 -> ReactionResultTier.AVERAGE
                in 270..320 -> ReactionResultTier.NOT_GOOD
                else -> ReactionResultTier.POOR
            }
        )
        this.lightsOutTime = 0L
    }
}