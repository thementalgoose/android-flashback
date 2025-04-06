package tmg.flashback.crashlytics.usecases

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.crashlytics.services.FirebaseCrashService

internal class AddCustomKeyUseCaseTest {

    private val mockFirebaseCrashService: FirebaseCrashService = mockk(relaxed = true)

    private lateinit var underTest: AddCustomKeyUseCase

    private fun initUnderTest() {
        underTest = AddCustomKeyUseCase(
            firebaseCrashService = mockFirebaseCrashService
        )
    }

    @Test
    fun `setting string value calls service`() {
        initUnderTest()
        underTest.invoke(FirebaseKey.AppFirstOpen, "value")

        verify {
            mockFirebaseCrashService.setCustomKey(FirebaseKey.AppFirstOpen, "value")
        }
    }

    @Test
    fun `setting boolean value calls service`() {
        initUnderTest()
        underTest.invoke(FirebaseKey.AppFirstOpen, false)

        verify {
            mockFirebaseCrashService.setCustomKey(FirebaseKey.AppFirstOpen, false)
        }
    }
}