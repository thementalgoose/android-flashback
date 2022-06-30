package tmg.flashback.rss.repo.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class ResponseTest {

    lateinit var underTest: Response<String>

    @Test
    fun `if result is not null then response code is 200`() {

        val expected = "Not null"
        underTest = Response(expected)

        assertEquals(underTest.code, 200)
        assertEquals(underTest.result, expected)
        assertEquals(underTest.isNoNetwork, false)
    }

    @Test
    fun `if result is null then response code is 500`() {

        val expected = null
        underTest = Response(expected)

        assertEquals(underTest.code, 500)
        assertNull(underTest.result)
        assertEquals(underTest.isNoNetwork, false)
    }

    @Test
    fun `if response code is set to -1 then response code is considered no network`() {

        val expected = null
        underTest = Response(expected, -1)

        assertEquals(underTest.code, -1)
        assertNull(underTest.result)
        assertEquals(underTest.isNoNetwork, true)
    }
}