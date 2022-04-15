package tmg.flashback.releasenotes.repository

import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.prefs.manager.PreferenceManager

class ReleaseNotesRepository(
    private val preferenceManager: PreferenceManager,
    private val buildConfigManager: BuildConfigManager
) {
    companion object {
        private const val keyReleaseNotesSeenVersion: String = "RELEASE_NOTES_SEEN_VERSION"
    }

    /**
     * App version for which the release notes had last been seen
     */
    var releaseNotesSeenAppVersion: Int
        get() = preferenceManager.getInt(keyReleaseNotesSeenVersion, 0)
        private set(value) = preferenceManager.save(keyReleaseNotesSeenVersion, value)

    fun releaseNotesSeen() {
        releaseNotesSeenAppVersion = buildConfigManager.versionCode
    }
}