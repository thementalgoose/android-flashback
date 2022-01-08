package tmg.flashback.ui.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.ui.databinding.ViewBottomSheetItemBinding
import tmg.utilities.models.Selected

class BottomSheetAdapter(
    private val itemClicked: (menuItem: BottomSheetItem) -> Unit
): RecyclerView.Adapter<BottomSheetViewHolder>() {

    var list: List<Selected<BottomSheetItem>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return BottomSheetViewHolder(
            ViewBottomSheetItemBinding.inflate(layoutInflater, parent, false),
            itemClicked
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}