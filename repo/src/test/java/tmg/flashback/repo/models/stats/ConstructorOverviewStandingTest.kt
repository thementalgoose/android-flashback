package tmg.flashback.repo.models.stats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ConstructorOverviewStandingTest {

    @ParameterizedTest
    @CsvSource(
            "-1,9,9",
            "1,4,1",
            "2,2,2",
            "0,2,2"
    )
    fun `ConstructorOverviewStanding best finish`(driver1: Int, driver2: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        bestFinish = driver1
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        bestFinish = driver2
                )
        ))
        assertEquals(expected, sut.bestFinish)
    }

    @ParameterizedTest
    @CsvSource(
            "-1,9,9",
            "1,9,1",
            "2,2,2",
            "0,2,2"
    )
    fun `ConstructorOverviewStanding best qualifying`(driver1: Int, driver2: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        bestQualifying = driver1
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        bestQualifying = driver2
                )
        ))
        assertEquals(expected, sut.bestQualifying)
    }

    @ParameterizedTest
    @CsvSource(
            "-1,9,9",
            "1,9,10",
            "2,2,4",
            "0,2,2",
            "0,0,0",
            "-1,0,0"
    )
    fun `ConstructorOverviewStanding qualifying on pole`(driver1: Int, driver2: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        qualifyingP1 = driver1
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        qualifyingP1 = driver2
                )
        ))
        assertEquals(expected, sut.qualifyingPole)
    }

    @ParameterizedTest
    @CsvSource(
            "-1,3,4,9,16",
            "1,1,2,9,13",
            "2,0,0,0,2",
            "0,0,0,0,0",
            "-1,-1,-1,-1,0"
    )
    fun `ConstructorOverviewStanding qualifying on front row`(driver1Pos1: Int, driver1Pos2: Int, driver2Pos1: Int, driver2Pos2: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        qualifyingP1 = driver1Pos1,
                        qualifyingP2 = driver1Pos2
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        qualifyingP1 = driver2Pos1,
                        qualifyingP2 = driver2Pos2
                )
        ))
        assertEquals(expected, sut.qualifyingFrontRow)
    }

    @ParameterizedTest
    @CsvSource(
            "-1,9,9",
            "1,9,10",
            "2,2,4",
            "0,2,2",
            "0,0,0",
            "-1,0,0"
    )
    fun `ConstructorOverviewStanding driver points`(driver1: Int, driver2: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        points = driver1
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        points = driver2
                )
        ))
        assertEquals(expected, sut.driverPoints)
    }

    @ParameterizedTest
    @CsvSource(
            "-1,9,9",
            "1,9,10",
            "2,2,4",
            "0,2,2",
            "0,0,0",
            "-1,0,0"
    )
    fun `ConstructorOverviewStanding finishes in points`(driver1: Int, driver2: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        finishesInPoints = driver1
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        finishesInPoints = driver2
                )
        ))
        assertEquals(expected, sut.finishInPoints)
    }

    @ParameterizedTest
    @CsvSource(
            "-1,9,9",
            "1,9,10",
            "2,2,4",
            "0,2,2",
            "0,0,0",
            "-1,0,0"
    )
    fun `ConstructorOverviewStanding wins`(driver1: Int, driver2: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        finishesInP1 = driver1
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        finishesInP1 = driver2
                )
        ))
        assertEquals(expected, sut.wins)
    }

    @ParameterizedTest
    @CsvSource(
            "1,2,3,4,5,6,21",
            "1,1,1,1,1,9,14",
            "0,0,1,0,0,0,1",
            "0,1,0,0,0,1,2",
            "0,0,0,0,0,0,0",
            "-1,0,0,0,-1,0,0"
    )
    fun `ConstructorOverviewStanding podiums`(driver1P1: Int, driver1P2: Int, driver1P3: Int, driver2P1: Int, driver2P2: Int, driver2P3: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        finishesInP1 = driver1P1,
                        finishesInP2 = driver1P2,
                        finishesInP3 = driver1P3
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        finishesInP1 = driver2P1,
                        finishesInP2 = driver2P2,
                        finishesInP3 = driver2P3
                )
        ))
        assertEquals(expected, sut.podiums)
    }

    @ParameterizedTest
    @CsvSource(
            "1,2,3,4,5,6,21",
            "1,1,1,1,1,9,14",
            "0,0,1,0,0,0,1",
            "0,1,0,0,0,1,2",
            "0,0,0,0,0,0,0",
            "-1,0,0,0,-1,0,0"
    )
    fun `ConstructorOverviewStanding qualified in top 3`(driver1P1: Int, driver1P2: Int, driver1P3: Int, driver2P1: Int, driver2P2: Int, driver2P3: Int, expected: Int) {
        val sut = mockConstructorOverviewStanding1.copy(drivers = mapOf(
                mockDriver1.id to mockConstructorOverviewStanding1Driver1.copy(
                        qualifyingP1 = driver1P1,
                        qualifyingP2 = driver1P2,
                        qualifyingP3 = driver1P3
                ),
                mockDriver2.id to mockConstructorOverviewStanding1Driver2.copy(
                        qualifyingP1 = driver2P1,
                        qualifyingP2 = driver2P2,
                        qualifyingP3 = driver2P3
                )
        ))
        assertEquals(expected, sut.qualifyingTop3)
    }
}