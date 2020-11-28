package tmg.flashback.repo.models.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DriverOverviewTest {

    // TODO: Implement all the tests for these classes

    @ParameterizedTest
    @CsvSource(
            "0,1,false,1",
            "1,1,false,2",
            "1,3,false,1",
            "4,2,false,0",
            "21,1,false,1",
            "21,1,true,0"
    )
    fun `DriverOverview championship wins`(s1: Int, s2: Int, s2InProgress: Boolean, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(championshipStanding = s1),
                mockDriverStanding2.copy(championshipStanding = s2, isInProgress = s2InProgress)
        ))
        assertEquals(expected, sut.championshipWins)
    }

    @ParameterizedTest
    @CsvSource(
            "0,1,false,1",
            "1,1,false,1",
            "1,3,false,1",
            "1,3,true,1",
            "4,2,true,4",
            "4,2,false,2",
            "21,1,false,1",
            "21,1,true,21"
    )
    fun `DriverOverview career best championship`(s1: Int, s2: Int, s2InProgress: Boolean, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(championshipStanding = s1),
                mockDriverStanding2.copy(championshipStanding = s2, isInProgress = s2InProgress)
        ))
        assertEquals(expected, sut.careerBestChampionship)
    }

    @ParameterizedTest
    @CsvSource(
            "true,false,true",
            "true,true,true",
            "false,true,true",
            "false,false,false"
    )
    fun `DriverOverview has championship currently in progress`(s1: Boolean, s2: Boolean, expected: Boolean) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(isInProgress = s1),
                mockDriverStanding2.copy(isInProgress = s2)
        ))
        assertEquals(expected, sut.hasChampionshipCurrentlyInProgress)
    }

    @ParameterizedTest
    @CsvSource(
            "3,1,4",
            "1,2,3",
            "4,5,9",
            "0,0,0",
            "0,3,3"
    )
    fun `DriverOverview career wins`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(wins = s1),
                mockDriverStanding2.copy(wins = s2)
        ))
        assertEquals(expected, sut.careerWins)
    }

    @ParameterizedTest
    @CsvSource(
            "3,1,4",
            "1,2,3",
            "4,5,9",
            "0,0,0",
            "0,3,3"
    )
    fun `DriverOverview career podiums`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(podiums = s1),
                mockDriverStanding2.copy(podiums = s2)
        ))
        assertEquals(expected, sut.careerPodiums)
    }

    @ParameterizedTest
    @CsvSource(
            "3,1,4",
            "1,2,3",
            "4,5,9",
            "0,0,0",
            "0,3,3"
    )
    fun `DriverOverview career points`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(points = s1),
                mockDriverStanding2.copy(points = s2)
        ))
        assertEquals(expected, sut.careerPoints)
    }

    @ParameterizedTest
    @CsvSource(
            "3,1,4",
            "1,2,3",
            "4,5,9",
            "0,0,0",
            "0,3,3"
    )
    fun `DriverOverview career races`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(races = s1),
                mockDriverStanding2.copy(races = s2)
        ))
        assertEquals(expected, sut.careerRaces)
    }

    @ParameterizedTest
    @CsvSource(
            "3,1,1",
            "1,2,1",
            "4,5,4",
            "1,1,1"
    )
    fun `DriverOverview career best finish`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(bestFinish = s1),
                mockDriverStanding2.copy(bestFinish = s2)
        ))
        assertEquals(expected, sut.careerBestFinish)
    }

    @ParameterizedTest
    @CsvSource(
            "3,1,1",
            "1,2,1",
            "4,5,4",
            "1,1,1"
    )
    fun `DriverOverview career best qualifying`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(bestQualifying = s1),
                mockDriverStanding2.copy(bestQualifying = s2)
        ))
        assertEquals(expected, sut.careerBestQualifying)
    }

    @ParameterizedTest
    @CsvSource(
            "3,1,1",
            "1,2,1",
            "4,5,4",
            "1,1,1"
    )
    fun `DriverOverview constructor standing`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(championshipStanding = s1),
                mockDriverStanding2.copy(championshipStanding = s2)
        ))
        assertEquals(expected, sut.careerConstructorStanding)
    }

    @ParameterizedTest
    @CsvSource(
            "0,1,-1,1,3,0",
            "1,2,3,4,1,1",
            "3,1,4,1,2,0",
            "4,5,6,7,3,0",
            "1,1,1,1,1,4"
    )
    fun `DriverOverview total finishes in`(s1r1: Int, s1r2: Int, s2r1: Int, s2r2: Int, finishesIn: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(raceOverview = listOf(
                        mockDriverRound11.copy(finished = s1r1),
                        mockDriverRound11.copy(finished = s1r2)
                )),
                mockDriverStanding2.copy(raceOverview = listOf(
                        mockDriverRound11.copy(finished = s2r1),
                        mockDriverRound11.copy(finished = s2r2)
                ))
        ))
        assertEquals(expected, sut.totalFinishesIn(finishesIn))
    }

    @ParameterizedTest
    @CsvSource(
            "0,1,-1,1,3,2",
            "1,2,3,4,1,1",
            "3,1,4,1,2,2",
            "4,5,6,7,3,0",
            "1,1,1,1,1,4",
            "9,4,6,3,6,3"
    )
    fun `DriverOverview total finishes above`(s1r1: Int, s1r2: Int, s2r1: Int, s2r2: Int, finishesIn: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(raceOverview = listOf(
                        mockDriverRound11.copy(finished = s1r1),
                        mockDriverRound11.copy(finished = s1r2)
                )),
                mockDriverStanding2.copy(raceOverview = listOf(
                        mockDriverRound11.copy(finished = s2r1),
                        mockDriverRound11.copy(finished = s2r2)
                ))
        ))
        assertEquals(expected, sut.totalFinishesAbove(finishesIn))
    }

    @ParameterizedTest
    @CsvSource(
            "0,1,-1,0,2,1",
            "1,2,3,4,1,1",
            "3,1,4,1,2,2",
            "4,5,6,7,3,0",
            "1,1,1,1,1,4",
            "9,4,6,3,6,3"
    )
    fun `DriverOverview total qualifying above`(s1r1: Int, s1r2: Int, s2r1: Int, s2r2: Int, finishesIn: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(raceOverview = listOf(
                        mockDriverRound11.copy(qualified = s1r1),
                        mockDriverRound11.copy(qualified = s1r2)
                )),
                mockDriverStanding2.copy(raceOverview = listOf(
                        mockDriverRound11.copy(qualified = s2r1),
                        mockDriverRound11.copy(qualified = s2r2)
                ))
        ))
        assertEquals(expected, sut.totalQualifyingAbove(finishesIn))
    }

    @ParameterizedTest
    @CsvSource(
            "0,1,-1,1,3,0",
            "1,2,3,4,1,1",
            "3,1,4,1,2,0",
            "4,5,6,7,3,0",
            "1,1,1,1,1,4"
    )
    fun `DriverOverview total qualifying in`(s1r1: Int, s1r2: Int, s2r1: Int, s2r2: Int, finishesIn: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(raceOverview = listOf(
                        mockDriverRound11.copy(qualified = s1r1),
                        mockDriverRound11.copy(qualified = s1r2)
                )),
                mockDriverStanding2.copy(raceOverview = listOf(
                        mockDriverRound11.copy(qualified = s2r1),
                        mockDriverRound11.copy(qualified = s2r2)
                ))
        ))
        assertEquals(expected, sut.totalQualifyingIn(finishesIn))
    }

    @Test
    fun `DriverOverview is world champion for`() {
        val valid = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(season = 2019, championshipStanding = 1, isInProgress = false)
        ))
        val inProgress = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(season = 2019, championshipStanding = 1, isInProgress = true)
        ))
        val isNotChampion = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(season = 2019, championshipStanding = 2, isInProgress = false)
        ))
        assertEquals(true, valid.isWorldChampionFor(2019))
        assertEquals(false, inProgress.isWorldChampionFor(2019))
        assertEquals(false, isNotChampion.isWorldChampionFor(2019))
        assertEquals(false, valid.isWorldChampionFor(2020))
    }
}