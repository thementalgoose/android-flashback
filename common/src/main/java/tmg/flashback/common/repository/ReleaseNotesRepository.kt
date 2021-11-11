package tmg.flashback.common.repository

import tmg.flashback.prefs.manager.PreferenceManager

class ReleaseNotesRepository(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        private const val keyReleaseNotesSeenVersion: String = "RELEASE_NOTES_SEEN_VERSION"
    }

    /**
     * App version for which the release notes had last been seen
     */
    var releaseNotesSeenAppVersion: Int
        get() = preferenceManager.getInt(keyReleaseNotesSeenVersion, 0)
        set(value) = preferenceManager.save(keyReleaseNotesSeenVersion, value)
}