package tmg.core.prefs.manager

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SharedPreferenceManagerTest {

    private val mockContext: Context = mockk()
    private val mockPreferences: SharedPreferences = mockk()
    private val mockEditor: SharedPreferences.Editor = mockk()

    private lateinit var sut: SharedPreferenceManager

    private fun initSUT() {
        sut = object : SharedPreferenceManager(mockContext) {
            override val prefsKey: String
                get() = "prefsKey"
        }
    }

    @BeforeEach
    internal fun setUp() {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockPreferences
        every { mockPreferences.edit() } returns mockEditor
        every { mockEditor.putInt(any(), any()) } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.putStringSet(any(), any()) } returns mockEditor
        every { mockEditor.putBoolean(any(), any()) } returns mockEditor
        every { mockEditor.putLong(any(), any()) } returns mockEditor
        every { mockEditor.putFloat(any(), any()) } returns mockEditor
        every { mockEditor.apply() } returns Unit
    }

    @Test
    fun `save int saves to preference editor`() {
        initSUT()
        sut.save("my-key", 1)
        verify {
            mockEditor.putInt("my-key", 1)
            mockEditor.apply()
        }
    }

    @Test
    fun `save string saves to preference editor`() {
        initSUT()
        sut.save("my-key", "sup")
        verify {
            mockEditor.putString("my-key", "sup")
            mockEditor.apply()
        }
    }

    @Test
    fun `save long saves to preference editor`() {
        initSUT()
        sut.save("my-key", 1L)
        verify {
            mockEditor.putLong("my-key", 1L)
            mockEditor.apply()
        }
    }

    @Test
    fun `save float saves to preference editor`() {
        initSUT()
        sut.save("my-key", 1f)
        verify {
            mockEditor.putFloat("my-key", 1f)
            mockEditor.apply()
        }
    }

    @Test
    fun `save boolean saves to preference editor`() {
        initSUT()
        sut.save("my-key", true)
        verify {
            mockEditor.putBoolean("my-key", true)
            mockEditor.apply()
        }
    }

    @Test
    fun `save string set saves to preference editor`() {
        initSUT()
        sut.save("my-key", setOf("hey"))
        verify {
            mockEditor.putStringSet("my-key", setOf("hey"))
            mockEditor.apply()
        }
    }

    @Test
    fun `get int pulls from preferences`() {
        every { mockPreferences.getInt(any(), any()) } returns 1
        initSUT()
        assertEquals(1, sut.getInt("key", 2))
        verify {
            mockPreferences.getInt("key", 2)
        }
    }

    @Test
    fun `get string pulls from preferences`() {
        every { mockPreferences.getString(any(), any()) } returns "hey"
        initSUT()
        assertEquals("hey", sut.getString("key", "sup"))
        verify {
            mockPreferences.getString("key", "sup")
        }
    }

    @Test
    fun `get float pulls from preferences`() {
        every { mockPreferences.getFloat(any(), any()) } returns 1f
        initSUT()
        assertEquals(1f, sut.getFloat("key", -1f))
        verify {
            mockPreferences.getFloat("key", -1f)
        }
    }

    @Test
    fun `get long pulls from preferences`() {
        every { mockPreferences.getLong(any(), any()) } returns 2L
        initSUT()
        assertEquals(2L, sut.getLong("key", 1L))
        verify {
            mockPreferences.getLong("key", 1L)
        }
    }

    @Test
    fun `get boolean pulls from preferences`() {
        every { mockPreferences.getBoolean(any(), any()) } returns true
        initSUT()
        assertTrue(sut.getBoolean("key", false))
        verify {
            mockPreferences.getBoolean("key", false)
        }
    }

    @Test
    fun `get set pulls from preferences`() {
        every { mockPreferences.getStringSet(any(), any()) } returns setOf("hey")
        initSUT()
        assertEquals(setOf("hey"), sut.getSet("key", setOf("default")))
        verify {
            mockPreferences.getStringSet("key", setOf("default"))
        }
    }
}