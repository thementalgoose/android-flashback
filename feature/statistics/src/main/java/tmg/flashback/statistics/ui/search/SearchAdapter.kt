package tmg.flashback.statistics.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.*
import tmg.flashback.statistics.ui.search.viewholder.*
import tmg.flashback.statistics.ui.shared.sync.SyncAdapter
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.utilities.difflist.GenericDiffCallback

@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_OVERRIDE")
class SearchAdapter(
    private val itemClicked: (item: SearchItem) -> Unit
): SyncAdapter<SearchItem>() {

    override var list: List<SearchItem> = emptyList()
        set(initialValue) {
            val value = initialValue.addDataProvidedByItem()
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun viewType(position: Int) = list[position].layoutId

    override fun dataProvidedItem(syncDataItem: SyncDataItem) = SearchItem.ErrorItem(syncDataItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_search_constructor -> SearchConstructorViewHolder(
                ViewSearchConstructorBinding.inflate(layoutInflater, parent, false),
                itemClicked
            )
            R.layout.view_search_driver -> SearchDriverViewHolder(
                ViewSearchDriverBinding.inflate(layoutInflater, parent, false),
                itemClicked
            )
            R.layout.view_search_circuit -> SearchCircuitViewHolder(
                ViewSearchCircuitBinding.inflate(layoutInflater, parent, false),
                itemClicked
            )
            R.layout.view_search_race -> SearchRaceViewHolder(
                ViewSearchRaceBinding.inflate(layoutInflater, parent, false),
                itemClicked
            )
            R.layout.view_search_placeholder -> SearchPlaceholderViewHolder(
                ViewSearchPlaceholderBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_search_advert -> SearchAdvertViewHolder(
                ViewSearchAdvertBinding.inflate(layoutInflater, parent, false)
            )
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SearchItem.Circuit -> (holder as SearchCircuitViewHolder).bind(item)
            is SearchItem.Constructor -> (holder as SearchConstructorViewHolder).bind(item)
            is SearchItem.Driver -> (holder as SearchDriverViewHolder).bind(item)
            is SearchItem.Race -> (holder as SearchRaceViewHolder).bind(item)
            is SearchItem.ErrorItem -> bindErrors(holder, item.item)
            else -> { }
        }
    }
}