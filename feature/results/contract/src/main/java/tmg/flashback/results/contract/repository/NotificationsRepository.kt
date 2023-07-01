package tmg.flashback.results.contract.repository

import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.contract.repository.models.NotificationUpcoming

interface NotificationsRepository {

    fun isUpcomingEnabled(upcoming: NotificationUpcoming): Boolean
    fun setUpcomingEnabled(upcoming: NotificationUpcoming, value: Boolean)

    fun isEnabled(resultsAvailable: NotificationResultsAvailable): Boolean
    fun setEnabled(upcoming: NotificationResultsAvailable, value: Boolean)
}