package tmg.flashback.presentation.aboutthisapp

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.rss.usecases.AllSupportedSourcesUseCase

internal class AboutThisAppConfigProviderTest {

    private val mockContext: Context = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)
    private val mockDeviceRepository: DeviceRepository = mockk(relaxed = true)
    private val mockContactRepository: ContactRepository = mockk(relaxed = true)
    private val mockAllSupportedSourcesUseCase: AllSupportedSourcesUseCase = mockk(relaxed = true)

    private lateinit var underTest: AboutThisAppConfigProvider

    private fun initUnderTest() {
        underTest = AboutThisAppConfigProvider(
            context = mockContext,
            buildConfigManager = mockBuildConfigManager,
            deviceRepository = mockDeviceRepository,
            contactRepository = mockContactRepository,
            allSupportedSourcesUseCase = mockAllSupportedSourcesUseCase
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockBuildConfigManager.applicationId } returns "applicationId"
        every { mockBuildConfigManager.versionName } returns "versionName"
        every { mockDeviceRepository.deviceUdid } returns "deviceUdid"
        every { mockDeviceRepository.installationId } returns "installationId"
        every { mockContactRepository.contactEmail } returns "email"

        every { mockContext.getString(R.string.app_name) } returns "app_name"
        every { mockContext.getString(R.string.dependency_thank_you) } returns "header"
        every { mockContext.getString(R.string.about_additional) } returns "footer"
    }

    @Test
    fun `configuration contains fields`() {
        initUnderTest()
        val config = underTest.getConfig()

        assertEquals("applicationId", config.appPackageName)
        assertEquals("app_name", config.appName)
        assertEquals(R.mipmap.ic_launcher, config.imageRes)
        assertEquals("versionName", config.appVersion)
        assertEquals("header", config.header)
        assertEquals("footer", config.footnote)

        assertEquals("email", config.email)
        assertEquals("https://www.github.com/thementalgoose", config.github)

        assertEquals("deviceUdid\ninstallationId", config.debugInfo)

        assertEquals(17, config.dependencies.size)
    }
}