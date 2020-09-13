package tmg.flashback.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.jupiter.api.Assertions.*
import tmg.utilities.lifecycle.Event

fun <T> LiveData<T>.test(): TestObserver<T> {
    return TestObserver(this)
}

/**
 * Observer class.
 * Holds all values emitted from live data to assert against
 */
class TestObserver<T>(liveData: LiveData<T>): Observer<T> {

    private val listOfValues: MutableList<T> = mutableListOf()

    init {
        liveData.observeForever(this)
    }

    override fun onChanged(t: T) {
        listOfValues.add(t)
    }

    fun latestValue(): T? { return listOfValues.lastOrNull() }
}

fun <T: Event> TestObserver<T>.assertEventFired() {
    assertTrue(latestValue() is Event)
}

fun <T: Event> TestObserver<T>.assertEventNotFired() {
    assertFalse(latestValue() is Event)
}

fun <T> TestObserver<List<T>>.assertListContains(predicate: (item: T) -> Boolean) {
    val list = latestValue() ?: emptyList()
    val firstItemToFind = list.firstOrNull(predicate)
    assertNotNull(firstItemToFind)
}