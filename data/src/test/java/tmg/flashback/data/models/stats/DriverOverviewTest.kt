package tmg.flashback.data.models.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DriverOverviewTest {

    @ParameterizedTest(name = "Expected {3} when SeasonA=[{0},inProgress=true] and SeasonB=[{1},inProgress={2}]")
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

    @ParameterizedTest(name = "Expected {3} when SeasonA=[{0},inProgress=true] and SeasonB=[{1},inProgress={2}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
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

    @ParameterizedTest(name = "Expected {5} when SeasonA=[{0},{1}] and SeasonB=[{2},{3}] finishes in {4}")
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

    @ParameterizedTest(name = "Expected {5} when SeasonA=[{0},{1}] and SeasonB=[{2},{3}] finishes above {4}")
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

    @ParameterizedTest(name = "Expected {5} when SeasonA=[{0},{1}] and SeasonB=[{2},{3}] qualifying above {4}")
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

    @ParameterizedTest(name = "Expected {5} when SeasonA=[{0},{1}] and SeasonB=[{2},{3}] qualifying in {4}")
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

    @ParameterizedTest(name = "Expected {2} when SeasonA=[{0}] and SeasonB=[{1}]")
    @CsvSource(
            "3,1,4",
            "1,2,3",
            "4,5,9",
            "1,1,2"
    )
    fun `DriverOverview race starts`(s1: Int, s2: Int, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(races = s1),
                mockDriverStanding1.copy(races = s2)
        ))
        assertEquals(expected, sut.raceStarts)
    }

    @ParameterizedTest(name = "Expected {4} when SeasonA=[{0},{1}] and SeasonB=[{2},{3}]")
    @CsvSource(
            "Finished,Finished,Finished,Finished,4",
            "+1 Lap,Finished,+1 Lap,Finished,4",
            "Engine,Finished,Finished,Finished,3",
            "Finished,Finished,Retired,Retired,2",
            "+2 Laps,+5 Laps,Finished,+3 Laps,4",
            "Retired,Engine,Cooling,Retired,0"
    )
    fun `DriverOverview race finishes`(s1r1: String, s1r2: String, s2r1: String, s2r2: String, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(raceOverview = listOf(
                        mockDriverRound11.copy(status = s1r1),
                        mockDriverRound12.copy(status = s1r2)
                )),
                mockDriverStanding2.copy(raceOverview = listOf(
                        mockDriverRound21.copy(status = s2r1),
                        mockDriverRound22.copy(status = s2r2)
                ))
        ))
        assertEquals(expected, sut.raceFinishes)
    }

    @ParameterizedTest(name = "Expected {4} when SeasonA=[{0},{1}] and SeasonB=[{2},{3}]")
    @CsvSource(
            "Finished,Finished,Finished,Finished,0",
            "+1 Lap,Finished,+1 Lap,Finished,0",
            "Engine,Finished,Finished,Finished,1",
            "Finished,Finished,Retired,Retired,2",
            "+2 Laps,+5 Laps,Finished,+3 Laps,0",
            "Retired,Engine,Cooling,Retired,4"
    )
    fun `DriverOverview race retirements`(s1r1: String, s1r2: String, s2r1: String, s2r2: String, expected: Int) {
        val sut = mockDriverOverview.copy(standings = listOf(
                mockDriverStanding1.copy(raceOverview = listOf(
                        mockDriverRound11.copy(status = s1r1),
                        mockDriverRound12.copy(status = s1r2)
                )),
                mockDriverStanding2.copy(raceOverview = listOf(
                        mockDriverRound21.copy(status = s2r1),
                        mockDriverRound22.copy(status = s2r2)
                ))
        ))
        assertEquals(expected, sut.raceRetirements)
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