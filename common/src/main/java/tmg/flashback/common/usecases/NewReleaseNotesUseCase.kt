package tmg.flashback.common.usecases

import tmg.flashback.common.constants.ReleaseNotes
import tmg.flashback.common.repository.ReleaseNotesRepository
import tmg.flashback.device.managers.BuildConfigManager

class NewReleaseNotesUseCase(
    private val releaseNotesRepository: ReleaseNotesRepository,
    private val buildConfigManager: BuildConfigManager
) {
    fun getNotes(): List<ReleaseNotes> {
        val currentVersion = buildConfigManager.versionCode
        val lastVersion = releaseNotesLastSeenVersionCode

        return if (currentVersion > lastVersion) {
            ReleaseNotes.values()
                .sortedByDescending { it.version }
                .filter { it.version in (lastVersion + 1)..currentVersion }
                .filter { it.isMajor }
                .take(1)
        } else {
            emptyList()
        }
    }

    private val releaseNotesLastSeenVersionCode: Int
        get() = when (val version = releaseNotesRepository.releaseNotesSeenAppVersion) {
            0 -> {
                releaseNotesRepository.releaseNotesSeen()
                buildConfigManager.versionCode
            }
            else -> version
        }
}