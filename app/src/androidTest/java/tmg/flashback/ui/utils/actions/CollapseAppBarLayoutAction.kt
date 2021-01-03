package tmg.flashback.ui.utils.actions

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.google.android.material.appbar.AppBarLayout
import org.hamcrest.Matcher

class CollapseAppBarLayoutAction: ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(AppBarLayout::class.java)
    }

    override fun getDescription() = "Collapse App Bar Layout"

    override fun perform(uiController: UiController?, view: View?) {
        val layout = view as AppBarLayout
        layout.setExpanded(false)
        uiController?.loopMainThreadUntilIdle()
    }
}