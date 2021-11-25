package tmg.flashback.statistics.extensions

import tmg.flashback.statistics.ui.dashboard.season.SeasonNavItem

val SeasonNavItem.analyticsLabel: String
    get() = when (this) {
        SeasonNavItem.CALENDAR -> "select_dashboard_calendar"
        SeasonNavItem.SCHEDULE -> "select_dashboard_schedule"
        SeasonNavItem.DRIVERS -> "select_dashboard_driver"
        SeasonNavItem.CONSTRUCTORS -> "select_dashboard_constructor"
    }