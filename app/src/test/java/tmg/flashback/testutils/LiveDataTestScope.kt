package tmg.flashback.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.jupiter.api.Assertions.*
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

/**
 * Assert method for live data
 */
internal fun <T> assertLiveData(liveData: LiveData<T>, block: LiveDataTestScope<T>.() -> Unit) {
    block(LiveDataTestScope(liveData))
}

/**
 * Assert method for live data
 */
internal fun <T> LiveData<T>.test(block: LiveDataTestScope<T>.() -> Unit) {
    block(LiveDataTestScope(this))
}

/**
 * Return the test scope observer to run custom checks on the observer
 */
internal fun <T> LiveData<T>.testObserve(): LiveDataTestScope<T> {
    return LiveDataTestScope(this)
}

/**
 * Live data testing scope class
 */
internal class LiveDataTestScope<T>(
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
internal fun <T: Event> LiveDataTestScope<T>.assertEventFired() {
    assertNotNull(latestValue)
}

/**
 * Assert that an event has not been fired
 */
internal fun <T: Event> LiveDataTestScope<T>.assertEventNotFired() {
    assertNull(latestValue)
}

/**
 * Assert that a data event item has been fired
 *  and that the data contains the item
 */
internal fun <T> LiveDataTestScope<DataEvent<T>>.assertDataEventValue(expected: T) {
    assertEventFired()
    assertEquals(expected, latestValue!!.data)
}

/**
 * Assert that a data event item has been fired
 *  and that the data contains the item
 */
internal fun <T> LiveDataTestScope<DataEvent<T>>.assertDataEventMatches(predicate: (item: T) -> Boolean) {
    assertEventFired()
    assertTrue(predicate(latestValue!!.data), "Item emitted does not match the predicate")
}

//endregion

//region LiveDataTestScope<List<T>>

/**
 * Assert that given the subject is a list that only one list has been
 *  emitted and the list contains the following item
 */
internal fun <T> LiveDataTestScope<List<T>>.assertListContainsItems(vararg item: T) {
    assertListContainsItems(item.toList())
}

/**
 * Assert that given the subject is a list that only one list has been
 *  emitted and the list contains the following item
 */
internal fun <T> LiveDataTestScope<List<T>>.assertListContainsItems(item: List<T>) {
    item.forEach {
        assertListContainsItem(it)
    }
}

/**
 * Assert that given the subject is a list that only one list has been
 *  emitted and the list contains the following item
 */
internal fun <T> LiveDataTestScope<List<T>>.assertListContainsItem(item: T) {
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
internal fun <T> LiveDataTestScope<List<T>>.assertListMatchesItem(predicate: (item: T) -> Boolean) {
    assertNotNull(latestValue)
    latestValue!!.forEach {
        if (predicate(it)) return
    }
    assertFalse(true, "List does not contain an item that matches the predicate - (${latestValue!!.size} items)")
}

/**
 * Assert that given the subject is a list that only one list has been
 *  emitted and the list contains the following item
 */
internal fun <T> LiveDataTestScope<List<T>>.assertListExcludesItem(item: T) {
    assertNotNull(latestValue)
    latestValue!!.forEach {
        if (it != item) return
    }
    assertFalse(true, "List contains an item that matches the predicate when exclusion is required - $item (${latestValue!!.size} items)")
}

/**
 * Assert that the latest value emitted contains 0 items
 */
internal fun <T> LiveDataTestScope<List<T>>.assertListNotEmpty() {
    assertNotNull(latestValue)
    assertTrue(latestValue!!.isNotEmpty(), "List contains 0 items")
}

//endregion