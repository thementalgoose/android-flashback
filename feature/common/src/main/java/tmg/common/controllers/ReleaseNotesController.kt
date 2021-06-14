package tmg.common.controllers

import tmg.common.constants.ReleaseNotes
import tmg.common.repository.ReleaseNotesRepository
import tmg.core.device.managers.BuildConfigManager

/**
 * Controller around release notes being displayed in the app
 */
class ReleaseNotesController(
    private val releaseNotesRepository: ReleaseNotesRepository,
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
    val majorReleaseNotes: List<ReleaseNotes>
        get() {
            val currentVersion = buildConfigManager.versionCode
            val lastVersion = releaseNotesLastSeenVersionCode

            return if (currentVersion > lastVersion) {
                ReleaseNotes.values()
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
        releaseNotesRepository.releaseNotesSeenAppVersion = buildConfigManager.versionCode
    }

    private val releaseNotesLastSeenVersionCode: Int
        get() = when (val version = releaseNotesRepository.releaseNotesSeenAppVersion) {
            0 -> {
                markReleaseNotesSeen()
                buildConfigManager.versionCode
            }
            else -> version
        }
}