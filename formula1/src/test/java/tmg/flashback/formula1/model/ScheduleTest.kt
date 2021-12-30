package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

internal class ScheduleTest {

    @Test
    fun `timestamp field uses date and time`() {
        val date = LocalDate.now()
        val time = LocalTime.now()

        val model = Schedule.model(date = date, time = time)

        assertEquals(Timestamp(date, time), model.timestamp)
    }
}