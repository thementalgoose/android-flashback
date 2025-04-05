package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

internal class ScheduleTest {

    @Test
    fun `timestamp field uses date and time`() {
        val date = LocalDate.now()
        val time = LocalTime.now()

        val model = Schedule.model(date = date, time = time)

        assertEquals(Timestamp(date, time), model.timestamp)
    }
}