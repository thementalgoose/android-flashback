package tmg.flashback.shared.ui.ui.animation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.shared.ui.model.AnimationSpeed
import tmg.flashback.shared.ui.bottomsheet.BottomSheetItem
import tmg.flashback.shared.ui.controllers.ThemeController
import tmg.flashback.shared.ui.extensions.icon
import tmg.flashback.shared.ui.extensions.label
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
        private val themeController: ThemeController
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
        themeController.animationSpeed = animationSpeed
        updateAnimationList()
        animationSpeedUpdated.value = Event()
    }

    //endregion

    private fun updateAnimationList() {
        animationSpeedPreference.value = AnimationSpeed.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == themeController.animationSpeed)
                }
    }
}
