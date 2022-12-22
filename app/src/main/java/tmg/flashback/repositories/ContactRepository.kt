package tmg.flashback.repositories

import tmg.flashback.BuildConfig
import tmg.flashback.configuration.manager.ConfigManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val configManager: ConfigManager
) {

    companion object {
        private const val keyEmail: String = "email"

        private const val fallbackEmail: String = BuildConfig.CONTACT_EMAIL
    }

    val contactEmail: String
        get() = configManager.getString(keyEmail) ?: fallbackEmail
}