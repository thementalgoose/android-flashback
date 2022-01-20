package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate

fun WinterTesting.Companion.model(
    label: String = "label",
    date: LocalDate = LocalDate.of(2020, 10, 12)
): WinterTesting = WinterTesting(
    label = label,
    date = date
)