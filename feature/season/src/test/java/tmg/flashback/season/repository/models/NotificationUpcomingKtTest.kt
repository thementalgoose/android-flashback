package tmg.flashback.season.repository.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.notifications.NotificationUpcoming

internal class NotificationUpcomingKtTest {
    @Test
    fun `pref names for results available are distinct`() {
        assertEquals(NotificationUpcoming.values().size, NotificationUpcoming.values().distinctBy { it.prefKey }.size)
    }
}
