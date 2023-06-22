package tmg.flashback.newrelic.usecases

import android.content.Context
import tmg.flashback.newrelic.services.NewRelicService
import javax.inject.Inject

class InitializeNewRelicUseCase @Inject constructor(
    private val newRelicService: NewRelicService
) {
    fun start(applicationContext: Context) {
        newRelicService.start(applicationContext)
    }
}