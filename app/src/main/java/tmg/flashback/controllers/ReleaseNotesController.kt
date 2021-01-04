package tmg.flashback.controllers

import tmg.flashback.constants.Releases
import tmg.flashback.managers.buildconfig.BuildConfigManager
import tmg.flashback.repo.pref.DeviceRepository

/**
 * Controller around release notes being displayed in the app
 */
class ReleaseNotesController(
        private val deviceRepository: DeviceRepository,
        private val buildConfigManager: BuildConfigManager
) {

    /**
     * Determine if there are release notes that should
     * be shown
     */
    val pendingReleaseNotes: Boolean
        get() = majorReleaseNotes.isNotEmpty()

    /**
     * The release notes to show since the release notes had last been read
     * Will show only those which are considered major (and will only show the last one!)
     */
    val majorReleaseNotes: List<Releases>
        get() {
            val currentVersion = buildConfigManager.versionCode
            val lastVersion = releaseNotesLastSeenVersionCode

            return if (currentVersion > lastVersion) {
                Releases.values()
                        .sortedByDescending { it.version }
                        .filter { it.version in lastVersion..currentVersion }
                        .filter { it.isMajor }
                        .take(3)
            } else {
                emptyList()
            }
        }

    /**
     * Mark that we have seen the release notes
     */
    fun markReleaseNotesSeen() {
        deviceRepository.releaseNotesSeenAppVersion = buildConfigManager.versionCode
    }

    private val releaseNotesLastSeenVersionCode: Int
        get() = when (val version = deviceRepository.releaseNotesSeenAppVersion) {
            0 -> {
                markReleaseNotesSeen()
                buildConfigManager.versionCode
            }
            else -> version
        }
}