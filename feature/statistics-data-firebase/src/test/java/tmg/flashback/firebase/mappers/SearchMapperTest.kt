package tmg.flashback.firebase.mappers

import io.mockk.mockk
import io.mockk.verify
import java.lang.NullPointerException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.SearchDriver
import tmg.flashback.firebase.models.FSearchDriver
import tmg.flashback.firebase.models.FSearchDriverModel
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class SearchMapperTest: BaseTest() {

    private val mockCrashController: CrashController = mockk(relaxed = true)

    private lateinit var sut: SearchMapper

    private fun initSUT() {
        sut = SearchMapper(mockCrashController)
    }

    @Test
    fun `SearchDriver maps fields correctly`() {
        initSUT()

        val input = FSearchDriverModel.model()
        val inputId = "driverId"
        val expected = SearchDriver(
            id = "driverId",
            firstName = "firstName",
            lastName = "lastName",
            image = "imageUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            dateOfBirth = LocalDate.of(1995, 10, 12),
            wikiUrl = "wikiUrl"
        )

        assertEquals(expected, sut.mapSearchDriver(input, inputId))
        verify(exactly = 0) {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `SearchDriver returns null if first name is null`() {
        initSUT()

        val input = FSearchDriverModel.model(fname = null)
        assertNull(sut.mapSearchDriver(input, "driverId"))
        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `SearchDriver returns null if last name is null`() {
        initSUT()

        val input = FSearchDriverModel.model(sname = null)
        assertNull(sut.mapSearchDriver(input, "driverId"))
        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `SearchDriver returns null if nationality iso name is null`() {
        initSUT()

        val input = FSearchDriverModel.model(natISO = null)
        assertNull(sut.mapSearchDriver(input, "driverId"))
        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `SearchDriver returns null if date of birth is null`() {
        initSUT()

        val input = FSearchDriverModel.model(dob = null)
        assertNull(sut.mapSearchDriver(input, "driverId"))
        verify {
            mockCrashController.logException(any())
        }
    }

    @Test
    fun `SearchDriver throws exception if date of birth is invalid`() {
        initSUT()

        val input = FSearchDriverModel.model(dob = "invalid")
        assertNull(sut.mapSearchDriver(input, "driverId"))
        verify {
            mockCrashController.logException(any())
        }
    }
}