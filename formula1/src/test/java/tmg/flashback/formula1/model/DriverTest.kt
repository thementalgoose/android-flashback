package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DriverTest {

    @Test
    fun `name combines first name and last name`() {
        val driver = Driver.model(
            firstName = "John",
            lastName = "Smith"
        )

        assertEquals("John Smith", driver.name)
    }
}