package tmg.flashback.ui

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import tmg.flashback.ui.assertions.RecyclerViewItemCountAssertion
import tmg.flashback.ui.assertions.RecyclerViewItemGreaterThanAssertion
import tmg.flashback.ui.EspressoUtils.getString

open class RecyclerViewUtils(
    @IdRes val recyclerView: Int
) {

    //region Scrolling

    fun scrollToListText(text: String) {
        onView(withId(recyclerView))
            .perform(scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(text))))
    }

    fun scrollToListText(@StringRes id: Int) {
        val text = getString(id)
        scrollToListText(text)
    }

    //endregion

    //region Text Assertions / Actions

    fun assertTextDisplayedInList(text: String?) {
        scrollToListText(text ?: "ERROR - TEXT NOT FOUND")
        assertDisplayed(text ?: "ERROR - TEXT NOT FOUND")
    }

    fun assertTextDisplayedInList(@IdRes id: Int) {
        val text = getString(id)
        assertTextDisplayedInList(text)
    }

    fun clickOnListText(@StringRes id: Int) {
        val text = getString(id)
        clickOnListText(text)
    }

    fun clickOnListText(text: String) {
        scrollToListText(text)
        EspressoUtils.clickOnText(text)
    }

    //endregion

    //region RecyclerView actions

    fun assertItemsAtLeast(count: Int) {
        onView(withId(recyclerView))
            .check(RecyclerViewItemGreaterThanAssertion(count))
    }

    fun assertItemsEquals(count: Int) {
        onView(withId(recyclerView))
            .check(RecyclerViewItemCountAssertion(count))
    }

    //endregion
}