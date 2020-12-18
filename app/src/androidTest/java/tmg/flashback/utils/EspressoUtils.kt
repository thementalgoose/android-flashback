package tmg.flashback.utils

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.internal.viewaction.NestedEnabledScrollToAction.scrollTo
import tmg.flashback.R
import tmg.flashback.utils.actions.CollapseAppBarLayoutAction

object EspressoUtils {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext!!

    fun getString(@StringRes res: Int): String {
        return context.getString(res)
    }

    fun collapseAppBar(@IdRes id: Int = R.id.appbar) {
        onView(withId(id)).perform(CollapseAppBarLayoutAction())
    }

    fun assertTextDisplayed(@StringRes textId: Int) {
        val text = getString(textId)
        assertTextDisplayed(text)
    }

    fun assertTextDisplayed(text: String) {
        assertDisplayed(text)
    }

    //region RecyclerView

    fun withRecyclerView(@IdRes recyclerView: Int, method: RecyclerViewUtils.() -> Unit) {
        method(RecyclerViewUtils(recyclerView))
    }

    //endregion

    //region Clicking

    fun clickOn(@IdRes id: Int) {
        onView(withId(id)).perform(click())
    }

    fun clickOnText(@StringRes id: Int) {
        val text = getString(id)
        clickOnText(text)
    }

    fun clickOnText(text: String) {
        onView(withText(text)).perform(click())
    }

    //endregion
}