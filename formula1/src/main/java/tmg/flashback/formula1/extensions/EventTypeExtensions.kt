package tmg.flashback.formula1.extensions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.formula1.R
import tmg.flashback.formula1.enums.EventType

val EventType.icon: Int
    @DrawableRes
    get() = when (this) {
        EventType.TESTING -> R.drawable.ic_event_type_testing
        EventType.CAR_LAUNCH -> R.drawable.ic_event_type_car_launch
        EventType.OTHER -> R.drawable.ic_event_type_other
    }

val EventType.label: Int
    @StringRes
    get() = when (this) {
        EventType.TESTING -> R.string.event_type_testing
        EventType.CAR_LAUNCH -> R.string.event_type_car_launch
        EventType.OTHER -> R.string.event_type_other
    }