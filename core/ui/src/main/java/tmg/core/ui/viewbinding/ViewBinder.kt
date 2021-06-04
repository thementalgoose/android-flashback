package tmg.core.ui.viewbinding

import android.view.View
import androidx.viewbinding.ViewBinding

interface ViewBinder<T: ViewBinding> {
    fun bind(view: View): T
}