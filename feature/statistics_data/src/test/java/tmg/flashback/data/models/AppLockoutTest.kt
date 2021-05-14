package tmg.flashback.data.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class AppLockoutTest {

    @ParameterizedTest(name = "build config of {0}, if app lockout has version {1} then we are locked out {2}")
    @CsvSource(
        "1,true,1,true",
        "1,true,2,true",
        "2,true,1,false",
        "2,true,,true",
        "1,false,1,false",
        "1,false,2,false",
        "2,false,1,false",
        "2,false,,false"
    )
    fun `should lockout based on build config manager version`(buildConfig: Int, appLockoutShow: Boolean, appLockoutVersion: Int?, expectedResult: Boolean) {
        val model = AppLockout(
            show = appLockoutShow,
            title = "",
            message = "",
            linkText = null,
            link = null,
            appLockoutVersion
        )

        assertEquals(expectedResult, model.showLockout(buildConfig))
    }
}