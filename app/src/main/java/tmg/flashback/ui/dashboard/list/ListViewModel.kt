package tmg.flashback.ui.dashboard.list

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import kotlinx.coroutines.flow.combine
import tmg.flashback.DebugController
import tmg.flashback.R
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.model.NightMode
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.extensions.isInNightMode
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface ListViewModelInputs {
    fun refresh()
    fun clickRss()

    fun toggleHeader(header: HeaderType, to: Boolean? = null)
    fun toggleFavourite(season: Int)
    fun clickSeason(season: Int)

    fun toggleDarkMode()

    fun clickClearDefaultSeason()
    fun clickSetDefaultSeason(season: Int)

    fun clickSettings()
    fun clickContact()

    fun clickFeatureBanner(banner: ListItem.FeatureBanner)
}

//endregion

//region Outputs

interface ListViewModelOutputs {
    val list: LiveData<List<ListItem>>
    val showSeasonEvent: LiveData<DataEvent<Int>>

    val defaultSeasonUpdated: LiveData<DataEvent<Int?>>

    val openSettings: LiveData<Event>
    val openRss: LiveData<Event>
    val openContact: LiveData<Event>

    val themeUpdated: LiveData<DataEvent<NightMode>>

    val openNotificationsOnboarding: LiveData<Event>
}

//endregion

class ListViewModel(
    private val homeController: HomeController,
    private val rssController: RSSController,
    private val debugController: DebugController,
    private val adsController: AdsController,
    private val themeController: ThemeController,
    private val scheduleController: ScheduleController
) : ViewModel(), ListViewModelInputs, ListViewModelOutputs {

    private var selectionHeaderFavouited: MutableLiveData<Boolean> = MutableLiveData(homeController.favouritesExpanded)
    private var selectionHeaderAll: MutableLiveData<Boolean> = MutableLiveData(homeController.allExpanded)
    private var currentSeason: MutableLiveData<Int> = MutableLiveData(homeController.defaultSeason)
    private val refreshList: MutableLiveData<Event> = MutableLiveData(Event())

    override val themeUpdated: MutableLiveData<DataEvent<NightMode>> = MutableLiveData()

    override val list: LiveData<List<ListItem>> = combine(
        currentSeason.asFlow(),
        selectionHeaderFavouited.asFlow(),
        selectionHeaderAll.asFlow(),
        refreshList.asFlow()
    ) { currentSeason, favouriteExpanded, allExpanded, _ ->

        val list: MutableList<ListItem> = mutableListOf(ListItem.Hero)

        // Extra buttons
        val buttonsList = mutableListOf<ListItem.Button>().apply {
            if (rssController.enabled) {
                add(
                    ListItem.Button(
                        itemId = "rss",
                        label = R.string.dashboard_season_list_extra_rss_title,
                        icon = R.drawable.nav_rss
                    )
                )
            }
            add(
                ListItem.Button(
                    itemId = "settings",
                    label = R.string.dashboard_season_list_extra_settings_title,
                    icon = R.drawable.nav_settings
                )
            )
            add(
                ListItem.Button(
                    itemId = "contact",
                    label = R.string.dashboard_season_list_extra_contact_title,
                    icon = R.drawable.ic_contact
                )
            )
            debugController.listItem?.let {
                add(it)
            }
        }

        if (buttonsList.isNotEmpty()) {
            list.add(ListItem.Divider)
            list.add(ListItem.Header(type = HeaderType.LINKS, expanded = null))
            list.addAll(buttonsList)
            list.add(ListItem.Divider)
            list.add(ListItem.Switch(
                itemId = "dark_mode",
                label = R.string.dashboard_season_list_extra_dark_mode_title,
                icon = R.drawable.ic_nightmode_dark,
                isChecked = !themeController.isDayMode
            ))
        }

        // Notifications
        if (scheduleController.shouldShowNotificationOnboarding) {
            list.add(ListItem.FeatureBanner.EnrolNotifications)
        }

        // Adverts
        if (adsController.advertConfig.onHomeScreen) {
            list.add(ListItem.Advert)
        }

        // Season breakdown
        val supportedSeasons = homeController.supportedSeasons
        val defaultSeason = homeController.defaultSeason
        val isUserDefinedValueSet = homeController.isUserDefinedValueSet
        val favouritedSeasons = homeController.favouriteSeasons

        // Favourites
        list.add(ListItem.Divider)
        list.add(
            ListItem.Header(
                HeaderType.FAVOURITED,
                favouriteExpanded
            )
        )
        if (favouriteExpanded) {
            list.addAll(favouritedSeasons
                .toList()
                .filter { supportedSeasons.contains(it) }
                .sortedByDescending { it }
                .map {
                    ListItem.Season(
                        season = it,
                        isFavourited = true,
                        fixed = HeaderType.FAVOURITED,
                        selected = currentSeason == it,
                        default = defaultSeason == it,
                        showClearDefault = isUserDefinedValueSet
                    )
                }
            )
        }

        // All
        list.add(ListItem.Header(HeaderType.ALL, allExpanded))
        if (allExpanded) {
            list.addAll(supportedSeasons
                .map {
                    ListItem.Season(
                        season = it,
                        isFavourited = favouritedSeasons.contains(it),
                        fixed = HeaderType.ALL,
                        selected = currentSeason == it,
                        default = defaultSeason == it,
                        showClearDefault = isUserDefinedValueSet
                    )
                }
                .sortedByDescending { it.season }
            )
        }

        return@combine list
    }
        .asLiveData(viewModelScope.coroutineContext)

    override val showSeasonEvent: MutableLiveData<DataEvent<Int>> = MutableLiveData()
    override val openSettings: MutableLiveData<Event> = MutableLiveData()
    override val openRss: MutableLiveData<Event> = MutableLiveData()
    override val openContact: MutableLiveData<Event> = MutableLiveData()
    override val defaultSeasonUpdated: MutableLiveData<DataEvent<Int?>> = MutableLiveData()

    override val openNotificationsOnboarding: MutableLiveData<Event> = MutableLiveData()

    var inputs: ListViewModelInputs = this
    var outputs: ListViewModelOutputs = this

    //region Inputs

    override fun refresh() {
        refreshList.postValue(Event())
    }

    override fun clickRss() {
        openRss.value = Event()
    }

    override fun toggleDarkMode() {
        themeController.nightMode = when (themeController.isDayMode) {
            true -> NightMode.NIGHT
            false -> NightMode.DAY
        }
        refreshList.postValue(Event())
    }

    override fun toggleHeader(header: HeaderType, to: Boolean?) {
        when (header) {
            HeaderType.FAVOURITED -> {
                selectionHeaderFavouited.value = to ?: (selectionHeaderFavouited.value != true)
            }
            HeaderType.ALL -> {
                selectionHeaderAll.value = to ?: (selectionHeaderAll.value != true)
            }
            HeaderType.LINKS -> { /* Do nothing */ }
        }
    }

    override fun toggleFavourite(season: Int) {
        if (homeController.isFavourite(season)) {
            homeController.removeFavourite(season)
        } else {
            homeController.addFavourite(season)
        }
        refreshList.postValue(Event())
    }

    override fun clickSeason(season: Int) {
        showSeasonEvent.value = DataEvent(season)
        currentSeason.postValue(season)
    }

    override fun clickSetDefaultSeason(season: Int) {
        homeController.setUserDefaultSeason(season)
        defaultSeasonUpdated.value = DataEvent(season)
        refreshList.postValue(Event())
    }

    override fun clickClearDefaultSeason() {
        homeController.clearDefault()
        defaultSeasonUpdated.value = DataEvent(null)
        refreshList.postValue(Event())
    }

    override fun clickSettings() {
        openSettings.value = Event()
    }

    override fun clickContact() {
        openContact.value = Event()
    }

    override fun clickFeatureBanner(banner: ListItem.FeatureBanner) {
        when (banner) {
            ListItem.FeatureBanner.EnrolNotifications -> {
                openNotificationsOnboarding.value = Event()
                scheduleController.seenOnboarding()
                refresh()
            }
        }
    }

    //endregion
}