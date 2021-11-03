package tmg.flashback.data.models.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DriverWithEmbeddedConstructorHistoryStandingTest {

    @ParameterizedTest(name = "Expected {2} when rounds [{0}, {1}]")
    @CsvSource(
            "1,2,1",
            "2,2,0",
            "1,1,2"
    )
    fun `qualifying pole positions`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingPoles)
    }

    @ParameterizedTest(name = "Expected {2} when rounds [{0}, {1}]")
    @CsvSource(
            "3,4,1",
            "4,4,0",
            "3,2,2"
    )
    fun `qualifying top 3`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingTop3)
    }

    @ParameterizedTest(name = "Expected {2} when rounds [{0}, {1}]")
    @CsvSource(
            "3,2,1",
            "4,4,0",
            "1,2,2"
    )
    fun `qualifying front row`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingFrontRow)
    }

    @ParameterizedTest(name = "Expected {2} when rounds [{0}, {1}]")
    @CsvSource(
            "3,2,1",
            "4,4,2",
            "1,2,0"
    )
    fun `qualifying second row`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.qualifyingSecondRow)
    }

    @ParameterizedTest(name = "Expected {2} when rounds [{0}, {1}]")
    @CsvSource(
            "0.0,3.0,1",
            "3.0,3.0,2",
            "0.0,0.0,0"
    )
    fun `finishes in points`(r1: Double, r2: Double, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(points = r1),
                mockDriverRound12.copy(points = r2)
        ))
        assertEquals(expected, sut.finishesInPoints)
    }

    @ParameterizedTest(name = "Expected {2} when rounds [{0}, {1}]")
    @CsvSource(
            "2,8,1",
            "5,6,1",
            "1,3,2",
            "13,12,0"
    )
    fun `finishes in top 5`(r1: Int, r2: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(finished = r1),
                mockDriverRound12.copy(finished = r2)
        ))
        assertEquals(expected, sut.finishesInTop5)
    }

    @ParameterizedTest(name = "Expected {3} when rounds [{0}, {1}] and finishes in {2}")
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,2,0",
            "12,12,12,2"
    )
    fun `total finishes in`(r1: Int, r2: Int, finishesIn: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(finished = r1),
                mockDriverRound12.copy(finished = r2)
        ))
        assertEquals(expected, sut.totalFinishesIn(finishesIn))
    }

    @ParameterizedTest(name = "Expected {3} when rounds [{0}, {1}] and finishes above {2}")
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,15,2",
            "12,12,14,2"
    )
    fun `total finishes above`(r1: Int, r2: Int, finishesAbove: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(finished = r1),
                mockDriverRound12.copy(finished = r2)
        ))
        assertEquals(expected, sut.totalFinishesAbove(finishesAbove))
    }

    @ParameterizedTest(name = "Expected {3} when rounds [{0}, {1}] and qualifying above {2}")
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,15,2",
            "12,12,14,2"
    )
    fun `total qualifying above`(r1: Int, r2: Int, qualifyingAbove: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.totalQualifyingAbove(qualifyingAbove))
    }

    @ParameterizedTest(name = "Expected {3} when rounds [{0}, {1}] and qualifying in {2}")
    @CsvSource(
            "2,8,1,0",
            "5,6,5,1",
            "3,3,3,2",
            "13,12,2,0",
            "12,12,12,2"
    )
    fun `total qualifying in`(r1: Int, r2: Int, qualifyingIn: Int, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(qualified = r1),
                mockDriverRound12.copy(qualified = r2)
        ))
        assertEquals(expected, sut.totalQualifyingIn(qualifyingIn))
    }

    @ParameterizedTest(name = "Expected {0} when rounds [{0}, {1}]")
    @CsvSource(
            "Finished,Finished,2",
            "+1 Lap,Finished,2",
            "Engine,Finished,1",
            "Finished,Finished,2",
            "+2 Laps,+5 Laps,2",
            "Retired,Engine,0"
    )
    fun `race finishes`(r1: String, r2: String, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(status = r1),
                mockDriverRound12.copy(status = r2)
        ))
        assertEquals(expected, sut.raceFinishes)
    }

    @ParameterizedTest(name = "Expected {0} when rounds [{0}, {1}]")
    @CsvSource(
            "Finished,Finished,0",
            "+1 Lap,Finished,0",
            "Engine,Finished,1",
            "Retired,Engine,2"
    )
    fun `race retirements`(r1: String, r2: String, expected: Int) {
        val sut = mockDriverStanding1.copy(raceOverview = listOf(
                mockDriverRound11.copy(status = r1),
                mockDriverRound12.copy(status = r2)
        ))
        assertEquals(expected, sut.raceRetirements)
    }
}