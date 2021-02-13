package tmg.flashback.ui.settings.customisation.animation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.ui.BaseViewModel
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.core.utils.Selected
import tmg.flashback.core.utils.StringHolder
import tmg.flashback.core.ui.bottomsheet.BottomSheetItem
import tmg.utilities.lifecycle.Event

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
        private val appearanceController: AppearanceController
): BaseViewModel(), AnimationSpeedViewModelInputs, AnimationSpeedViewModelOutputs {

    var inputs: AnimationSpeedViewModelInputs = this
    var outputs: AnimationSpeedViewModelOutputs = this

    override val animationSpeedPreference: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val animationSpeedUpdated: MutableLiveData<Event> = MutableLiveData()

    init {
        updateAnimationList()
    }

    //region Inputs

    override fun selectAnimationSpeed(animationSpeed: AnimationSpeed) {
        appearanceController.animationSpeed = animationSpeed
        updateAnimationList()
        animationSpeedUpdated.value = Event()
    }

    //endregion

    private fun updateAnimationList() {
        animationSpeedPreference.value = AnimationSpeed.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == appearanceController.animationSpeed)
                }
    }
}
