package tmg.flashback.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.utilities.lifecycle.DataEvent

fun <T> Flow<T>.toDataEvent(): Flow<DataEvent<T>> {
    return this.map { DataEvent(it) }
}