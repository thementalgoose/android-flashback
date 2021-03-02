package tmg.flashback.ui.settings.widgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.core.ui.BaseViewModel
import tmg.utilities.lifecycle.Event

//region Inputs

interface SettingsWidgetViewModelInputs {
    fun preferenceClicked(pref: String?, value: Boolean?)
}

//endregion

//region Outputs

interface SettingsWidgetViewModelOutputs {
    val settings: LiveData<List<AppPreferencesItem>>

    val refreshWidget: LiveData<Event>
}

//endregion

class SettingsWidgetViewModel: BaseViewModel(), SettingsWidgetViewModelInputs,
    SettingsWidgetViewModelOutputs {

    private val keyRefreshWidget: String = "Widgets"

    var inputs: SettingsWidgetViewModelInputs = this
    var outputs: SettingsWidgetViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    override val refreshWidget: MutableLiveData<Event> = MutableLiveData()

    init {
        settings.value = mutableListOf<AppPreferencesItem>().apply {
            add(AppPreferencesItem.Category(R.string.settings_widgets))
            add(AppPreferencesItem.Preference("Widgets", R.string.settings_widgets_update_all_title, R.string.settings_widgets_update_all_description))
        }
    }

    //region Inputs

    override fun preferenceClicked(pref: String?, value: Boolean?) {
        when (pref) {
            keyRefreshWidget -> refreshWidget.value = Event()
        }
    }

    //endregion
}
