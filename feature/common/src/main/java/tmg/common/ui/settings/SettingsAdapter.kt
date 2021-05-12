package tmg.common.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import tmg.common.R
import tmg.core.ui.databinding.ViewSettingsCategoryBinding
import tmg.core.ui.databinding.ViewSettingsPreferenceBinding
import tmg.core.ui.databinding.ViewSettingsPreferenceSwitchBinding
import java.lang.RuntimeException

class SettingsAdapter<T: Fragment>(
    private val getFragmentContext: () -> T
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<SettingsModel<T>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_settings_category -> HeaderViewHolder<T>(
                ViewSettingsCategoryBinding.inflate(layoutInflater, parent, false),
                getFragmentContext
            )
            R.layout.view_settings_preference -> PreferenceViewHolder<T>(
                ViewSettingsPreferenceBinding.inflate(layoutInflater, parent, false),
                getFragmentContext
            )
            R.layout.view_settings_preference_switch -> SwitchViewHolder<T>(
                ViewSettingsPreferenceSwitchBinding.inflate(layoutInflater, parent, false),
                getFragmentContext
            )
            else -> throw RuntimeException("View type is not supported!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SettingsModel.Header -> (holder as HeaderViewHolder<T>).bind(item)
            is SettingsModel.Pref -> (holder as PreferenceViewHolder<T>).bind(item)
            is SettingsModel.SwitchPref -> (holder as SwitchViewHolder<T>).bind(item)
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].layoutId
}