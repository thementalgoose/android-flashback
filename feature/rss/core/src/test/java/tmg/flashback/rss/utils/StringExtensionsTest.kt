package tmg.flashback.rss.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class StringExtensionsTest {

    @ParameterizedTest(name = "stripHTTP stripping prefixes {0}.stripHTTP() = {1}")
    @CsvSource(
        "https://www.google.com,google.com",
        "http://www.google.com,google.com",
        "https://google.com,google.com",
        "http://google.com,google.com",
        "www.google.com,google.com",
        "ww.google.com,ww.google.com"
    )
    fun `stripHost strips the prefixes off of values`(input: String, expected: String) {
        assertEquals(expected, input.stripHTTP())
    }

    @Test
    fun `stripWWW strips www off start`() {
        assertEquals("google.com", "www.google.com".stripWWW())
    }
}