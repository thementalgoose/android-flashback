package tmg.flashback.utils

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import tmg.flashback.utils.EspressoUtils.getString

class RecyclerViewUtils(
    @IdRes val recyclerView: Int
) {
    fun assertTextDisplayed(text: String) {
        scrollToText(text)
        assertDisplayed(text)
    }

    fun scrollToText(text: String) {
        onView(withId(recyclerView))
            .perform(scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(text))))
    }

    fun scrollToText(@StringRes id: Int) {
        val text = getString(id)
        scrollToText(text)
    }

    fun assertTextDisplayed(@IdRes id: Int) {
        val text = getString(id)
        assertTextDisplayed(text)
    }
}