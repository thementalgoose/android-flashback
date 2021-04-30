package tmg.flashback.shared.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class LockableRecyclerView: RecyclerView {

    var locked: Boolean = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (locked) {
            return true
        }
        return super.onInterceptTouchEvent(e)
    }
}