package tmg.flashback.statistics.ui.shared.sync

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.ui.navigation.NavigationProvider
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.databinding.*
import tmg.flashback.statistics.ui.shared.sync.viewholders.*

@KoinApiExtension
abstract class SyncAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

    private val navigationProvider: NavigationProvider by inject()
    private val seasonController: SeasonController by inject()

    open val providedByAtTopIndex: Int = 0

    protected fun List<T>.addDataProvidedByItem(): List<T> {
        val item = dataProvidedItem(SyncDataItem.ProvidedBy()) ?: return this

        return if (seasonController.dataProvidedByAtTop) {
            if (providedByAtTopIndex < this.size) {
                // Valid to be added at that position
                this.toMutableList()
                        .apply { add(providedByAtTopIndex, item) }
            } else {
                // Not valid. Add it at the bottom
                this.toMutableList()
                        .apply { add(item) }
            }
        } else {
            // Not valid. Add it at the bottom
            this.toMutableList().apply {
                add(item)
            }
        }
    }

    abstract fun dataProvidedItem(syncDataItem: SyncDataItem): T?

    abstract var list: List<T>

    abstract fun viewType(position: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_shared_data_unavailable -> DataUnavailableViewHolder(
                    ViewSharedDataUnavailableBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_shared_no_network -> NoNetworkViewHolder(
                    ViewSharedNoNetworkBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_shared_internal_error -> InternalErrorOccurredViewHolder(
                    ViewSharedInternalErrorBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_shared_message -> MessageViewHolder(
                    ViewSharedMessageBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_shared_constructor_championship_not_awarded -> ConstructorsChampionshipNotAwardedViewHolder(
                    ViewSharedConstructorChampionshipNotAwardedBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_shared_provided -> ProvidedByViewHolder(
                    navigationProvider,
                    ViewSharedProvidedBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_shared_skeleton -> SkeletonViewHolder(
                    ViewSharedSkeletonBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw Error("${this.javaClass.simpleName} Does not have a supported layout id to create a viewholder by")
        }
    }

    fun bindErrors(holder: RecyclerView.ViewHolder, item: SyncDataItem) {
        when (item) {
            is SyncDataItem.Unavailable -> (holder as DataUnavailableViewHolder).bind(item.type)
            is SyncDataItem.Message -> (holder as MessageViewHolder).bind(item.msg, item.clickUrl)
            is SyncDataItem.MessageRes -> (holder as MessageViewHolder).bind(item.msg, item.values, item.clickUrl)
            is SyncDataItem.ProvidedBy -> (holder as ProvidedByViewHolder).bind(seasonController.dataProvidedBy)
            else -> {
            }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = viewType(position)

}