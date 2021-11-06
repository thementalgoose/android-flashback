package tmg.flashback.statistics.repo.mappers.app

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.room.models.constructors.model

private typealias RoomConstructorHistory = tmg.flashback.statistics.room.models.constructors.ConstructorHistory

internal class ConstructorMapperTest {

    private val mockConstructorDataMapper: ConstructorDataMapper = mockk(relaxed = true)
    private val mockDriverDataMapper: DriverDataMapper = mockk(relaxed = true)

    private lateinit var sut: ConstructorMapper

    @BeforeEach
    internal fun setUp() {
        sut = ConstructorMapper(
            mockConstructorDataMapper,
            mockDriverDataMapper
        )

        every { mockDriverDataMapper.mapDriver(any()) } returns Driver.model()
        every { mockConstructorDataMapper.mapConstructorData(any()) } returns Constructor.model()
    }

    @Test
    fun `mapConstructor maps fields correctly`() {
        val input = RoomConstructorHistory.model()
        val expected = ConstructorHistory.model()

        assertEquals(expected, sut.mapConstructor(input))
    }
}