package tmg.flashback.utils

import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import tmg.flashback.R
import tmg.flashback.utils.actions.CollapseAppBarLayoutAction
import java.lang.Exception


object EspressoUtils {

    //region RecyclerView

    fun withRecyclerView(@IdRes recyclerView: Int, method: RecyclerViewUtils.() -> Unit) {
        try {
            collapseAppBar()
        } catch (e: NoMatchingViewException) { /* Ignore */ }
        method(RecyclerViewUtils(recyclerView))
    }

    //endregion

    //region AppBar

    fun collapseAppBar(@IdRes id: Int = R.id.appbar) {
        onView(withId(id)).perform(CollapseAppBarLayoutAction())
    }

    //endregion

    //region Text assertions

    fun assertTextDisplayed(@StringRes textId: Int) {
        val text = getString(textId)
        assertTextDisplayed(text)
    }

    fun assertTextDisplayed(text: String) {
        assertDisplayed(text)
    }

    fun assertViewDisplayed(@IdRes id: Int) {
        assertDisplayed(id)
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

    //region Navigation

    fun pressBack() {
        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        mDevice.pressBack()
    }

    //endregion

    //region Intents

    fun assertIntentFired(action: String, block: () -> Unit) {
        assertIntentFired(allOf(hasAction(action)), block)
    }

    fun assertIntentFired(matcher: Matcher<Intent>, block: () -> Unit) {
        Intents.init()
        block()
        intended(matcher)
        try {
            Intents.release()
        } catch (e: Exception) { /* Thrown */ }
        pressBack()
    }

    //endregion

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext!!

    internal fun getString(@StringRes res: Int): String {
        return context.getString(res)
    }

}