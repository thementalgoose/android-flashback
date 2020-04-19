package tmg.flashback.utils

import android.util.Log
import tmg.flashback.BuildConfig

fun localLog(msg: String) {
    if (BuildConfig.DEBUG) {
        Log.i("Flashback", msg)
    }
}