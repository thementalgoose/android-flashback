package tmg.flashback.extensions

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.disableTouch() {
    this.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return true
        }
    })
}