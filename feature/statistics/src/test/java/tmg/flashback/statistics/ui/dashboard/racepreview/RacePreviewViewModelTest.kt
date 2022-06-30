package tmg.flashback.statistics.ui.dashboard.racepreview

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class RacePreviewViewModelTest: BaseTest() {

    private val mockRepository: OverviewRepository = mockk(relaxed = true)

    private lateinit var sut: RacePreviewViewModel

    private fun initSUT() {
        sut = RacePreviewViewModel(mockRepository)
    }

    @Test
    fun `overview emits null with not found season round`() = coroutineTest {

        every { mockRepository.getOverview(2020, 1) } returns flow { emit(null) }
        initSUT()
        sut.inputs.input(2020, 1)
        sut.overview.test {
            assertValue(null)
        }
    }

    @Test
    fun `overview emits model with found season round`() = coroutineTest {

        val inputModel = OverviewRace.model()
        every { mockRepository.getOverview(2020, 1) } returns flow { emit(inputModel) }
        initSUT()
        sut.inputs.input(2020, 1)
        sut.overview.test {
            assertValue(inputModel)
        }
    }
}