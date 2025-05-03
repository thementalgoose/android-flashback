package tmg.flashback.weekend.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.weekend.presentation.WeekendNavItem
import tmg.flashback.weekend.presentation.WeekendNavItem.SCHEDULE
import tmg.flashback.weekend.presentation.WeekendScreenState

internal class WeekendEventOrderTest {

    @ParameterizedTest
    @CsvSource(
        "2020,SCHEDULE|QUALIFYING|RACE",
        "2021,SCHEDULE|QUALIFYING|RACE",
        "2022,SCHEDULE|QUALIFYING|RACE",
        "2023,SCHEDULE|QUALIFYING|RACE",
        "2024,SCHEDULE|QUALIFYING|RACE",
        "2025,SCHEDULE|QUALIFYING|RACE"
    )
    fun `expected year returns expected schedule`(season: Int, orderString: String) {
        val expectedList = orderString.split("|")
            .map { WeekendNavItem.valueOf(it) }
            .map {
                WeekendScreenState(it, it == SCHEDULE)
            }

        val resultSchedule = getWeekendEventOrder(
            navItem = SCHEDULE,
            hasSprintQualifying = false,
            hasSprintRace = false,
            season = season
        )

        assertEquals(expectedList, resultSchedule)
    }

    @ParameterizedTest
    @CsvSource(
        "2021,false,false,SCHEDULE|QUALIFYING|RACE",
        "2021,false,true,SCHEDULE|QUALIFYING|SPRINT|RACE",
        "2022,false,false,SCHEDULE|QUALIFYING|RACE",
        "2022,false,true,SCHEDULE|QUALIFYING|SPRINT|RACE",
        "2023,false,false,SCHEDULE|QUALIFYING|RACE",
        "2023,true,false,SCHEDULE|QUALIFYING|SPRINT_QUALIFYING|RACE",
        "2023,true,true,SCHEDULE|QUALIFYING|SPRINT_QUALIFYING|SPRINT|RACE",
        "2024,false,false,SCHEDULE|QUALIFYING|RACE",
        "2024,true,false,SCHEDULE|SPRINT_QUALIFYING|QUALIFYING|RACE",
        "2024,false,true,SCHEDULE|SPRINT|QUALIFYING|RACE",
        "2024,true,true,SCHEDULE|SPRINT_QUALIFYING|SPRINT|QUALIFYING|RACE",
        "2025,false,false,SCHEDULE|QUALIFYING|RACE",
        "2025,true,false,SCHEDULE|SPRINT_QUALIFYING|QUALIFYING|RACE",
        "2025,false,true,SCHEDULE|SPRINT|QUALIFYING|RACE",
        "2025,true,true,SCHEDULE|SPRINT_QUALIFYING|SPRINT|QUALIFYING|RACE"
    )
    fun `expected year returns expected schedule with sprint data`(
        season: Int,
        hasSprintQualifying: Boolean,
        hasSprintRace: Boolean,
        orderString: String
    ) {
        val expectedList = orderString.split("|")
            .map { WeekendNavItem.valueOf(it) }
            .map {
                WeekendScreenState(it, it == SCHEDULE)
            }

        val resultSchedule = getWeekendEventOrder(
            navItem = SCHEDULE,
            hasSprintQualifying = hasSprintQualifying,
            hasSprintRace = hasSprintRace,
            season = season
        )

        assertEquals(expectedList, resultSchedule)
    }

}