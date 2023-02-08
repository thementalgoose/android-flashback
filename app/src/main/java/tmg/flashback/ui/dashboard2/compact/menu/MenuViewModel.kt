package tmg.flashback.ui.dashboard2.compact.menu

////region Inputs
//
//interface MenuViewModelInputs {
//    fun clickSeason(season: Int)
//
//    fun clickButton(button: MenuItems.Button)
//    fun clickToggle(toggle: MenuItems.Toggle)
//    fun clickFeature(feature: MenuItems.Feature)
//}
//
////endregion
//
////region Outputs
//
//interface MenuViewModelOutputs {
//    val links: LiveData<List<MenuItems>>
//    val season: LiveData<List<MenuSeasonItem>>
//
//    val appVersion: LiveData<String>
//
//    val isSnowEasterEggEnabled: Boolean
//    val overrideMenuKey: MenuIcons?
//}
//
////endregion
//
//@HiltViewModel
//class MenuViewModel @Inject constructor(
//    private val getSeasonsUseCase: GetSeasonsUseCase,
//    private val buildConfigManager: BuildConfigManager,
//    private val permissionManager: PermissionManager,
//    private val permissionRepository: PermissionRepository,
//    private val notificationRepository: NotificationRepository,
//    private val defaultSeasonUseCase: DefaultSeasonUseCase,
//    private val changeNightModeUseCase: ChangeNightModeUseCase,
//    private val styleManager: StyleManager,
//    private val rssNavigationComponent: RssNavigationComponent,
//    private val navigationComponent: ApplicationNavigationComponent,
//    private val statsNavigationComponent: StatsNavigationComponent,
//    private val debugNavigationComponent: DebugNavigationComponent,
//    private val isSnowEnabledUseCase: IsSnowEnabledUseCase,
//    private val isMenuIconEnabledUseCase: IsMenuIconEnabledUseCase,
//) : ViewModel(), MenuViewModelInputs, MenuViewModelOutputs {
//
//    val inputs: MenuViewModelInputs = this
//    val outputs: MenuViewModelOutputs = this
//
//    private val selectedSeason: MutableStateFlow<Int> = MutableStateFlow(defaultSeasonUseCase.defaultSeason)
//
//    override val isSnowEasterEggEnabled: Boolean by lazy { isSnowEnabledUseCase() }
//    override val overrideMenuKey: MenuIcons? by lazy { isMenuIconEnabledUseCase() }
//
//    override val links: MutableLiveData<List<MenuItems>> = MutableLiveData(getLinks())
//    override val appVersion: MutableLiveData<String> = MutableLiveData(buildConfigManager.versionName)
//
//    override val season: LiveData<List<MenuSeasonItem>> = selectedSeason
//        .map { selectedSeason ->
//            getSeasonsUseCase.get(selectedSeason)
//        }
//        .asLiveData(viewModelScope.coroutineContext)
//
//    override fun clickSeason(season: Int) {
//        this.selectedSeason.value = season
//    }
//
//    override fun clickButton(button: MenuItems.Button) {
//        when (button) {
//            MenuItems.Button.Contact -> {
//                navigationComponent.aboutApp()
//            }
//            MenuItems.Button.Rss -> {
//                rssNavigationComponent.rss()
//            }
//            MenuItems.Button.Settings -> {
//                navigationComponent.settings()
//            }
//            MenuItems.Button.Search -> {
//                statsNavigationComponent.search()
//            }
//            is MenuItems.Button.Custom -> {
//                debugNavigationComponent.navigateTo(button.id)
//            }
//        }
//        links.value = getLinks()
//    }
//
//    override fun clickToggle(toggle: MenuItems.Toggle) {
//        when (toggle) {
//            is MenuItems.Toggle.DarkMode -> {
//                navigationComponent.home()
//                if (toggle.isEnabled) {
//                    changeNightModeUseCase.setNightMode(NightMode.DAY)
//                } else {
//                    changeNightModeUseCase.setNightMode(NightMode.NIGHT)
//                }
//            }
//        }
//        links.value = getLinks()
//    }
//
//    override fun clickFeature(feature: MenuItems.Feature) {
//        when (feature) {
//            MenuItems.Feature.Notifications -> {
//                statsNavigationComponent.featureNotificationOnboarding()
//                notificationRepository.seenNotificationOnboarding = true
//            }
//            MenuItems.Feature.RuntimeNotifications -> {
//                viewModelScope.launch {
//                    permissionManager
//                        .requestPermission(RationaleType.RuntimeNotifications)
//                        .invokeOnCompletion {
//                            notificationRepository.seenRuntimeNotifications = true
//                            if (permissionRepository.isRuntimeNotificationsEnabled) {
//                                statsNavigationComponent.featureNotificationOnboarding()
//                            }
//                            links.value = getLinks()
//                        }
//                }
//            }
//        }
//        links.value = getLinks()
//    }
//
//    private fun getLinks(): List<MenuItems> {
//        val list = mutableListOf<MenuItems>()
//        list.add(MenuItems.Button.Search)
//        list.add(MenuItems.Button.Rss)
//        list.add(MenuItems.Button.Settings)
//        list.add(MenuItems.Button.Contact)
//        val debugMenuItemList = debugNavigationComponent.getDebugMenuItems()
//        if (debugMenuItemList.isNotEmpty()) {
//            list.add(MenuItems.Divider("z"))
//            debugMenuItemList.forEach {
//                list.add(MenuItems.Button.Custom(it.label, it.icon, it.id))
//            }
//        }
//        list.add(MenuItems.Divider("a"))
//        list.add(
//            MenuItems.Toggle.DarkMode(
//                _isEnabled = !styleManager.isDayMode
//            )
//        )
//        list.add(MenuItems.Divider("b"))
//        if (buildConfigManager.isRuntimeNotificationsSupported &&
//            !notificationRepository.seenRuntimeNotifications &&
//            !permissionRepository.isRuntimeNotificationsEnabled
//        ) {
//            list.add(MenuItems.Feature.RuntimeNotifications)
//        }
//        if (!buildConfigManager.isRuntimeNotificationsSupported && !notificationRepository.seenNotificationOnboarding) {
//            list.add(MenuItems.Feature.Notifications)
//        }
//        if (list.any { it is MenuItems.Feature }) {
//            list.add(MenuItems.Divider("c"))
//        }
//        return list
//    }
//}