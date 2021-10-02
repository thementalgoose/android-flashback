package tmg.flashback.firebase.mappers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.data.models.AppLockout
import tmg.flashback.firebase.models.FAppLockout
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class AppLockoutMapperTest: BaseTest() {

    private lateinit var sut: AppLockoutMapper

    private fun initSUT() {
        sut = AppLockoutMapper()
    }

    @Test
    fun `AppLockout maps fields correctly`() {
        initSUT()

        val input = FAppLockout.model()
        val expected = AppLockout(
            show = false,
            title = "title",
            message = "message",
            linkText = "linkText",
            link = "link",
            version = 1
        )

        assertEquals(expected, sut.mapAppLockout(input))
    }

    @Test
    fun `AppLockout show defaults to false when input show is null`() {
        initSUT()

        val inputModel = FAppLockout.model(show = null, version = 1)
        val result = sut.mapAppLockout(inputModel)

        assertFalse(result.showLockout(-1))
    }

    @Test
    fun `AppLockout title defaults to App lockout when input title is null`() {
        initSUT()

        val inputModel = FAppLockout.model(title = null)
        val result = sut.mapAppLockout(inputModel)

        assertEquals("App lockout", result.title)
    }

    @ParameterizedTest(name = "AppLockout version is {1} when input version is {0}")
    @CsvSource(
        ",",
        "0,",
        "-1,",
        "1,1",
        "99999,99999"
    )
    fun `AppLockout version is value when input version is value`(input: Int?, expected: Int?) {
        initSUT()

        val inputModel = FAppLockout.model(version = input)
        val result = sut.mapAppLockout(inputModel)

        assertEquals(expected, result.version)
    }
}

