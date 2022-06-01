package tmg.flashback.stats.ui.weekend.qualifying

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.repo.RaceRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import java.time.Year

internal class QualifyingViewModelTest: BaseTest() {

    private val mockRaceRepository: RaceRepository = mockk(relaxed = true)

    private lateinit var underTest: QualifyingViewModel

    private fun initUnderTest() {
        underTest = QualifyingViewModel(
            raceRepository = mockRaceRepository,
            ioDispatcher = coroutineScope.testDispatcher
        )
    }

    @Test
    fun `loading view with no race results in same year shows not available yet`() {
        val currentSeason = Year.now().value
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                QualifyingModel.NotAvailableYet
            ))
        }
    }

    @Test
    fun `loading view with no race results in different year shows not available`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(null) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                QualifyingModel.NotAvailable
            ))
        }
    }

    @Test
    fun `loading view with list of results for q1q2q3`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model()) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                QualifyingModel.Q1Q2Q3.model()
            ))
        }
    }


    @Test
    fun `loading view with list of results for q1q2`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model(
            qualifying = listOf(
                RaceQualifyingRound.model(label = RaceQualifyingType.Q1, results = listOf(
                    RaceQualifyingResult.model(lapTime = LapTime.model(0,1,2,1))
                )),
                RaceQualifyingRound.model(label = RaceQualifyingType.Q2, results = listOf(
                    RaceQualifyingResult.model(lapTime = LapTime.model(0,1,2,2))
                ))
            )
        )) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                QualifyingModel.Q1Q2.model()
            ))
        }
    }


    @Test
    fun `loading view with list of results for q1`() {
        val currentSeason = 2020
        every { mockRaceRepository.getRace(currentSeason, 1) } returns flow { emit(Race.model(
            qualifying = listOf(
                RaceQualifyingRound.model(label = RaceQualifyingType.Q1, results = listOf(
                    RaceQualifyingResult.model(lapTime = LapTime.model(0,1,2,1))
                ))
            )
        )) }

        initUnderTest()
        underTest.load(currentSeason, 1)

        underTest.outputs.list.test {
            assertValue(listOf(
                QualifyingModel.Q1.model()
            ))
        }
    }
}