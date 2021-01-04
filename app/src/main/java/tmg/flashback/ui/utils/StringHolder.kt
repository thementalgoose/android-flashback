package tmg.flashback.ui.utils

import android.content.Context
import androidx.annotation.StringRes


data class StringHolder(
        val msg: String
) {
    private var id: Int? = null
    private var args: Array<Any> = emptyArray()

    constructor(@StringRes id: Int, vararg args: Any): this("") {
        this.id = id
        this.args = args.toList().toTypedArray()
    }

    fun resolve(context: Context): String {
        return when {
            id != null && args.isNotEmpty() -> context.getString(id!!, *args)
            id != null -> context.getString(id!!)
            else -> msg
        }
    }
}