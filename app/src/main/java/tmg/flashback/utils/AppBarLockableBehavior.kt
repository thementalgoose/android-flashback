package tmg.flashback.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import tmg.flashback.R

class AppBarLockableBehavior : AppBarLayout.Behavior {

    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )
}