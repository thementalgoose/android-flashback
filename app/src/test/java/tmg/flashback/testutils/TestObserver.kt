package tmg.flashback.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.jupiter.api.Assertions.*
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

fun <T> LiveData<T>.test(): TestObserver<T> {
    return TestObserver(this)
}

/**
 * Observer class.
 * Holds all values emitted from live data to assert against
 */
class TestObserver<T>(liveData: LiveData<T>): Observer<T> {

    val listOfValues: MutableList<T> = mutableListOf()

    init {
        liveData.observeForever(this)
    }

    override fun onChanged(t: T) {
        listOfValues.add(t)
    }

    fun latestValue(): T? {
        return listOfValues.lastOrNull()
    }

    fun values(): List<T> {
        return listOfValues
    }

    fun assertQuantityOf(number: Int, predicate: (item: T) -> Boolean = { true }) {
        val items = listOfValues.count(predicate)
        assertEquals(number, items, "Expected $number Actual $items - Items have been emitted $listOfValues")
    }

    fun assertAtLeast(number: Int, predicate: (item: T) -> Boolean = { true }) {
        val items = listOfValues.count(predicate)
        assertTrue(number >= items, "Expected at least $number Actual $items - Items have been emitted $listOfValues")
    }

    fun assertContains(predicate: (item: T) -> Boolean) {
        val item = listOfValues.lastOrNull(predicate)
        assertNotNull(item, "Cannot find item in the list $listOfValues that matches the predicate provided")
    }

    fun assertValueIs(value: T) {
        assertEquals(1, listOfValues.size)
        val item = latestValue()!!
        assertEquals(value, item)
    }

    fun assertLatestValueIs(value: T) {
        val item = latestValue()!!
        assertEquals(value, item)
    }
}

private fun <T> LiveData<List<T>>.assertListContainItem(predicate: (item: T) -> Boolean) {
    val list = this.test().listOfValues.lastOrNull()!!
    val item = list.firstOrNull(predicate)
    assertNotNull(item, "No list has been emitted by this live data that contains the item specified by the predicate")
}

private fun <T> LiveData<List<T>>.assertListNotContainItem(predicate: (item: T) -> Boolean) {
    val list = this.test().listOfValues.lastOrNull()!!
    val item = list.firstOrNull(predicate)
    assertNull(item, "Item has been found in the list. Item found $item")
}

private fun <T> LiveData<List<T>>.assertLatestValueContains(valuesToFind: List<T>) {
    val list = this.test().listOfValues.lastOrNull()!!

    val indexes = valuesToFind
            .map {
                val index = list.indexOf(it)
                if (index == -1) {
                    throw Exception("Item $it cannot be found in the list of values returned")
                }
                return@map index
            }

    val sorted = indexes.sorted()
    assertEquals(sorted, indexes, "Items didn't appear in the order specified. Expected $sorted Actual $indexes")

    // Final existance check
//    for (x in valuesToFind) {
//        assertTrue(list.contains(x), "List does not contain one of the specified items. Item that couldn't be found $x")
//    }
}

fun <T: Event> assertEventNotFired(liveData: LiveData<T>) {
    liveData.test().assertQuantityOf(0)
}

fun <T: Event> assertEventFired(liveData: LiveData<T>, count: Int = 1) {
    liveData.test().assertQuantityOf(count)
}

fun <T> assertValues(liveData: LiveData<T>, vararg values: T) {
    val list = liveData.test().listOfValues
    assertEquals(values.size, list.size, "Values mismatch - Expected $values Actual $list")
    for (x in list.indices) {
        assertEquals(values[x], list[x], "Items as position $x did not match: Expected ${values[x]} Actual ${list[x]}")
    }
}

fun <T> assertValues(values: List<T>, liveData: LiveData<T>) {
    val list = liveData.test().listOfValues
    assertEquals(values.size, list.size, "Values mismatch - Expected $values Actual $list")
    for (x in list.indices) {
        assertEquals(values[x], list[x], "Items as position $x did not match: Expected ${values[x]} Actual ${list[x]}")
    }
}

fun <T> assertValue(expected: T, liveData: LiveData<T>) {
    val test = liveData.test()
    if (test.listOfValues.size == 1) {
        test.assertValueIs(expected)
    }
    else {
        test.assertContains {
            it == expected
        }
    }
}

fun <T> assertLatestValue(expected: T, liveData: LiveData<T>) {
    val test = liveData.test()
    if (test.listOfValues.isNotEmpty()) {
        test.assertLatestValueIs(expected)
    }
}

fun <T> assertDataEventValue(expected: T, liveData: LiveData<DataEvent<T>>) {
    liveData.test().assertContains {
        it.data == expected
    }
}

fun <T> assertDataEventValue(liveData: LiveData<DataEvent<T>>, predicate: (item: T) -> Boolean) {
    liveData.test().assertContains {
        predicate(it.data)
    }
}

fun <T> assertHasItems(liveData: LiveData<T>) {
    liveData.test().assertAtLeast(1)
}

fun <T> assertListContains(liveData: LiveData<List<T>>, predicate: (item: T) -> Boolean) {
    liveData.assertListContainItem(predicate)
}
fun <T> assertListNotContains(liveData: LiveData<List<T>>, predicate: (item: T) -> Boolean) {
    liveData.assertListNotContainItem(predicate)
}
fun <T> assertListContainsValues(liveData: LiveData<List<T>>, list: List<T>) {
    liveData.assertLatestValueContains(list)
}