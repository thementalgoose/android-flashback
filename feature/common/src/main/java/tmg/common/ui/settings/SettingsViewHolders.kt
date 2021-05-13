package tmg.common.ui.settings

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import tmg.core.ui.databinding.ViewSettingsCategoryBinding
import tmg.core.ui.databinding.ViewSettingsPreferenceBinding
import tmg.core.ui.databinding.ViewSettingsPreferenceSwitchBinding

class HeaderViewHolder(
    private val binding: ViewSettingsCategoryBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingsModel.Header) {
        binding.tvTitle.setText(item.title)
    }
}

class PreferenceViewHolder(
    private val binding: ViewSettingsPreferenceBinding,
    private val clickPref: (model: SettingsModel.Pref) -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    lateinit var model: SettingsModel.Pref

    init {
        binding.clPref.setOnClickListener(this)
    }

    fun bind(item: SettingsModel.Pref) {
        this.model = item
        binding.tvTitle.setText(item.title)
        binding.tvDescription.setText(item.description)
    }

    override fun onClick(p0: View?) {
        clickPref(model)
    }
}

class SwitchViewHolder(
    private val binding: ViewSettingsPreferenceSwitchBinding,
    private val clickSwitch: (model: SettingsModel.SwitchPref, toNewState: Boolean) -> Unit,
    private val getState: (model: SettingsModel.SwitchPref) -> Boolean
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    lateinit var model: SettingsModel.SwitchPref

    init {
        binding.clCheckbox.setOnClickListener(this)
    }

    fun bind(item: SettingsModel.SwitchPref) {
        this.model = item
        binding.tvTitle.setText(item.title)
        binding.tvDescription.setText(item.description)
        binding.checkbox.isChecked = getState(item)
    }

    override fun onClick(p0: View?) {
        clickSwitch(model, !binding.checkbox.isChecked)
    }
}