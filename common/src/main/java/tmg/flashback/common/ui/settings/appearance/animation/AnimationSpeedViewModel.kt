package tmg.flashback.common.ui.settings.appearance.animation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.extensions.icon
import tmg.flashback.ui.extensions.label
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.ui.repository.ThemeRepository
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface AnimationSpeedViewModelInputs {
    fun selectAnimationSpeed(animationSpeed: AnimationSpeed)
}

//endregion

//region Outputs

interface AnimationSpeedViewModelOutputs {

    val animationSpeedPreference: LiveData<List<Selected<BottomSheetItem>>>
    val animationSpeedUpdated: LiveData<Event>
}

//endregion


class AnimationSpeedViewModel(
        private val themeRepository: ThemeRepository
): ViewModel(), AnimationSpeedViewModelInputs, AnimationSpeedViewModelOutputs {

    var inputs: AnimationSpeedViewModelInputs = this
    var outputs: AnimationSpeedViewModelOutputs = this

    override val animationSpeedPreference: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val animationSpeedUpdated: MutableLiveData<Event> = MutableLiveData()

    init {
        updateAnimationList()
    }

    //region Inputs

    override fun selectAnimationSpeed(animationSpeed: AnimationSpeed) {
        themeRepository.animationSpeed = animationSpeed
        updateAnimationList()
        animationSpeedUpdated.value = Event()
    }

    //endregion

    private fun updateAnimationList() {
        animationSpeedPreference.value = AnimationSpeed.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == themeRepository.animationSpeed)
                }
    }
}
