package tmg.flashback.managers.appsettings

import android.content.Context
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.extensions.isLightMode
import tmg.flashback.core.managers.AppSettingsManager

class FlashbackAppSettingsManager(
    private val appearanceController: AppearanceController
): AppSettingsManager {

    override fun openAboutThisApp(context: Context) {
        context.startActivity(
            AboutThisAppActivity.intent(
                context = context,
                configuration = AboutThisAppConfig.configuration(context, !appearanceController.currentTheme.isLightMode(context))
        ))
    }
}