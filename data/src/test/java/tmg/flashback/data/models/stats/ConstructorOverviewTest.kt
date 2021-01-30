package tmg.flashback.data.models.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ConstructorOverviewTest {

    @ParameterizedTest
    @CsvSource(
            "0,1,false,1",
            "1,1,false,2",
            "1,3,false,1",
            "4,2,false,0",
            "21,1,false,1",
            "21,1,true,0"
    )
    fun `ConstructorOverview championship wins`(s1: Int, s2: Int, s2InProgress: Boolean, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(championshipStanding = s1),
                mockConstructorOverviewStanding2.copy(championshipStanding = s2, isInProgress = s2InProgress)
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
    fun `ConstructorOverview career best championship`(s1: Int, s2: Int, s2InProgress: Boolean, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(championshipStanding = s1),
                mockConstructorOverviewStanding2.copy(championshipStanding = s2, isInProgress = s2InProgress)
        ))
        assertEquals(expected, sut.bestChampionship)
    }

    @ParameterizedTest
    @CsvSource(
            "true,false,true",
            "true,true,true",
            "false,true,true",
            "false,false,false"
    )
    fun `ConstructorOverview has championship currently in progress`(s1: Boolean, s2: Boolean, expected: Boolean) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(isInProgress = s1),
                mockConstructorOverviewStanding2.copy(isInProgress = s2)
        ))
        assertEquals(expected, sut.hasChampionshipCurrentlyInProgress)
    }

    @ParameterizedTest
    @CsvSource(
            "1,4,5",
            "0,2,2",
            "-1,0,0",
            "4,2,6"
    )
    fun `ConstructorOverview races`(s1: Int, s2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(races = s1),
                mockConstructorOverviewStanding2.copy(races = s2)
        ))
        assertEquals(expected, sut.races)
    }

    @ParameterizedTest
    @CsvSource(
            "1,4,5",
            "0,2,2",
            "-1,0,0",
            "4,2,6"
    )
    fun `ConstructorOverview points`(s1: Int, s2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(points = s1),
                mockConstructorOverviewStanding2.copy(points = s2)
        ))
        assertEquals(expected, sut.totalPoints)
    }

    @ParameterizedTest
    @CsvSource(
            "1,4,1,3,9",
            "0,0,0,0,0",
            "-1,0,0,0,0",
            "4,1,3,2,10"
    )
    fun `ConstructorOverview race entries`(s1Driver1: Int, s1Driver2: Int, s2Driver1: Int, s2Driver2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                                races = s1Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                                races = s1Driver2
                        )
                )),
                mockConstructorOverviewStanding2.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding2Driver1.copy(
                                races = s2Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding2Driver2.copy(
                                races = s2Driver2
                        )
                ))
        ))
        assertEquals(expected, sut.raceEntries)
    }

    @ParameterizedTest
    @CsvSource(
            "1,4,1,3,9",
            "0,0,0,0,0",
            "-1,0,0,0,0",
            "4,1,3,2,10"
    )
    fun `ConstructorOverview wins`(s1Driver1: Int, s1Driver2: Int, s2Driver1: Int, s2Driver2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                                finishesInP1 = s1Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                                finishesInP1 = s1Driver2
                        )
                )),
                mockConstructorOverviewStanding2.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding2Driver1.copy(
                                finishesInP1 = s2Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding2Driver2.copy(
                                finishesInP1 = s2Driver2
                        )
                ))
        ))
        assertEquals(expected, sut.totalWins)
    }


    @ParameterizedTest
    @CsvSource(
            "1,4,1,3,1",
            "0,0,0,0,-1",
            "-1,0,0,0,-1",
            "4,1,3,2,1",
            "4,8,3,6,3"
    )
    fun `ConstructorOverview best finish`(s1Driver1: Int, s1Driver2: Int, s2Driver1: Int, s2Driver2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                                bestFinish = s1Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                                bestFinish = s1Driver2
                        )
                )),
                mockConstructorOverviewStanding2.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding2Driver1.copy(
                                bestFinish = s2Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding2Driver2.copy(
                                bestFinish = s2Driver2
                        )
                ))
        ))
        assertEquals(expected, sut.bestFinish)
    }

    @ParameterizedTest
    @CsvSource(
            "1,4,1,3,1",
            "0,0,0,0,-1",
            "-1,0,0,0,-1",
            "4,1,3,2,1",
            "6,86,31,7,6"
    )
    fun `ConstructorOverview best qualifying`(s1Driver1: Int, s1Driver2: Int, s2Driver1: Int, s2Driver2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                                bestQualifying = s1Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                                bestQualifying = s1Driver2
                        )
                )),
                mockConstructorOverviewStanding2.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding2Driver1.copy(
                                bestQualifying = s2Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding2Driver2.copy(
                                bestQualifying = s2Driver2
                        )
                ))
        ))
        assertEquals(expected, sut.bestQualifying)
    }

    @ParameterizedTest
    @CsvSource(
            "1,4,1,3,9",
            "0,0,0,0,0",
            "-1,0,0,0,0",
            "4,1,3,2,10"
    )
    fun `ConstructorOverview total qualifying on pole`(s1Driver1: Int, s1Driver2: Int, s2Driver1: Int, s2Driver2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                                qualifyingP1 = s1Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                                qualifyingP1 = s1Driver2
                        )
                )),
                mockConstructorOverviewStanding2.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding2Driver1.copy(
                                qualifyingP1 = s2Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding2Driver2.copy(
                                qualifyingP1 = s2Driver2
                        )
                ))
        ))
        assertEquals(expected, sut.totalQualifyingPoles)
    }

    @ParameterizedTest
    @CsvSource(
            "1,4,1,3,9",
            "0,0,0,0,0",
            "-1,0,0,0,0",
            "4,1,3,2,10"
    )
    fun `ConstructorOverview finishes in the points`(s1Driver1: Int, s1Driver2: Int, s2Driver1: Int, s2Driver2: Int, expected: Int) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                                finishesInPoints = s1Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                                finishesInPoints = s1Driver2
                        )
                )),
                mockConstructorOverviewStanding2.copy(drivers = mapOf(
                        mockDriver1.id to mockConstructorOverviewStanding2Driver1.copy(
                                finishesInPoints = s2Driver1
                        ),
                        mockDriver2.id to mockConstructorOverviewStanding2Driver2.copy(
                                finishesInPoints = s2Driver2
                        )
                ))
        ))
        assertEquals(expected, sut.finishesInPoints)
    }

    @ParameterizedTest
    @CsvSource(
            "true,true,true",
            "true,false,true",
            "false,true,false",
            "false,false,false"
    )
    fun `ConstructorOverview isWorldChampionFor`(championForSeason1: Boolean, championForSeason2: Boolean, expectedChampionForSeason1: Boolean) {
        val sut = mockConstructorOverview.copy(standings = listOf(
                mockConstructorOverviewStanding1.copy(championshipStanding = if (championForSeason1) 1 else 2),
                mockConstructorOverviewStanding2.copy(championshipStanding = if (championForSeason2) 1 else 2))
        )
        assertEquals(expectedChampionForSeason1, sut.isWorldChampionFor(mockConstructorOverviewStanding1.season))
    }
}