package tmg.flashback.controllers

import tmg.flashback.constants.Releases
import tmg.flashback.core.managers.BuildConfigManager
import tmg.flashback.core.repositories.CoreRepository

/**
 * Controller around release notes being displayed in the app
 */
class ReleaseNotesController(
    private val coreRepository: CoreRepository,
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
                        .filter { it.version in (lastVersion + 1)..currentVersion }
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
        coreRepository.releaseNotesSeenAppVersion = buildConfigManager.versionCode
    }

    private val releaseNotesLastSeenVersionCode: Int
        get() = when (val version = coreRepository.releaseNotesSeenAppVersion) {
            0 -> {
                markReleaseNotesSeen()
                buildConfigManager.versionCode
            }
            else -> version
        }
}