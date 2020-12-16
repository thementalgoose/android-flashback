package tmg.flashback.repo.config

interface ToggleRepository {

    val isRSSEnabled: Boolean

    val isNotificationChannelsSupported: Boolean
}