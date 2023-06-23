package tmg.flashback.newrelic.usecases

import tmg.flashback.newrelic.services.NewRelicService
import javax.inject.Inject

class LogEventUseCase @Inject constructor(
    private val newRelicService: NewRelicService
) {

    fun log(eventName: String, attributes: Map<String, Any>) {
        newRelicService.logEvent(eventName, attributes)
    }
}