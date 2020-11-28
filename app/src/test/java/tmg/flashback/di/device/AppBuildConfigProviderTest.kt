package tmg.flashback.di.device

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.BuildConfig

class BuildConfigProviderTest {

    lateinit var sut: BuildConfigProvider

    private fun initSUT(versionCode: Int) {
        sut = object : BuildConfigProvider {
            override val autoEnrolPushNotifications: Boolean
                get() = true
            override val versionCode: Int
                get() = versionCode
            override val versionName: String
                get() = "1.0"

        }
    }

    @ParameterizedTest
    @CsvSource(
            "10,11,false",
            "11,11,true",
            "12,11,true",
            ",11,false"
    )
    fun `BuildConfigProvider test versions lockout app properly`(suppliedVersion: Int?, appVersionCode: Int, expectedToPass: Boolean) {

        initSUT(appVersionCode)

        assertEquals(expectedToPass, sut.shouldLockoutBasedOnVersion(suppliedVersion))
    }
}