package tmg.core.ui.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.core.ui.R
import tmg.utilities.models.Selected
import tmg.core.ui.databinding.ViewBottomSheetItemBinding
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show

class BottomSheetViewHolder(
    private val binding: ViewBottomSheetItemBinding,
    val menuItemClicked: (menuItem: BottomSheetItem) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var currentItem: BottomSheetItem

    init {
        binding.container.setOnClickListener(this)
    }

    fun bind(selected: Selected<BottomSheetItem>) {
        this.currentItem = selected.value
        binding.apply {
            if (selected.value.image != null) {
                menuItemIcon.show()
                menuItemIcon.setImageResource(selected.value.image!!)
            }
            else {
                menuItemIcon.gone()
            }

            menuItemLabel.text = selected.value.text.resolve(context)

            if (selected.isSelected) {
                container.setBackgroundResource(R.drawable.background_bottom_sheet_selected)
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