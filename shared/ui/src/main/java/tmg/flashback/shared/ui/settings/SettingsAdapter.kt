package tmg.flashback.shared.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import tmg.components.prefs.AppPreferencesAdapter
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.shared.ui.databinding.ViewSettingsCategoryBinding
import tmg.flashback.shared.ui.databinding.ViewSettingsPreferenceBinding
import tmg.flashback.shared.ui.databinding.ViewSettingsPreferenceSwitchBinding

class SettingsAdapter(
        prefClicked: (prefKey: String) -> Unit = { _ -> },
        prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { _, _ -> }
) : AppPreferencesAdapter(prefClicked, prefSwitchClicked) {

    override fun categoryLayoutId(viewGroup: ViewGroup) =
        ViewSettingsCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

    override fun preferenceLayoutId(viewGroup: ViewGroup) =
        ViewSettingsPreferenceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

    override fun preferenceSwitchLayoutId(viewGroup: ViewGroup) =
        ViewSettingsPreferenceSwitchBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

    override fun bindCategory(view: ViewBinding, model: AppPreferencesItem.Category) {
        (view as? ViewSettingsCategoryBinding)?.apply {
            this.tvTitle.setText(model.title)
        }
    }

    override fun bindPreference(view: ViewBinding, model: AppPreferencesItem.Preference) {
        (view as? ViewSettingsPreferenceBinding)?.apply {
            this.clPref.setOnClickListener {
                prefClicked(model.prefKey)
            }
            this.tvTitle.setText(model.title)
            this.tvDescription.setText(model.description)
        }
    }

    override fun bindPreferenceSwitch(view: ViewBinding, model: AppPreferencesItem.SwitchPreference) {
        (view as? ViewSettingsPreferenceSwitchBinding)?.apply {
            this.tvTitle.setText(model.title)
            this.tvDescription.setText(model.description)
            this.checkbox.isChecked = model.isChecked

            this.clCheckbox.setOnClickListener {
                switchChecked(model.prefKey, !model.isChecked)
            }
        }
    }
}