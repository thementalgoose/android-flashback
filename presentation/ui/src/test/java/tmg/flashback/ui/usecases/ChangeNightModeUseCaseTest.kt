package tmg.flashback.ui.usecases

import androidx.appcompat.app.AppCompatDelegate
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository

internal class ChangeNightModeUseCaseTest {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)

    private lateinit var underTest: ChangeNightModeUseCase

    private fun initUnderTest() {
        underTest = ChangeNightModeUseCase(mockThemeRepository)
    }

    @Test
    fun `setting night theme saves to theme repository`() {
        initUnderTest()
        underTest.setNightMode(NightMode.DAY)

        verify {
            mockThemeRepository.nightMode = NightMode.DAY
        }
    }

    @Test
    fun `setting night theme DAY sets app compat delegate method`() {
        mockkStatic(AppCompatDelegate::class)

        initUnderTest()
        underTest.setNightMode(NightMode.DAY)

        verify {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    @Test
    fun `setting night theme DEFAULT sets app compat delegate method`() {
        mockkStatic(AppCompatDelegate::class)

        initUnderTest()
        underTest.setNightMode(NightMode.DEFAULT)

        verify {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    @Test
    fun `setting night theme NIGHT sets app compat delegate method`() {
        mockkStatic(AppCompatDelegate::class)

        initUnderTest()
        underTest.setNightMode(NightMode.NIGHT)

        verify {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}