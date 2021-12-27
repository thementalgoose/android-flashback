package tmg.flashback.appshortcuts.models

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

data class ShortcutInfo(
    val id: String,
    @StringRes
    val shortLabel: Int,
    @StringRes
    val longLabel: Int,
    @DrawableRes
    val icon: Int,
    @StringRes
    val unavailableMessage: Int,
    val intentResolver: (Context) -> Intent
)