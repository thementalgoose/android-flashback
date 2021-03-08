package tmg.flashback.managers.notifications

interface PushNotificationManager {
    suspend fun raceSubscribe(): Boolean
    suspend fun raceUnsubscribe(): Boolean

    suspend fun qualifyingSubscribe(): Boolean
    suspend fun qualifyingUnsubscribe(): Boolean

    suspend fun seasonInfoSubscribe(): Boolean
    suspend fun seasonInfoUnsubscribe(): Boolean

    fun createChannels()
}