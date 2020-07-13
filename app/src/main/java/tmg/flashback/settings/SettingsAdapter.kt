package tmg.flashback.settings

import android.view.View
import kotlinx.android.synthetic.main.view_settings_category.view.tvTitle
import kotlinx.android.synthetic.main.view_settings_preference.view.*
import kotlinx.android.synthetic.main.view_settings_preference.view.tvDescription
import kotlinx.android.synthetic.main.view_settings_preference_switch.view.*
import tmg.components.prefs.AppPreferencesAdapter
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R

class SettingsAdapter(
    prefClicked: (prefKey: String) -> Unit = { _ -> },
    prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { _, _ -> }
) : AppPreferencesAdapter(prefClicked, prefSwitchClicked) {


    override val categoryLayoutId: Int = R.layout.view_settings_category

    override val preferenceLayoutId: Int = R.layout.view_settings_preference

    override val preferenceSwitchLayoutId: Int = R.layout.view_settings_preference_switch

    override fun bindCategory(view: View, model: AppPreferencesItem.Category) {
        view.apply {
            this.tvTitle.text = model.title
        }
    }

    override fun bindPreference(view: View, model: AppPreferencesItem.Preference) {
        view.apply {
            this.clPref.setOnClickListener {
                prefClicked(model.prefKey)
            }
            this.tvTitle.text = model.title
            this.tvDescription.text = model.description
        }
    }

    override fun bindPreferenceSwitch(view: View, model: AppPreferencesItem.SwitchPreference) {
        view.apply {
            this.tvTitle.text = model.title
            this.tvDescription.text = model.description
            this.checkbox.isChecked = model.isChecked

            this.clCheckbox.setOnClickListener {
                switchChecked(model.prefKey, !model.isChecked)
            }
        }
    }
}