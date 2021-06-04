package tmg.core.ui.viewbinding

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

interface ViewInflater<T: ViewBinding> {
    fun inflate(layoutInflater: LayoutInflater): T
}