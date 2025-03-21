package tmg.flashback.device.usecases

import tmg.flashback.device.managers.WidgetManager
import javax.inject.Inject

class RefreshWidgetUseCase @Inject constructor(
    private val widgetManager: WidgetManager
) {
    fun update(widgetProvider: Class<*>) {
        widgetManager.updateWidgets(widgetProvider)
    }
}