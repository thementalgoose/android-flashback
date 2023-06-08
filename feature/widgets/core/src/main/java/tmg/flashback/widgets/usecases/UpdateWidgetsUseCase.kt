package tmg.flashback.widgets.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.widgets.updateAllWidgets
import javax.inject.Inject

class UpdateWidgetsUseCase @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
) {
    fun update() {
        applicationContext.updateAllWidgets()
    }
}