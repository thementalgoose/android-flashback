package tmg.core.ui.viewbinding

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T: ViewBinding>(
        private val binder: ViewBinder<T>,
): ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }

        thisRef.lifecycle.addObserver(object : DefaultLifecycleOwner {

        })
    }
}