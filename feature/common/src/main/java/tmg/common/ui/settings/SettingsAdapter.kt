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

class SettingsAdapter(
    private val clickSwitch: (model: SettingsModel.SwitchPref, toNewState: Boolean) -> Unit,
    private val clickPref: (model: SettingsModel.Pref) -> Unit,
    private val getState: (model: SettingsModel.SwitchPref) -> Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<SettingsModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.view_settings_category -> HeaderViewHolder(
                ViewSettingsCategoryBinding.inflate(layoutInflater, parent, false)
            )
            R.layout.view_settings_preference -> PreferenceViewHolder(
                ViewSettingsPreferenceBinding.inflate(layoutInflater, parent, false),
                clickPref
            )
            R.layout.view_settings_preference_switch -> SwitchViewHolder(
                ViewSettingsPreferenceSwitchBinding.inflate(layoutInflater, parent, false),
                clickSwitch,
                getState
            )
            else -> throw RuntimeException("View type is not supported!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SettingsModel.Header -> (holder as HeaderViewHolder).bind(item)
            is SettingsModel.Pref -> (holder as PreferenceViewHolder).bind(item)
            is SettingsModel.SwitchPref -> (holder as SwitchViewHolder).bind(item)
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].layoutId
}