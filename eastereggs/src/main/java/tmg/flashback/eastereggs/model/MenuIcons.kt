package tmg.flashback.eastereggs.model

import org.threeten.bp.LocalDate
import org.threeten.bp.Year

private val year: Int by lazy { Year.now().value }
enum class MenuIcons(
    val key: String,
    val startResolver: () -> LocalDate,
    val endResolver: () -> LocalDate
) {
    NEW_YEARS(
        key = "new_year",
        startResolver = { LocalDate.of(year, 1, 1) },
        endResolver = { LocalDate.of(year, 1, 1) }
    ),
    CHINESE_NEW_YEAR(
        key = "new_year",
        startResolver = { LocalDate.of(2024, 2, 9) },
        endResolver = { LocalDate.of(2024, 2, 10) }
    ),
    VALENTINES_DAY(
        key = "valentines",
        startResolver = { LocalDate.of(year, 2, 11) },
        endResolver = { LocalDate.of(year, 2, 14) }
    ),
    EASTER(
        key = "easter",
        startResolver = { LocalDate.of(2023, 4, 4) },
        endResolver = { LocalDate.of(2023, 4, 9) }
    ),
    HALLOWEEN(
        key = "halloween",
        startResolver = { LocalDate.of(year, 10, 17) },
        endResolver = { LocalDate.of(year, 10, 31) }
    ),
    BONFIRE(
        key = "bonfire",
        startResolver = { LocalDate.of(year, 11, 4) },
        endResolver = { LocalDate.of(year, 11, 5) }
    ),
    CHRISTMAS(
        key = "christmas",
        startResolver = { LocalDate.of(year, 12, 19) },
        endResolver = { LocalDate.of(year, 12, 25) }
    ),
    NEW_YEARS_EVE(
        key = "new_year_eve",
        startResolver = { LocalDate.of(year, 12, 31) },
        endResolver = { LocalDate.of(year, 12, 31) }
    );

    val start: LocalDate by lazy { startResolver() }
    val end: LocalDate by lazy { endResolver() }

    fun isNow(now: LocalDate = LocalDate.now()): Boolean{
        return now in start..end
    }
}