package tmg.flashback.controllers

import tmg.flashback.repo.pref.DeviceRepository

/**
 * Controller around release notes being displayed in the app
 */
class ReleaseNotesController(
        private val deviceRepository: DeviceRepository
) {

    val showReleaseNotes: Boolean = false

    fun markReleaseNotesSeen() {
        /* Do nothing */
    }
//
//    val lastAppVersion: Int
//        get() = deviceRepository.lastAppVersion
//    val shouldShowReleaseNotes: Boolean
//        get() =

}