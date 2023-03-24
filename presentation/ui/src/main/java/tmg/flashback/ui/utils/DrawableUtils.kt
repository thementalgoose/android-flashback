package tmg.flashback.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.murgupluoglu.flagkit.FlagKit
import tmg.flashback.ui.R
import java.util.Locale
import java.util.MissingResourceException

object DrawableUtils {

    @DrawableRes
    fun Context.getFlagResourceAlpha3(flag: String): Int {
        return getFlagResource(flag.toAlpha2ISO())
    }

    @DrawableRes
    fun Context.getFlagResource(flag: String?): Int {
        if (flag == null) {
            return R.drawable.nationality_not_found
        }
        return try {
            FlagKit.getResId(this, flag.lowercase(Locale.ENGLISH))
        } catch (e: RuntimeException) {
            R.drawable.nationality_not_found
        }
    }

    private fun String.toAlpha2ISO(): String? {
        return Locale.getISOCountries()
            .firstOrNull { isCountryCodesEqual(this, it) }
    }

    private fun isCountryCodesEqual(alpha3: String, alpha2: String) = try {
        alpha3.equals(Locale("", alpha2).isO3Country, ignoreCase = true)
    } catch (e: MissingResourceException) {
        false
    }

}