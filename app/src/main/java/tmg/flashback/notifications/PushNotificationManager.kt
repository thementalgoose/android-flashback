package tmg.flashback.notifications

interface PushNotificationManager {
    suspend fun raceSubscribe(): Boolean
    suspend fun raceUnsubscribe(): Boolean

    suspend fun qualifyingSubscribe(): Boolean
    suspend fun qualifyingUnsubscribe(): Boolean

    fun createChannels()
}