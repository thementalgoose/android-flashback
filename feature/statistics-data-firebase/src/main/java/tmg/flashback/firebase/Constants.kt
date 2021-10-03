package tmg.flashback.firebase

import org.threeten.bp.Year

internal val currentYear: Int
    get() = Year.now().value

private const val overviewStartingYear: Int = 1950

internal val overviewKeys: List<String> by lazy {
    return@lazy getOverviewKeys(overviewStartingYear, currentYear)
        .map { "season$it" }
}

internal fun getOverviewKeys(startingYear: Int, current: Int): List<Int> {
    val number = (current - startingYear) / 10
    return List(number + 1) { it * 10 + startingYear }
}