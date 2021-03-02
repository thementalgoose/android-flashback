package tmg.flashback.statistics.ui.shared.sync

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.viewholders.*

@KoinApiExtension
abstract class SyncAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

    private val navigationManager: NavigationManager by inject()
    private val configurationController: ConfigurationController by inject()

    abstract var list: List<T>

    abstract fun viewType(position: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_shared_data_unavailable -> DataUnavailableViewHolder(view)
            R.layout.view_shared_no_network -> NoNetworkViewHolder(view)
            R.layout.view_shared_internal_error -> InternalErrorOccurredViewHolder(view)
            R.layout.view_shared_message -> MessageViewHolder(view)
            R.layout.view_shared_constructor_championship_not_awarded -> ConstructorsChampionshipNotAwardedViewHolder(view)
            R.layout.view_shared_provided -> ProvidedByViewHolder(navigationManager, view)
            else -> throw Error("${this.javaClass.simpleName} Does not have a supported layout id to create a viewholder by")
        }
    }

    fun bindErrors(holder: RecyclerView.ViewHolder, item: SyncDataItem) {
        when (item) {
            is SyncDataItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
            is SyncDataItem.Message -> (holder as MessageViewHolder).bind(item.msg)
            is SyncDataItem.MessageRes -> (holder as MessageViewHolder).bind(item.msg, item.values)
            is SyncDataItem.ProvidedBy -> (holder as ProvidedByViewHolder).bind(configurationController.dataProvidedBy)
            else -> { }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = viewType(position)

}