package tmg.flashback.di.device

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.core.managers.BuildConfigManager

internal class BuildConfigProviderTest {

    lateinit var sut: BuildConfigManager

    private fun initSUT(versionCode: Int) {
        sut = object : BuildConfigManager {
            override val applicationId: String
                get() = "tmg.flashback"
            override val versionCode: Int
                get() = versionCode
            override val versionName: String
                get() = "1.0"
        }
    }

    @ParameterizedTest(name = "Lockout version is {0}, app version is {1} =>  {2}")
    @CsvSource(
        "10,11,false",
        "11,11,true",
        "12,11,true",
        ",11,true"
    )
    fun `test versions lockout app properly`(suppliedVersion: Int?, appVersionCode: Int, expectedToPass: Boolean) {

        initSUT(appVersionCode)

        assertEquals(expectedToPass, sut.shouldLockoutBasedOnVersion(suppliedVersion))
    }

    @ParameterizedTest(name = "Version supplied as {0} which is should lockout for")
    @CsvSource(
        "0,11",
        "-1,11",
        ",11"
    )
    fun `test versions when version are invalid`(suppliedVersion: Int?, appVersionCode: Int) {

        initSUT(appVersionCode)

        assertTrue(sut.shouldLockoutBasedOnVersion(suppliedVersion))
    }
}