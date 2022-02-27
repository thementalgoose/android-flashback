package tmg.flashback.ui.settings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import tmg.flashback.ui.R
import tmg.flashback.ui.databinding.ViewSettingsCategoryBinding
import tmg.flashback.ui.databinding.ViewSettingsPreferenceBinding
import tmg.flashback.ui.databinding.ViewSettingsPreferenceSwitchBinding
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

class HeaderViewHolder(
    private val binding: ViewSettingsCategoryBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingsModel.Header) {
        binding.tvTitle.setText(item.title)
        binding.experimental.show(item.beta)

        if (item.beta) {
            binding.experimental.setOnClickListener {
                Snackbar
                    .make(binding.experimental, getString(R.string.settings_experimental_description), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
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
        binding.experimental.show(item.beta)
        binding.tvTitle.setText(item.title)
        binding.tvDescription.setText(item.description)

        if (item.beta) {
            binding.experimental.setOnClickListener {
                Snackbar
                    .make(binding.experimental, getString(R.string.settings_experimental_description), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
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
        binding.experimental.show(item.beta)
        binding.tvTitle.setText(item.title)
        binding.tvDescription.setText(item.description)
        binding.checkbox.isChecked = getState(item)

        if (item.beta) {
            binding.experimental.setOnClickListener {
                Snackbar
                    .make(binding.experimental, getString(R.string.settings_experimental_description), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onClick(p0: View?) {
        clickSwitch(model, !binding.checkbox.isChecked)
    }
}