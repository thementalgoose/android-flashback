package tmg.flashback.data.repositories

import tmg.flashback.data.enums.NotificationRegistration

/**
 * Storage variables that the user interacts with or manipulates through
 *   the usage of the app
 */
interface AppRepository {

    /**
     * Show the qualifying delta in the layout
     */
    var showQualifyingDelta: Boolean

    /**
     * Fade the race results where the driver results in DNF
     */
    var fadeDNF: Boolean

    /**
     * Show the favourited bottom sheet section expanded by default
     */
    var showListFavourited: Boolean

    /**
     * Show the all bottom sheet section expanded by default
     */
    var showListAll: Boolean

    /**
     * Show grid penalties in qualifying
     */
    var showGridPenaltiesInQualifying: Boolean

    /**
     * Favourited seasons in the list
     */
    var favouriteSeasons: Set<Int>

    /**
     * Default season to be shown in the app
     *  null = no default set
     */
    var defaultSeason: Int?

    /**
     * What should happen when the widget is clicked
     * true = Open the app
     * false = Refresh the widget
     */
    var widgetOpenApp: Boolean

    //region Notifications

    /**
     * Race Notification preference
     *   null = hasn't been set
     */
    var notificationsRace: NotificationRegistration?

    /**
     * Qualifying Notification preference
     *   null = hasn't been set
     */
    var notificationsQualifying: NotificationRegistration?

    /**
     * Miscellaneous Notification preference
     *   null = hasn't been set
     */
    var notificationsSeasonInfo: NotificationRegistration?

    //endregion
}