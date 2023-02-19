package tmg.flashback.formula1.utils

import java.util.Locale
import java.util.MissingResourceException

fun String.toAlpha2ISO(): String? {
    return Locale.getISOCountries()
        .firstOrNull { isCountryCodesEqual(this, it) }
}

private fun isCountryCodesEqual(alpha3: String, alpha2: String) = try {
    alpha3.equals(Locale("", alpha2).isO3Country, ignoreCase = true)
} catch (e: MissingResourceException) {
    false
}

