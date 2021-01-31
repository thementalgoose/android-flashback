package tmg.flashback.rss.repo.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ResponseTest {

    lateinit var sut: Response<String>

    @Test
    fun `if result is not null then response code is 200`() {

        val expected = "Not null"
        sut = Response(expected)

        assertEquals(sut.code, 200)
        assertEquals(sut.result, expected)
        assertEquals(sut.isNoNetwork, false)
    }

    @Test
    fun `if result is null then response code is 500`() {

        val expected = null
        sut = Response(expected)

        assertEquals(sut.code, 500)
        assertNull(sut.result)
        assertEquals(sut.isNoNetwork, false)
    }

    @Test
    fun `if response code is set to -1 then response code is considered no network`() {

        val expected = null
        sut = Response(expected, -1)

        assertEquals(sut.code, -1)
        assertNull(sut.result)
        assertEquals(sut.isNoNetwork, true)
    }
}