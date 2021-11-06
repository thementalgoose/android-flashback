package tmg.flashback.statistics.repo.mappers.app

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.room.models.constructors.model

private typealias RoomConstructor = tmg.flashback.statistics.room.models.constructors.Constructor

internal class ConstructorDataMapperTest {

    private lateinit var sut: ConstructorDataMapper

    @BeforeEach
    internal fun setUp() {
        sut = ConstructorDataMapper()
    }

    @Test
    fun `mapConstructorData maps fields correctly`() {
        val input = RoomConstructor.model()
        val expected = Constructor.model()

        assertEquals(expected, sut.mapConstructorData(input))
    }
}