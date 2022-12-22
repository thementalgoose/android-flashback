package tmg.flashback.device.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.managers.DeviceConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.utilities.enums.ScreenDensityState
import tmg.utilities.extensions.format
import tmg.utilities.models.DeviceStatus
import java.util.Locale

internal class GetDeviceInfoUseCaseTest {

    private val mockDeviceRepository: DeviceRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockDeviceConfigManager: DeviceConfigManager = mockk(relaxed = true)

    private lateinit var underTest: GetDeviceInfoUseCase

    private fun initUnderTest() {
        underTest = GetDeviceInfoUseCase(
            deviceRepository = mockDeviceRepository,
            buildConfigManager = mockBuildConfigManager,
            deviceConfigManager = mockDeviceConfigManager
        )
    }

    private val manufacturer = "MANUFACTURER"
    private val model = "MODEL"
    private val osVersion = "OS_VERSION"
    private val sdkVersion = 4
    private val language = "LANGUAGE"
    private val screenDensity = ScreenDensityState.xxxhdpi
    private val screenWidth = 1
    private val screenHeight = 2
    private val versionName = "VERSION_NAME"
    private val versionCode = 3
    private val deviceUdid = "DEVICE_UDID"
    private val installationId = "INSTALLATION_ID"
    private val installedOn = LocalDate.of(2022, 1, 1)
    private val deviceStatus: DeviceStatus = mockk(relaxed = true) {
        every { deviceManufacturer } returns this@GetDeviceInfoUseCaseTest.manufacturer
        every { deviceModel } returns this@GetDeviceInfoUseCaseTest.model
        every { osVersion } returns this@GetDeviceInfoUseCaseTest.osVersion
        every { sdkVersion } returns this@GetDeviceInfoUseCaseTest.sdkVersion
        every { language } returns this@GetDeviceInfoUseCaseTest.language
        every { screenDensity } returns this@GetDeviceInfoUseCaseTest.screenDensity
        every { screenWidth } returns this@GetDeviceInfoUseCaseTest.screenWidth
        every { screenHeight } returns this@GetDeviceInfoUseCaseTest.screenHeight
    }

    private fun mockDevice(status: DeviceStatus? = deviceStatus) {
        every { mockDeviceConfigManager.getDeviceStatus() } returns status
        every { mockBuildConfigManager.versionName } returns versionName
        every { mockBuildConfigManager.versionCode } returns versionCode
        every { mockDeviceRepository.deviceUdid } returns deviceUdid
        every { mockDeviceRepository.installationId } returns installationId
        every { mockDeviceRepository.appFirstOpened } returns installedOn
    }

    @Test
    fun `prints out expected device info`() {
        mockDevice()
        initUnderTest()

        val expected = """
            -------- DEVICE INFORMATION --------
            Manufacturer: $manufacturer
            Model: $model
            OS Version: $osVersion
            SDK Version: $sdkVersion
            Language: $language
            Screen density: $screenDensity
            Screen resolution: ${screenWidth}x${screenHeight}
            Version name: $versionName ($versionCode)
            Device ID: $deviceUdid
            Installation ID: $installationId
            Installed on: ${installedOn.format("dd MMM yyyy")}
        """.trimIndent()

        assertEquals(expected, underTest.run())
    }

    @Test
    fun `prints out expected device info with device status null`() {
        mockDevice(status = null)
        initUnderTest()

        val expected = """
            -------- DEVICE INFORMATION --------
            SDK Version: 0
            Language: ${Locale.getDefault().language}
            Version name: $versionName ($versionCode)
            Device ID: $deviceUdid
            Installation ID: $installationId
            Installed on: ${installedOn.format("dd MMM yyyy")}
        """.trimIndent()

        assertEquals(expected, underTest.run())
    }
}