package tmg.flashback.formula1.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.murgupluoglu.flagkit.FlagKit
import tmg.flashback.formula1.R
import java.util.Locale

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