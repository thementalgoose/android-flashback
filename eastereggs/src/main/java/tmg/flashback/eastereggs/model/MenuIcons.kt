package tmg.flashback.eastereggs.model

import org.threeten.bp.LocalDate
import org.threeten.bp.Year

private val year: Int by lazy { Year.now().value }
enum class MenuIcons(
    val key: String,
    val start: LocalDate,
    val end: LocalDate
) {
    NEW_YEARS(
        key = "new_year",
        start = LocalDate.of(year, 1, 1),
        end = LocalDate.of(year, 1, 1)
    ),
    CHINESE_NEW_YEAR(
        key = "new_year",
        start = LocalDate.of(2023, 1, 21),
        end = LocalDate.of(2023, 1, 22)
    ),
    VALENTINES_DAY(
        key = "valentines",
        start = LocalDate.of(year, 2, 11),
        end = LocalDate.of(year, 2, 14)
    ),
    EASTER(
        key = "easter",
        start = LocalDate.of(2023, 4, 4),
        end = LocalDate.of(2023, 4, 9)
    ),
    HALLOWEEN(
        key = "halloween",
        start = LocalDate.of(year, 10, 17),
        end = LocalDate.of(year, 10, 31)
    ),
    BONFIRE(
        key = "bonfire",
        start = LocalDate.of(year, 11, 4),
        end = LocalDate.of(year, 11, 5)
    ),
    CHRISTMAS(
        key = "christmas",
        start = LocalDate.of(year, 12, 19),
        end = LocalDate.of(year, 12, 25)
    ),
    NEW_YEARS_EVE(
        key = "new_year_eve",
        start = LocalDate.of(year, 12, 31),
        end = LocalDate.of(year, 12, 31)
    );

    fun isNow(now: LocalDate = LocalDate.now()): Boolean{
        return now in start..end
    }
}