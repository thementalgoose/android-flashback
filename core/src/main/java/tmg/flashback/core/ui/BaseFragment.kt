package tmg.flashback.core.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tmg.utilities.lifecycle.common.CommonFragment

abstract class BaseFragment: CommonFragment() {

    private lateinit var viewRef: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.viewRef = view
        super.onViewCreated(view, savedInstanceState)
    }

    fun setInsets(callback: (insets: WindowInsetsCompat) -> Unit) {
        ViewCompat.setOnApplyWindowInsetsListener(viewRef) { view, insets ->
            callback(insets)
            insets
        }
    }
}