package tmg.flashback.widgets.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat


object BitmapUtils {

    fun setTint(res: Resources?, resId: Int, tint: Int): Drawable {
        val bitmap = BitmapFactory.decodeResource(res, resId)
        // make a copy of the drawable object
        val bitmapDrawable: Drawable = BitmapDrawable(res, bitmap)
        // setup color filter for tinting
        val cf: ColorFilter = PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN)
        bitmapDrawable.colorFilter = cf
        return bitmapDrawable
    }

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