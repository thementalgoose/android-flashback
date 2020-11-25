package tmg.flashback.rss.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.jupiter.api.Assertions.*
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

/**
 * Assert method for live data
 */
fun <T> assertLiveData(liveData: LiveData<T>, block: LiveDataTestScope<T>.() -> Unit) {
    block(LiveDataTestScope(liveData))
}

/**
 * Assert method for live data
 */
fun <T> LiveData<T>.test(block: LiveDataTestScope<T>.() -> Unit) {
    block(LiveDataTestScope(this))
}

/**
 * Return the test scope observer to run custom checks on the observer
 */
fun <T> LiveData<T>.testObserve(): LiveDataTestScope<T> {
    return LiveDataTestScope(this)
}

/**
 * Live data testing scope class
 */
class LiveDataTestScope<T>(
    liveData: LiveData<T>
): Observer<T> {

    private val listOfValues: MutableList<T> = mutableListOf()

    init {
        liveData.observeForever(this)
    }

    override fun onChanged(t: T) {
        listOfValues.add(t)
    }

    internal val latestValue: T?
        get() = listOfValues.lastOrNull()

    /**
     * Assert that the latest value emitted matches
     *  the live data matches exactly
     */
    fun assertValue(expected: T) {
        assertNotNull(latestValue)
        assertEquals(expected, latestValue)
    }

    /**
     * Given a number of items have been emitted, assert that the value at
     *  the given position matches what is expected
     */
    fun assertValueAt(expected: T, position: Int) {
        assertTrue(listOfValues.size > position, "Number of items emitted is less than position requested (${listOfValues.size} > $position)")
        assertEquals(expected, listOfValues[position])
    }

    /**
     * Assert that of any of the values that have been emitted that one of them
     *  is the expected item
     */
    fun assertValueExists(expected: T) {
        listOfValues.forEach {
            if (it == expected) { return }
        }
        assertFalse(true, "All emitted items do not contain the expected item. $expected (${listOfValues.size} items emitted)")
    }

    /**
     * Assert that the number of items emitted is equal to the expected value
     */
    fun assertItemCount(expected: Int) {
        assertEquals(listOfValues.size, expected)
    }

    /**
     * Assert that the items emitted match the expected list
     */
    fun assertEmittedItems(vararg expected: T) {
        assertEmittedItems(expected.toList())
    }

    /**
     * Assert that the items emitted match the expected list
     */
    fun assertEmittedItems(expected: List<T>) {
        assertEquals(listOfValues, expected)
    }
}

//region LiveDataTestScope<Event>

/**
 * Assert that an event has been fired in the latest value
 */
fun <T: Event> LiveDataTestScope<T>.assertEventFired() {
    assertNotNull(latestValue)
}

/**
 * Assert that an event has not been fired
 */
fun <T: Event> LiveDataTestScope<T>.assertEventNotFired() {
    assertNull(latestValue)
}

/**
 * Assert that a data event item has been fired
 *  and that the data contains the item
 */
fun <T> LiveDataTestScope<DataEvent<T>>.assertDataEventValue(expected: T) {
    assertEventFired()
    assertEquals(expected, latestValue!!.data)
}

//endregion

//region LiveDataTestScope<List<T>>

/**
 * Assert that given the subject is a list that only one list has been
 *  emitted and the list contains the following item
 */
fun <T> LiveDataTestScope<List<T>>.assertListContainsItems(vararg item: T) {
    item.forEach {
        assertListContainsItem(it)
    }
}

/**
 * Assert that given the subject is a list that only one list has been
 *  emitted and the list contains the following item
 */
fun <T> LiveDataTestScope<List<T>>.assertListContainsItem(item: T) {
    assertNotNull(latestValue)
    latestValue!!.forEach {
        if (it == item) return
    }
    assertFalse(true, "List does not contain the expected item - $item (${latestValue!!.size} items)")
}

/**
 * Assert that given the subject is a list that only one list has been
 *  emitted and the list contains the following item
 */
fun <T> LiveDataTestScope<List<T>>.assertListHasItem(predicate: (item: T) -> Boolean) {
    assertNotNull(latestValue)
    latestValue!!.forEach {
        if (predicate(it)) return
    }
    assertFalse(true, "List does not contain an item that matches the predicate - (${latestValue!!.size} items)")
}

//endregion