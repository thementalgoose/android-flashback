package tmg.flashback.ui.utils.popup

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes

fun View.setOnClickPopupMenu(@MenuRes menuRes: Int, itemClicked: (id: Int) -> Boolean) {
    this.setOnClickListener { view ->
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(menuRes)
        popupMenu.setOnMenuItemClickListener {
            itemClicked(it.itemId)
        }
        popupMenu.show()
    }
}

fun Context.popupMenu(anchorView: View, @MenuRes menuRes: Int, itemClicked: (id: Int) -> Boolean) {
    val popupMenu = PopupMenu(anchorView.context, anchorView)
    popupMenu.inflate(menuRes)
    popupMenu.setOnMenuItemClickListener {
        itemClicked(it.itemId)
    }
    popupMenu.show()
}