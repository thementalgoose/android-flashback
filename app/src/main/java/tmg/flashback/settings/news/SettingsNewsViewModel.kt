package tmg.flashback.settings.news

import android.content.Context
import androidx.lifecycle.MutableLiveData
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.extensions.description
import tmg.flashback.extensions.title
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.NewsSource

//region Inputs

interface SettingsNewsViewModelInputs {
    fun updateNewsSourcePref(pref: NewsSource, toNewState: Boolean)
    fun updatePref(pref: String, toNewState: Boolean)
}

//endregion

//region Outputs

interface SettingsNewsViewModelOutputs {
    val settings: MutableLiveData<List<AppPreferencesItem>>
}

//endregion

class SettingsNewsViewModel(
    private val prefDB: PrefsDB,
    private val isLive: Boolean,
    private val applicationContext: Context
): BaseViewModel(), SettingsNewsViewModelInputs, SettingsNewsViewModelOutputs {

    private val keyShowDescription: String = "keyShowDescription"
    private val keyJavascript: String = "keyJavascript"

    var inputs: SettingsNewsViewModelInputs = this
    var outputs: SettingsNewsViewModelOutputs = this

    override val settings: MutableLiveData<List<AppPreferencesItem>> = MutableLiveData()

    init {
        settings.value = populateList()
    }

    //region Inputs

    override fun updateNewsSourcePref(pref: NewsSource, toNewState: Boolean) {
        val source = prefDB.newsSourceExcludeList.toMutableSet()
        if (toNewState) {
            // true = remove from exclude list
            source.remove(pref)
        } else {
            // false = add to exclude list
            source.add(pref)
        }
        prefDB.newsSourceExcludeList = source
    }

    override fun updatePref(pref: String, toNewState: Boolean) {
        when (pref) {
            keyShowDescription -> prefDB.newsShowDescription = toNewState
            keyJavascript -> prefDB.inAppEnableJavascript = toNewState
        }
    }

    //endregion

    private fun populateList(): List<AppPreferencesItem> {

        val sources = NewsSource.values()
            .filter { !isLive || NewsSource.SKY_SPORTS != it }
            .map { AppPreferencesItem.SwitchPreference(
                prefKey = it.key,
                title = applicationContext.getString(it.title),
                description = applicationContext.getString(it.description),
                isChecked = !prefDB.newsSourceExcludeList.contains(it))
            }

        val list: MutableList<AppPreferencesItem> = mutableListOf()
        list.add(AppPreferencesItem.Category(applicationContext.getString(R.string.settings_news_appearance_title)))
        list.add(AppPreferencesItem.SwitchPreference(
            keyShowDescription,
            applicationContext.getString(R.string.settings_news_show_description_title),
            applicationContext.getString(R.string.settings_news_show_description_description),
            prefDB.newsShowDescription
        ))
        list.add(AppPreferencesItem.Category(applicationContext.getString(R.string.settings_news_sources)))
        list.addAll(sources)
        list.add(AppPreferencesItem.Category(applicationContext.getString(R.string.settings_news_browser)))
        list.add(AppPreferencesItem.SwitchPreference(
            keyJavascript,
            applicationContext.getString(R.string.settings_news_browser_javascript_title),
            applicationContext.getString(R.string.settings_news_browser_javascript_description),
            prefDB.inAppEnableJavascript
        ))
        return list
    }
}