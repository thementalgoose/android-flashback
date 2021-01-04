package tmg.flashback.ui.utils.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_bottom_sheet_item.view.*
import tmg.flashback.R
import tmg.flashback.ui.utils.Selected
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show

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
            if (selected.value.image != null) {
                menuItemIcon.show()
                menuItemIcon.setImageResource(selected.value.image)
            }
            else {
                menuItemIcon.gone()
            }

            menuItemLabel.text = selected.value.text.resolve(context)

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