package tmg.flashback.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T: ViewBinding>: Fragment() {

    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateView(inflater)
        return binding.root
    }

    abstract fun inflateView(inflater: LayoutInflater): T

    fun setInsets(callback: (insets: WindowInsetsCompat) -> Unit) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            callback(insets)
            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}