package tmg.flashback.firebase.extensions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StringExtensionsKtTest {

    data class TestPojo(
        val str: String,
        val int: Int,
        val bool: Boolean,
        val nullable: String? = null,
        val list: List<String>
    )

    @Test
    fun `toJson converts a sample object to expected JSON`() {

        val expectedJson = """{"str":"test","int":2,"bool":true,"nullable":null,"list":["item-1"]}""".trimIndent()
        val exampleModel = TestPojo(
            str = "test",
            int = 2,
            bool = true,
            nullable = null,
            list = listOf("item-1")
        )

        assertEquals(exampleModel, expectedJson.toJson<TestPojo>())
    }
}