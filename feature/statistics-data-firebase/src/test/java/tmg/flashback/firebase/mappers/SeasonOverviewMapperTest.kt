package tmg.flashback.firebase.mappers

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeParseException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.CircuitSummary
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorStandings
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.DriverStandings
import tmg.flashback.data.models.stats.FastestLap
import tmg.flashback.data.models.stats.SeasonStanding
import tmg.flashback.data.models.stats.noTime
import tmg.flashback.data.utils.toLapTime
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeason
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonOverviewDriver
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuitLocation
import tmg.flashback.firebase.models.FSeasonOverviewRaceRaceFastestLap
import tmg.flashback.firebase.models.FSeasonStatistics
import tmg.flashback.firebase.models.FSeasonStatisticsPoints
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class SeasonOverviewMapperTest: BaseTest() {

    private val mockCrashController: CrashController = mockk(relaxed = true)

    private lateinit var sut: SeasonOverviewMapper

    private fun initSUT() {
        sut = SeasonOverviewMapper(mockCrashController)
    }

}