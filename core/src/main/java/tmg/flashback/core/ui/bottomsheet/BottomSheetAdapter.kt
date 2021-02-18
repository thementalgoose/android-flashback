package tmg.flashback.core.ui.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.core.R
import tmg.flashback.core.utils.Selected

class BottomSheetAdapter(
    private val itemClicked: (menuItem: BottomSheetItem) -> Unit
): RecyclerView.Adapter<BottomSheetViewHolder>() {

    var list: List<Selected<BottomSheetItem>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_bottom_sheet_item, parent, false),
            itemClicked
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}