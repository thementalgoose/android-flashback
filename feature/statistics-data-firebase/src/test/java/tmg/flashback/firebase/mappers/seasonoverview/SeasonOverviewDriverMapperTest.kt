package tmg.flashback.firebase.mappers.seasonoverview

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeason
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonOverviewDriver
import tmg.flashback.firebase.models.model
import tmg.testutils.BaseTest

internal class SeasonOverviewDriverMapperTest: BaseTest() {

    private val mockConstructorMapper: SeasonOverviewConstructorMapper = mockk(relaxed = true)

    private lateinit var sut: SeasonOverviewDriverMapper

    private fun initSUT() {
        sut = SeasonOverviewDriverMapper(mockConstructorMapper)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockConstructorMapper.mapConstructor(FSeasonOverviewConstructor.model("constructorId")) } returns mockConstructor()
        every { mockConstructorMapper.mapConstructor(FSeasonOverviewConstructor.model("constructorId1")) } returns mockConstructor("constructorId1")
        every { mockConstructorMapper.mapConstructor(FSeasonOverviewConstructor.model("constructorId2")) } returns mockConstructor("constructorId2")
    }

    @Test
    fun `Driver maps fields correctly`() {
        initSUT()

        val input = FSeason.model()
        val expected = Driver(
            id = "driverId",
            firstName = "firstName",
            lastName = "lastName",
            code = "ALB",
            number = 23,
            wikiUrl = "wikiUrl",
            photoUrl = "photoUrl",
            dateOfBirth = LocalDate.of(1995, 10, 12),
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            constructors = mapOf(
                1 to Constructor(
                    id = "constructorId",
                    name = "constructorName",
                    wikiUrl = "wikiUrl",
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    color = 0
                )
            ),
            startingConstructor = Constructor(
                id = "constructorId",
                name = "constructorName",
                wikiUrl = "wikiUrl",
                nationality = "nationality",
                nationalityISO = "nationalityISO",
                color = 0
            )
        )

        assertEquals(expected, sut.mapDriver(input, "driverId"))
    }

    @Test
    fun `Driver returns null if driver id not found in season object`() {
        initSUT()

        val input = FSeason.model()
        assertNull(sut.mapDriver(input, "driverId2"))
    }

    @Test
    fun `Driver object contains accurate map of race constructors if driverCon exists`() {
        initSUT()

        val input = FSeason.model(
            constructors = mapOf(
                "constructorId1" to FSeasonOverviewConstructor.model(id = "constructorId1"),
                "constructorId2" to FSeasonOverviewConstructor.model(id = "constructorId2"),
            ),
            race = mapOf(
                "r1" to FRound.model(round = 1, driverCon = mapOf(
                    "driverId" to "constructorId1",
                )),
                "r2" to FRound.model(round = 2, driverCon = mapOf(
                    "driverId" to "constructorId2"
                ))
            )
        )
        val expected = mapOf(
            1 to mockConstructor("constructorId1"),
            2 to mockConstructor("constructorId2")
        )

        every { mockConstructorMapper.mapConstructor(FSeasonOverviewConstructor.model(id = "constructorId1")) } returns mockConstructor("constructorId1")
        every { mockConstructorMapper.mapConstructor(FSeasonOverviewConstructor.model(id = "constructorId2")) } returns mockConstructor("constructorId2")

        assertEquals(expected, sut.mapDriver(input, "driverId")!!.constructors)
    }

    @Test
    fun `Driver throws error if no constructor is found for the driver in top list`() {
        initSUT()

        val input = FSeason.model(constructors = null)
        assertThrows(NullPointerException::class.java) {
            sut.mapDriver(input, "driverId")
        }
    }

    @Test
    fun `Driver throws error if constructor not assigned to driver is not available in top list`() {
        initSUT()

        val input = FSeason.model(constructors = mapOf(
            "constructorId2" to FSeasonOverviewConstructor.model(id = "constructorId2")
        ))
        assertThrows(NullPointerException::class.java) {
            sut.mapDriver(input, "driverId")
        }
    }

    @Test
    fun `Driver number returns 0 if number is null`() {
        initSUT()

        val input = FSeason.model(drivers = mapOf(
            "driverId" to FSeasonOverviewDriver.model(
                number = null
            )
        ))
        assertEquals(0, sut.mapDriver(input, "driverId")!!.number)
    }

    @Test
    fun `Driver invalid date of birth crashes mapper`() {
        initSUT()

        val input = FSeason.model(drivers = mapOf(
            "driverId" to FSeasonOverviewDriver.model(
                dob = "invalid"
            )
        ))
        assertThrows(DateTimeParseException::class.java) {
            sut.mapDriver(input, "driverId")
        }
    }

    private fun mockConstructor(id: String = "constructorId"): Constructor {
        return Constructor(
            id = id,
            name = "constructorName",
            wikiUrl = "wikiUrl",
            nationality = "nationality",
            nationalityISO = "nationalityISO",
            color = 0
        )
    }
}