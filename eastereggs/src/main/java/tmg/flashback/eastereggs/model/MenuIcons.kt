package tmg.flashback.eastereggs.model

import androidx.annotation.StringRes
import java.time.LocalDate
import java.time.Year
import tmg.flashback.strings.R.string

private val year: Int by lazy { Year.now().value }
enum class MenuIcons(
    val key: String,
    val startResolver: () -> LocalDate,
    val endResolver: () -> LocalDate,
    @StringRes
    val label: Int?
) {
    NEW_YEARS(
        key = "new_year",
        startResolver = { LocalDate.of(year, 1, 1) },
        endResolver = { LocalDate.of(year, 1, 1) },
        label = string.easter_egg_menu_new_years
    ),
    CHINESE_NEW_YEAR(
        key = "new_year",
        startResolver = { LocalDate.of(2026, 2, 17) },
        endResolver = { LocalDate.of(2026, 3, 3) },
        label = string.easter_egg_menu_chinese_new_year
    ),
    VALENTINES_DAY(
        key = "valentines",
        startResolver = { LocalDate.of(year, 2, 12) },
        endResolver = { LocalDate.of(year, 2, 14) },
        label = string.easter_egg_menu_valentines
    ),
    EASTER(
        key = "easter",
        startResolver = { LocalDate.of(2025, 4, 18) },
        endResolver = { LocalDate.of(2025, 4, 21) },
        label = string.easter_egg_menu_easter
    ),
    HALLOWEEN(
        key = "halloween",
        startResolver = { LocalDate.of(year, 10, 21) },
        endResolver = { LocalDate.of(year, 10, 31) },
        label = string.easter_egg_menu_halloween
    ),
    BONFIRE(
        key = "bonfire",
        startResolver = { LocalDate.of(year, 11, 4) },
        endResolver = { LocalDate.of(year, 11, 5) },
        label = null
    ),
    DIWALI(
        key = "diwali",
        startResolver = { LocalDate.of(2025, 10, 19) },
        endResolver = { LocalDate.of(2025, 10, 21) },
        label = string.easter_egg_menu_diwali
    ),
    CHRISTMAS(
        key = "christmas",
        startResolver = { LocalDate.of(year, 12, 19) },
        endResolver = { LocalDate.of(year, 12, 25) },
        label = string.easter_egg_menu_christmas
    ),
    NEW_YEARS_EVE(
        key = "new_year_eve",
        startResolver = { LocalDate.of(year, 12, 31) },
        endResolver = { LocalDate.of(year, 12, 31) },
        label = string.easter_egg_menu_new_years
    );

    val start: LocalDate by lazy { startResolver() }
    val end: LocalDate by lazy { endResolver() }

    fun isNow(now: LocalDate = LocalDate.now()): Boolean{
        return now in start..end
    }
}