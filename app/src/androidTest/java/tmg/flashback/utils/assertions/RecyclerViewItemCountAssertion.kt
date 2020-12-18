package tmg.flashback.utils.assertions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.junit.jupiter.api.Assertions.assertEquals

class RecyclerViewItemCountAssertion(
    private val count: Int
): ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) throw noViewFoundException

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter!!
        assertEquals(count, adapter.itemCount)
    }
}