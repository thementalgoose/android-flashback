package tmg.flashback.firebase.converters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.firebase.models.FAppLockout
import tmg.flashback.firebase.testutils.BaseTest

internal class AppLockoutConverterTest: BaseTest() {

    @Test
    fun `AppLockoutConverter show is false if found to be null`() {
        assertFalse(FAppLockout(show = null).convert().show)
    }

    @ParameterizedTest(name = "AppLockoutConverter version value {0} maps to {1}")
    @CsvSource(
        ",",
        "0,",
        "-1,",
        "3,3"
    )
    fun `AppLockoutConverter version is null set version null in model`(firebaseValue: Int?, expected: Int?) {
        assertEquals(FAppLockout(version = firebaseValue).convert().version, expected)
    }
}