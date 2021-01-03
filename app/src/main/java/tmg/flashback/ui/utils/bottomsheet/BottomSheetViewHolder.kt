package tmg.flashback.ui.utils.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_bottom_sheet_item.view.*
import tmg.flashback.R
import tmg.flashback.ui.utils.Selected

class BottomSheetViewHolder(
    itemView: View,
    val menuItemClicked: (menuItem: BottomSheetItem) -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private lateinit var currentItem: BottomSheetItem

    init {
        itemView.container.setOnClickListener(this)
    }

    fun bind(selected: Selected<BottomSheetItem>) {
        this.currentItem = selected.value
        itemView.apply {
            menuItemIcon.setImageResource(selected.value.image)
            menuItemLabel.setText(selected.value.text)

            if (selected.isSelected) {
                container.setBackgroundResource(R.drawable.background_selected)
            }
            else {
                container.setBackgroundResource(0)
            }
        }
    }

    override fun onClick(p0: View?) {
        menuItemClicked(currentItem)
    }
}