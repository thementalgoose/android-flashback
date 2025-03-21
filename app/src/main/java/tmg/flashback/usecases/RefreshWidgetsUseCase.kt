package tmg.flashback.usecases

import tmg.flashback.device.usecases.RefreshWidgetUseCase
import tmg.flashback.widgets.presentation.UpNextWidgetReceiver
import javax.inject.Inject

class RefreshWidgetsUseCase @Inject constructor(
    private val refreshWidgetsUseCase: RefreshWidgetUseCase
) {
    fun update() {
        refreshWidgetsUseCase.update(UpNextWidgetReceiver::class.java)
    }
}