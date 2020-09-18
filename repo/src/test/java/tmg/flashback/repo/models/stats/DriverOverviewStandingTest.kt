package tmg.flashback.repo.models.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DriverOverviewStandingTest {

    @ParameterizedTest
    @CsvSource(
            "1,2,1",
            "2,2,0",
            "1,1,2"
    )
    fun `DriverOverviewStanding qualifying pole positions`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingPoles)
    }

    @ParameterizedTest
    @CsvSource(
            "3,4,1",
            "4,4,0",
            "3,2,2"
    )
    fun `DriverOverviewStanding qualifying top 3`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingTop3)
    }

    @ParameterizedTest
    @CsvSource(
            "3,2,1",
            "4,4,0",
            "1,2,2"
    )
    fun `DriverOverviewStanding qualifying front row`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingFrontRow)
    }

    @ParameterizedTest
    @CsvSource(
            "3,2,1",
            "4,4,2",
            "1,2,0"
    )
    fun `DriverOverviewStanding qualifying second row`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingSecondRow)
    }

    @ParameterizedTest
    @CsvSource(
            "0,3,1",
            "3,3,2",
            "0,0,0"
    )
    fun `DriverOverviewStanding finishes in points`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(points = r1),
                mockDriverRound12.copy(points = r2)
        ))
        assertEquals(expected, sut.finishesInPoints)
    }

    @ParameterizedTest
    @CsvSource(
            "2,8,1",
            "5,6,1",
            "1,3,2",
            "13,12,0"
    )
    fun `DriverOverviewStanding finishes in top 5`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(finished = r1),
                mockDriverRound12.copy(finished = r2)
        ))
        assertEquals(expected, sut.finishesInTop5)
    }

    @ParameterizedTest
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,2,0",
            "12,12,12,2"
    )
    fun `DriverOverviewStanding total finishes in`(r1: Int, r2: Int, finishesIn: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(finished = r1),
                mockDriverRound12.copy(finished = r2)
        ))
        assertEquals(expected, sut.totalFinishesIn(finishesIn))
    }

    @ParameterizedTest
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,15,2",
            "12,12,14,2"
    )
    fun `DriverOverviewStanding total finishes above`(r1: Int, r2: Int, finishesAbove: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(finished = r1),
                mockDriverRound12.copy(finished = r2)
        ))
        assertEquals(expected, sut.totalFinishesAbove(finishesAbove))
    }

    @ParameterizedTest
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,15,2",
            "12,12,14,2"
    )
    fun `DriverOverviewStanding total qualifying above`(r1: Int, r2: Int, qualifyingAbove: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.totalQualifyingAbove(qualifyingAbove))
    }

    @ParameterizedTest
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,2,0",
            "12,12,12,2"
    )
    fun `DriverOverviewStanding total qualifying in`(r1: Int, r2: Int, finishesIn: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.totalQualifyingIn(finishesIn))
    }
}