package tmg.flashback.crashlytics.usecases

import android.os.Build
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.PrivacyRepository

internal class InitialiseCrashReportingUseCaseTest {

    private val mockPrivacyRepository: PrivacyRepository = mockk(relaxed = true)
    private val mockFirebaseCrashService: FirebaseCrashService = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var underTest: InitialiseCrashReportingUseCase

    companion object {
        private const val IS_EMULATOR: Boolean = true
        private const val IS_DEBUG: Boolean = true
        private const val CRASH_REPORTING_ENABLED: Boolean = true
    }

    private fun initUnderTest() {
        underTest = InitialiseCrashReportingUseCase(
            privacyRepository = mockPrivacyRepository,
            firebaseCrashService = mockFirebaseCrashService,
            buildConfigManager = mockBuildConfigManager
        )

        every { mockPrivacyRepository.crashReporting } returns CRASH_REPORTING_ENABLED
        every { mockBuildConfigManager.isEmulator } returns IS_EMULATOR
        every { mockBuildConfigManager.isDebug } returns IS_DEBUG
        every { mockBuildConfigManager.brand } returns "BRAND"
        every { mockBuildConfigManager.hardware } returns "HARDWARE"
        every { mockBuildConfigManager.board } returns "BOARD"
        every { mockBuildConfigManager.fingerprint } returns "FINGERPRINT"
        every { mockBuildConfigManager.model } returns "MODEL"
        every { mockBuildConfigManager.manufacturer } returns "MANUFACTURER"
        every { mockBuildConfigManager.product } returns "PRODUCT"
        every { mockBuildConfigManager.device } returns "DEVICE"
    }

    @Test
    fun `initialise sends metrics to crash service with feature enabled`() {
        val deviceUuid = "deviceUuid"
        val keys = mapOf(FirebaseKey.AppFirstOpen to "X")

        initUnderTest()
        underTest.initialise(
            deviceUuid = deviceUuid,
            extraKeys = keys,
        )

        verify {
            mockFirebaseCrashService.setCrashlyticsCollectionEnabled(CRASH_REPORTING_ENABLED)
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Emulator, IS_EMULATOR)
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Debug, IS_DEBUG)
            mockFirebaseCrashService.setUserId(deviceUuid)

            mockFirebaseCrashService.setCustomKey(FirebaseKey.DeviceUuid, deviceUuid)
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Brand, "BRAND")
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Hardware, "HARDWARE")
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Board, "BOARD")
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Fingerprint, "FINGERPRINT")
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Model, "MODEL")
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Manufacturer, "MANUFACTURER")
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Product, "PRODUCT")
            mockFirebaseCrashService.setCustomKey(FirebaseKey.Device, "DEVICE")

            mockFirebaseCrashService.setCustomKey(FirebaseKey.AppFirstOpen, "X")
        }
    }
}