package tmg.flashback.upnext.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.configuration.controllers.ConfigController
import tmg.flashback.upnext.repository.json.UpNextItemJson
import tmg.flashback.upnext.repository.json.UpNextJson
import tmg.flashback.upnext.repository.json.UpNextScheduleJson
import tmg.flashback.upnext.repository.model.UpNextSchedule

internal class UpNextRepositoryTest {

    private val mockConfigManager: ConfigController = mockk()

    private lateinit var sut: UpNextRepository

    private fun initSUT() {
        sut = UpNextRepository(mockConfigManager)
    }

    //region Up next list

    @Test
    fun `getting up next list returns empty list if manager returns null`() {
        every { mockConfigManager.getJson<UpNextJson>(keyUpNext) } returns null
        initSUT()
        assertEquals(emptyList<UpNextSchedule>(), sut.upNext)
        verify {
            mockConfigManager.getJson<UpNextJson>(keyUpNext)
        }
    }

    @Test
    fun `getting up next list returns empty list if model schedule is null`() {
        every { mockConfigManager.getJson<UpNextJson>(keyUpNext) } returns UpNextJson(schedule = null)
        initSUT()
        assertEquals(emptyList<UpNextSchedule>(), sut.upNext)
        verify {
            mockConfigManager.getJson<UpNextJson>(keyUpNext)
        }
    }

    @Test
    fun `getting up next list returns empty list if model schedule is empty`() {
        every { mockConfigManager.getJson<UpNextJson>(keyUpNext) } returns UpNextJson(schedule = emptyList())
        initSUT()
        assertEquals(emptyList<UpNextSchedule>(), sut.upNext)
        verify {
            mockConfigManager.getJson<UpNextJson>(keyUpNext)
        }
    }

    @Test
    fun `getting up next list returns valid list when json content`() {
        every { mockConfigManager.getJson<UpNextJson>(keyUpNext) } returns UpNextJson(schedule = listOf(UpNextScheduleJson(
                s = 1,
                r = 2,
                title = "3",
                subtitle = "4",
                dates = listOf(UpNextItemJson(type = "FP1", d = "2020-01-01", t = "13:00:00"))
        )))
        initSUT()
        assertEquals(1, sut.upNext.first().season)
        assertEquals(2, sut.upNext.first().round)
        assertEquals("3", sut.upNext.first().title)
        assertEquals("4", sut.upNext.first().subtitle)
        verify {
            mockConfigManager.getJson<UpNextJson>(keyUpNext)
        }
    }

    //endregion

    companion object {
        private const val keyUpNext: String = "up_next"
    }
}