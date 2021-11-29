package tmg.flashback.configuration.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.services.RemoteConfigService

internal class ConfigManagerTest {

    private val mockConfigService: RemoteConfigService = mockk()

    private lateinit var sut: ConfigManager

    private fun initSUT() {
        sut = ConfigManager(mockConfigService)
    }

    @Test
    fun `get boolean calls service`() {
        every { mockConfigService.getBoolean(any()) } returns true
        initSUT()
        assertTrue(sut.getBoolean("key"))
        verify {
            mockConfigService.getBoolean("key")
        }
    }

    @Test
    fun `get string calls service`() {
        every { mockConfigService.getString(any()) } returns "hey"
        initSUT()
        assertEquals("hey", sut.getString("key"))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Test
    fun `get empty string calls service and returns null`() {
        every { mockConfigService.getString(any()) } returns ""
        initSUT()
        assertNull(sut.getString("key"))
        verify {
            mockConfigService.getString("key")
        }
    }

    data class TestModel(
        val test: String
    )
    @Test
    fun `get json calls service`() {
        every { mockConfigService.getString(any()) } returns "{'test':'hey'}"
        initSUT()
        assertEquals(TestModel("hey"), sut.getJson("key", TestModel::class.java))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Test
    fun `get json calls service with misaligned keys`() {
        every { mockConfigService.getString(any()) } returns "{'tester':'hey'}"
        initSUT()
        assertNull(sut.getJson("key", TestModel::class.java))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Test
    fun `get json calls service with misaligned values`() {
        every { mockConfigService.getString(any()) } returns "{'test':3}"
        initSUT()
        assertNull(sut.getJson("key", TestModel::class.java))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Test
    fun `get json calls service with invalid json`() {
        every { mockConfigService.getString(any()) } returns "true"
        initSUT()
        assertNull(sut.getJson("key", TestModel::class.java))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Test
    fun `get json calls service with null`() {
        every { mockConfigService.getString(any()) } returns ""
        initSUT()
        assertNull(sut.getJson("key", TestModel::class.java))
        verify {
            mockConfigService.getString("key")
        }
    }
}