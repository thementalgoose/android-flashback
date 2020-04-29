package tmg.flashback.extensions

import android.app.Activity
import android.util.DisplayMetrics
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import tmg.utilities.extensions.views.recyclerView
import kotlin.math.ceil


/**
 * Set a desired target width for the fragments shown inside a ViewPager2
 *
 * @param activity
 * @param startPadding Padding at the start of the View Pager
 * @param widthOfContent Width that the padding will be
 *
 * @return Number of items that will be displayed by the viewpager with this target width
 */
fun ViewPager2.setContentMultiplierForFullWidthPager(activity: Activity, @Px startPadding: Int, widthOfContent: Float): Int {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val deviceWidth = displayMetrics.widthPixels

    recyclerView.setPadding(startPadding, 0, (deviceWidth - ((widthOfContent * deviceWidth) + startPadding)).toInt(), 0)
    recyclerView.clipToPadding = false
    recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    return ceil(deviceWidth.toDouble() / widthOfContent.toDouble()).toInt()
}