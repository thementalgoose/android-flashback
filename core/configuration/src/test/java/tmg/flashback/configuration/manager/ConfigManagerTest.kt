package tmg.flashback.configuration.manager

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.crash_reporting.controllers.CrashController

internal class ConfigManagerTest {

    private val mockConfigService: RemoteConfigService = mockk(relaxed = true)
    private val mockCrashController: CrashController = mockk(relaxed = true)

    private lateinit var underTest: ConfigManager

    private fun initUnderTest() {
        underTest = ConfigManager(mockConfigService, mockCrashController)
    }

    @Test
    fun `get boolean calls service`() {
        every { mockConfigService.getBoolean(any()) } returns true
        initUnderTest()
        assertTrue(underTest.getBoolean("key"))
        verify {
            mockConfigService.getBoolean("key")
        }
    }

    @Test
    fun `get string calls service`() {
        every { mockConfigService.getString(any()) } returns "hey"
        initUnderTest()
        assertEquals("hey", underTest.getString("key"))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Test
    fun `get empty string calls service and returns null`() {
        every { mockConfigService.getString(any()) } returns ""
        initUnderTest()
        assertNull(underTest.getString("key"))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Serializable
    data class TestModel(
        val test: String
    )

    @Test
    fun `get json calls service`() {
        every { mockConfigService.getString(any()) } returns "{\"test\":\"hey\"}"
        initUnderTest()
        assertEquals(TestModel("hey"), underTest.getJson("key", TestModel.serializer()))
        verify {
            mockConfigService.getString("key")
        }
        verify(exactly = 0) {
            mockCrashController.logException(any(), any())
        }
    }

    @Test
    fun `get json calls service with misaligned keys`() {
        every { mockConfigService.getString(any()) } returns "{'tester':'hey'}"
        initUnderTest()
        assertNull(underTest.getJson("key", TestModel.serializer()))
        verify {
            mockConfigService.getString("key")
            mockCrashController.logException(any(), any())
        }
    }

    @Test
    fun `get json calls service with misaligned values`() {
        every { mockConfigService.getString(any()) } returns "{'test':3}"
        initUnderTest()
        assertNull(underTest.getJson("key", TestModel.serializer()))
        verify {
            mockConfigService.getString("key")
            mockCrashController.logException(any(), any())
        }
    }

    @Test
    fun `get json calls service with invalid json`() {
        every { mockConfigService.getString(any()) } returns "true"
        initUnderTest()
        assertNull(underTest.getJson("key", TestModel.serializer()))
        verify {
            mockConfigService.getString("key")
        }
    }

    @Test
    fun `get json calls service with null`() {
        every { mockConfigService.getString(any()) } returns ""
        initUnderTest()
        assertNull(underTest.getJson("key", TestModel.serializer()))
        verify {
            mockConfigService.getString("key")
        }
    }
}