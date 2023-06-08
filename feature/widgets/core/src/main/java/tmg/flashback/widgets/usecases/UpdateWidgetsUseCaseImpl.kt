package tmg.flashback.widgets.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.widgets.contract.usecases.UpdateWidgetsUseCase
import tmg.flashback.widgets.updateAllWidgets
import javax.inject.Inject

internal class UpdateWidgetsUseCaseImpl @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
): UpdateWidgetsUseCase {
    override fun update() {
        applicationContext.updateAllWidgets()
    }
}