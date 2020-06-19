package tmg.flashback

import android.util.Log
import tmg.utilities.extensions.toEnum

val env: Env
    get() {
        Log.i("Flashback", "Environment is ${BuildConfig.ENVIRONMENT}")
        return BuildConfig.ENVIRONMENT.toEnum<Env> { it.id } ?: Env.LIVE
    }

enum class Env(
    val id: Int
) {
    SAND(1),
    LIVE(0);

    val isLive: Boolean
        get() = this == LIVE
}