package tmg.flashback.eastereggs.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Year
import kotlin.math.min

internal class MenuIconsTest {

    @Test
    fun `update required for easter eggs`() {
        val currentYear = Year.now().value
        val minYear = MenuIcons.values()
            .map { min(it.start.year, it.end.year) }
            .minBy { it }

        assertTrue(minYear <= currentYear, "Menu icons for easter eggs require updated date ranges, minYear found = $minYear, currentYear = $currentYear")
    }
}