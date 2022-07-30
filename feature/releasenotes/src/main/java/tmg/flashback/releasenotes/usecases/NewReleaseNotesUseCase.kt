package tmg.flashback.releasenotes.usecases

import tmg.flashback.releasenotes.constants.ReleaseNotes
import tmg.flashback.releasenotes.repository.ReleaseNotesRepository
import tmg.flashback.device.managers.BuildConfigManager
import javax.inject.Inject

class NewReleaseNotesUseCase @Inject constructor(
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