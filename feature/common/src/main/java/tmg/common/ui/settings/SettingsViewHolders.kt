package tmg.common.ui.settings

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import tmg.core.ui.databinding.ViewSettingsCategoryBinding
import tmg.core.ui.databinding.ViewSettingsPreferenceBinding
import tmg.core.ui.databinding.ViewSettingsPreferenceSwitchBinding

class HeaderViewHolder<T: Fragment>(
    private val binding: ViewSettingsCategoryBinding,
    private val getFragmentContext: () -> T
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingsModel.Header<T>) {
        binding.tvTitle.setText(item.title)
    }
}

class PreferenceViewHolder<T: Fragment>(
    private val binding: ViewSettingsPreferenceBinding,
    private val getFragmentContext: () -> T
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    lateinit var model: SettingsModel.Pref<T>

    init {
        binding.clPref.setOnClickListener(this)
    }

    fun bind(item: SettingsModel.Pref<T>) {
        this.model = item
        binding.tvTitle.setText(item.title)
        binding.tvDescription.setText(item.description)
    }

    override fun onClick(p0: View?) {
        model.onClick?.invoke(getFragmentContext())
    }
}

class SwitchViewHolder<T: Fragment>(
    private val binding: ViewSettingsPreferenceSwitchBinding,
    private val getFragmentContext: () -> T
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    lateinit var model: SettingsModel.SwitchPref<T>

    init {
        binding.clCheckbox.setOnClickListener(this)
    }

    fun bind(item: SettingsModel.SwitchPref<T>) {
        this.model = item
        binding.tvTitle.setText(item.title)
        binding.tvDescription.setText(item.description)
        binding.checkbox.isChecked = item.getState.invoke(getFragmentContext())
    }

    override fun onClick(p0: View?) {
        val newState = !binding.checkbox.isChecked
        model.saveState(getFragmentContext(), newState)
        binding.checkbox.isChecked = newState
    }
}