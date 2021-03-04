package tmg.flashback.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<T: ViewBinding>: BottomSheetDialogFragment() {

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateView(inflater)
        return binding.root
    }

    abstract fun inflateView(inflater: LayoutInflater): T

    val behavior: BottomSheetBehavior<*>
        get() {
            val dialog = dialog as BottomSheetDialog
            return dialog.behavior
        }
}