package tmg.flashback.repo.toggle

interface ToggleRepository {

    val isRSSEnabled: Boolean

    val isNotificationChannelsSupported: Boolean
}