package tmg.flashback.widgets.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


object BitmapUtils {

    fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int, @ColorInt tint: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId).apply {
            this?.colorFilter = PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN)
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}