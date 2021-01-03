package tmg.flashback.ui.utils

import java.util.*

fun String.toAlpha2ISO(): String? {
    return Locale.getISOCountries()
        .firstOrNull { isCountryCodesEqual(this, it) }
}

private fun isCountryCodesEqual(alpha3: String, alpha2: String): Boolean {
    return alpha3.equals(Locale("", alpha2).isO3Country, ignoreCase = true)
}
