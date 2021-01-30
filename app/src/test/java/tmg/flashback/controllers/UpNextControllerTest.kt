package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.models.Timestamp
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule
import tmg.flashback.testutils.BaseTest

internal class UpNextControllerTest: BaseTest() {

    private var mockRemoteConfigRepository: RemoteConfigRepository = mockk(relaxed = true)

    private lateinit var sut: UpNextController

    private fun initSUT() {
        sut = UpNextController(mockRemoteConfigRepository)
    }

    @Test
    fun `UpNextController no next race if remote config is empty`() {
        every { mockRemoteConfigRepository.upNext } returns emptyList()
        initSUT()

        assertNull(sut.getNextEvent())
    }

    @Test
    fun `UpNextController no next race if up next schedule is all in past `() {
        val list = listOf(
                generateUpNextItem(-1),
                generateUpNextItem(-2),
                generateUpNextItem(-4)
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertNull(sut.getNextEvent())
    }

    @Test
    fun `UpNextController next race returned if one found in future`() {
        val list = listOf(
                generateUpNextItem(1),
                generateUpNextItem(2),
                generateUpNextItem(3)
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(generateUpNextItem(1), (sut.getNextEvent()))
    }

    @Test
    fun `UpNextController next race returned if one found today`() {
        val list = listOf(
                generateUpNextItem(0),
                generateUpNextItem(2),
                generateUpNextItem(3)
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(generateUpNextItem(0), (sut.getNextEvent()))
    }

    @Test
    fun `UpNextController next race returns correct one mid list including today`() {
        val list = listOf(
                generateUpNextItem(-2),
                generateUpNextItem(0),
                generateUpNextItem(1),
                generateUpNextItem(4)
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(generateUpNextItem(0), (sut.getNextEvent()))
    }

    @Test
    fun `UpNextController next race returns correct one mid list not today`() {
        val list = listOf(
                generateUpNextItem(-2),
                generateUpNextItem(1),
                generateUpNextItem(4)
        )
        every { mockRemoteConfigRepository.upNext } returns list
        initSUT()

        assertEquals(generateUpNextItem(1), (sut.getNextEvent()))
    }

    private fun generateUpNextItem(daysDeltaFromNow: Long): UpNextSchedule {
        return UpNextSchedule(
                season = 1,
                round = 3,
                name = "Up next item $daysDeltaFromNow",
                timestamp = Timestamp(LocalDate.now().plusDays(daysDeltaFromNow)),
                flag = null,
                circuitId = null,
                circuitName = null
        )
    }

}