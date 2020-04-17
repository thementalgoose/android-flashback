package tmg.f1stats.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe

fun <T> AppCompatActivity.observe(liveData: LiveData<T>, action: (data: T) -> Unit) {
    liveData.observe(this, action)
}

fun <T> AppCompatActivity.observeEvent(liveData: LiveData<DataEvent<T>>, action: (data: T) -> Unit) {
    liveData.observe(this) {
        if (it.processEvent) {
            action(it.data)
        }
    }
}

fun AppCompatActivity.observeEvent(liveData: LiveData<Event>, action: () -> Unit) {
    liveData.observe(this) {
        if (it.processEvent) {
            action()
        }
    }
}

fun <T> Fragment.observe(liveData: LiveData<T>, action: (data: T) -> Unit) {
    liveData.observe(viewLifecycleOwner, action)
}

fun <T> Fragment.observeEvent(liveData: LiveData<DataEvent<T>>, action: (data: T) -> Unit) {
    observe(liveData) {
        if (it.processEvent) {
            action(it.data)
        }
    }
}

fun Fragment.observeEvent(liveData: LiveData<Event>, action: () -> Unit) {
    observe(liveData) {
        if (it.processEvent) {
            action()
        }
    }
}

data class DataEvent<T>(val data: T): Event()

open class Event {
    var processEvent: Boolean = true
        private set
        get() {
            val current = field
            processEvent = false
            return current
        }
}