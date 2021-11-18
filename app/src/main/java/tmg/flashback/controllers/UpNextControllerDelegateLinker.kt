package tmg.flashback.controllers

import tmg.flashback.statistics.controllers.UpNextControllerDelegate
import tmg.flashback.upnext.controllers.UpNextController

// Temporary class while up next and stats modules are separate
class UpNextControllerDelegateLinker(
    private val upNextController: UpNextController
): UpNextControllerDelegate {
    override val notificationsRaceEnabled: Boolean
        get() = upNextController.notificationRace
    override val notificationsQualifyingEnabled: Boolean
        get() = upNextController.notificationQualifying
    override val notificationsFreePracticeEnabled: Boolean
        get() = upNextController.notificationFreePractice
}