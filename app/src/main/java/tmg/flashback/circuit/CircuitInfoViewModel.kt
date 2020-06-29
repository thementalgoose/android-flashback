package tmg.flashback.circuit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.base.BaseViewModel
import tmg.flashback.circuit.list.CircuitItem
import tmg.flashback.news.NewsItem
import tmg.flashback.repo.db.stats.CircuitDB
import tmg.flashback.repo.enums.NewsSource
import tmg.flashback.repo.models.stats.CircuitRace
import tmg.flashback.repo.models.stats.CircuitSummary
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.viewholders.DataUnavailable

//region Inputs

interface CircuitInfoViewModelInputs {
    fun circuitId(circuitId: String)
}

//endregion

//region Outputs

interface CircuitInfoViewModelOutputs {
    val list: LiveData<List<CircuitItem>>
}

//endregion

class CircuitInfoViewModel(
    private val circuitDB: CircuitDB,
    private val connectivityManager: ConnectivityManager
): BaseViewModel(), CircuitInfoViewModelInputs, CircuitInfoViewModelOutputs {

    private lateinit var circuitId: String

    private val circuitIdentifier: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()

    override val list: LiveData<List<CircuitItem>> = circuitIdentifier
        .asFlow()
        .flatMapLatest { circuitDB.getCircuit(it) }
        .map {
            when {
                it == null && !connectivityManager.isConnected -> {
                    return@map listOf<CircuitItem>(CircuitItem.NoNetwork)
                }
                it == null -> {
                    return@map listOf<CircuitItem>(CircuitItem.InternalError)
                }
                else -> {
                    val list = mutableListOf<CircuitItem>(
                        CircuitItem.CircuitInfo(it)
                    )
                    list.addAll(it.results.map { result ->
                        CircuitItem.Race(
                            name = result.name,
                            season = result.season,
                            round = result.round,
                            date = result.date,
                            time = result.time,
                        )
                    })
                    return@map listOf<CircuitItem>()
                }
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: CircuitInfoViewModelInputs = this
    var outputs: CircuitInfoViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun circuitId(circuitId: String) {
        circuitIdentifier.offer(circuitId)
    }

    //endregion

    //region Outputs

    //endregion
}