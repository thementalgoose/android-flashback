package tmg.flashback.ui.shared.sync

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.KoinComponent
import org.koin.core.inject
import tmg.flashback.R
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.ui.shared.viewholders.*

abstract class SyncAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

    private val prefsRepo: UserRepository by inject()
    private val remoteConfigRepo: RemoteConfigRepository by inject()

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
            R.layout.view_shared_provided -> ProvidedByViewHolder(view)
            else -> throw Error("${this.javaClass.simpleName} Does not have a supported layout id to create a viewholder by")
        }
    }

    fun bindErrors(holder: RecyclerView.ViewHolder, item: SyncDataItem) {
        when (item) {
            is SyncDataItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
            is SyncDataItem.Message -> (holder as MessageViewHolder).bind(item.msg)
            is SyncDataItem.MessageRes -> (holder as MessageViewHolder).bind(item.msg, item.values)
            is SyncDataItem.ProvidedBy -> (holder as ProvidedByViewHolder).bind(remoteConfigRepo.dataProvidedBy, prefsRepo.theme)
            else -> {
            }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = viewType(position)

}