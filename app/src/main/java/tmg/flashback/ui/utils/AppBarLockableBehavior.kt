package tmg.flashback.ui.utils

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout

class AppBarLockableBehavior : AppBarLayout.Behavior {

    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )
}