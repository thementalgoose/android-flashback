package tmg.flashback.statistics.extensions

import androidx.annotation.StringRes
import tmg.flashback.formula1.enums.EventType
import tmg.flashback.statistics.R

val EventType.label: Int
    @StringRes
    get() = when (this) {
        EventType.TESTING -> R.string.dashboard_season_event_type_testing
        EventType.CAR_LAUNCH -> R.string.dashboard_season_event_type_car_launch
        EventType.OTHER -> R.string.dashboard_season_event_type_other
    }