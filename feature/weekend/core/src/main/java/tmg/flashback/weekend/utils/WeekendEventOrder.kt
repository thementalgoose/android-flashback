package tmg.flashback.weekend.utils

import tmg.flashback.weekend.presentation.WeekendNavItem
import tmg.flashback.weekend.presentation.WeekendNavItem.QUALIFYING
import tmg.flashback.weekend.presentation.WeekendNavItem.RACE
import tmg.flashback.weekend.presentation.WeekendNavItem.SCHEDULE
import tmg.flashback.weekend.presentation.WeekendNavItem.SPRINT
import tmg.flashback.weekend.presentation.WeekendNavItem.SPRINT_QUALIFYING
import tmg.flashback.weekend.presentation.WeekendScreenState

internal fun getWeekendEventOrder(
    navItem: WeekendNavItem,
    hasSprintQualifying: Boolean,
    hasSprintRace: Boolean,
    season: Int
): List<WeekendScreenState> {
    when {
        season < 2021 -> {
            return listOf(
                WeekendScreenState(SCHEDULE, isSelected = navItem == SCHEDULE),
                WeekendScreenState(QUALIFYING, isSelected = navItem == QUALIFYING),
                WeekendScreenState(RACE, isSelected = navItem == RACE)
            )
        }
        season in 2021..2022 -> {
            return listOf(
                WeekendScreenState(SCHEDULE, isSelected = navItem == SCHEDULE),
                WeekendScreenState(QUALIFYING, isSelected = navItem == QUALIFYING),
                WeekendScreenState(SPRINT, isSelected = navItem == SPRINT).takeIf { hasSprintRace },
                WeekendScreenState(RACE, isSelected = navItem == RACE)
            ).filterNotNull()
        }
        season == 2023 -> {
            return listOf(
                WeekendScreenState(SCHEDULE, isSelected = navItem == SCHEDULE),
                WeekendScreenState(QUALIFYING, isSelected = navItem == QUALIFYING),
                WeekendScreenState(SPRINT_QUALIFYING, isSelected = navItem == SPRINT_QUALIFYING).takeIf { hasSprintQualifying },
                WeekendScreenState(SPRINT, isSelected = navItem == SPRINT).takeIf { hasSprintRace },
                WeekendScreenState(RACE, isSelected = navItem == RACE)
            ).filterNotNull()
        }
        else -> {
            return listOf(
                WeekendScreenState(SCHEDULE, isSelected = navItem == SCHEDULE),
                WeekendScreenState(SPRINT_QUALIFYING, isSelected = navItem == SPRINT_QUALIFYING).takeIf { hasSprintQualifying },
                WeekendScreenState(SPRINT, isSelected = navItem == SPRINT).takeIf { hasSprintRace },
                WeekendScreenState(QUALIFYING, isSelected = navItem == QUALIFYING),
                WeekendScreenState(RACE, isSelected = navItem == RACE)
            ).filterNotNull()
        }
    }
}