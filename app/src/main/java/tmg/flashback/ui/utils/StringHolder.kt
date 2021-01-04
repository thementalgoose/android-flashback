package tmg.flashback.ui.utils

import android.content.Context
import androidx.annotation.StringRes

data class StringHolder(
    @StringRes
    val id: Int? = null,
    val msg: String? = null
) {
    fun resolve(context: Context): String {
        return when {
            id != null -> context.getString(id)
            msg != null -> msg
            else -> ""
        }
    }
}