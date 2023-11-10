package tmg.flashback.presentation.aboutthisapp

import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.aboutthisapp.configuration.Dependency
import tmg.aboutthisapp.configuration.DependencyIcon
import tmg.flashback.R
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.device.usecases.CopyToClipboardUseCase
import tmg.flashback.device.usecases.OpenPlayStoreUseCase
import tmg.flashback.device.usecases.OpenSendEmailUseCase
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.rss.usecases.AllSupportedSourcesUseCase
import tmg.flashback.web.usecases.OpenWebpageUseCase
import javax.inject.Inject

@HiltViewModel
class AboutThisAppViewModel @Inject constructor(
    buildConfigManager: BuildConfigManager,
    deviceRepository: DeviceRepository,
    private val contactRepository: ContactRepository,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val openPlayStoreUseCase: OpenPlayStoreUseCase,
    private val openSendEmailUseCase: OpenSendEmailUseCase,
    private val copyToClipboardUseCase: CopyToClipboardUseCase,
    private val allSupportedSourcesUseCase: AllSupportedSourcesUseCase,
): ViewModel() {

    private val _dependencies: MutableStateFlow<List<Dependency>> = MutableStateFlow(staticDependencies + rssDependencies + assetDependencies)
    val dependencies: StateFlow<List<Dependency>> = _dependencies

    private val _guid: MutableStateFlow<Pair<String, String>> = MutableStateFlow(deviceRepository.deviceUdid to deviceRepository.installationId)
    val guid: StateFlow<Pair<String, String>> = _guid

    private val _contactEmail: MutableStateFlow<String> = MutableStateFlow(contactRepository.contactEmail)
    val contactEmail: StateFlow<String> = _contactEmail

    private val _applicationId: MutableStateFlow<String> = MutableStateFlow(buildConfigManager.versionName)
    val applicationId: StateFlow<String> = _applicationId

    fun openUrl(string: String) {
        openWebpageUseCase.open(string, "")
    }

    fun openPlaystore() {
        openPlayStoreUseCase.openPlaystore()
    }

    fun openEmail() {
        openSendEmailUseCase.sendEmail(contactRepository.contactEmail)
    }

    fun copyToClipboard(content: String) {
        copyToClipboardUseCase.copyToClipboard(content)
    }

    private val staticDependencies: List<Dependency>
        get() = listOf(
            Dependency(
                dependencyName = "Ergast API",
                author = "Ergast",
                url = "https://ergast.com/mrd/",
                icon = DependencyIcon.Image(url = "https://pbs.twimg.com/profile_images/204468195/logo_400x400.png")
            ),
            Dependency(
                dependencyName = "Jetpack",
                author = "Google",
                url = "https://developer.android.com/jetpack",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/6955922?s=200&v=4")
            ),
            Dependency(
                dependencyName = "Firebase",
                author = "Google",
                url = "https://firebase.google.com/",
                icon = DependencyIcon.Image(url = "https://avatars2.githubusercontent.com/u/1335026")
            ),
            Dependency(
                dependencyName = "Retrofit",
                author = "Square",
                url = "https://square.github.io/retrofit/",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/82592")
            ),
            Dependency(
                dependencyName = "OkHttp",
                author = "Square",
                url = "https://square.github.io/okhttp/",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/82592")
            ),
            Dependency(
                dependencyName = "Glide",
                author = "Bump Technologies",
                url = "https://github.com/bumptech/glide",
                icon = DependencyIcon.Image(url = "https://lh3.googleusercontent.com/OOjYcooPxIC4PHWxKGg5tfVm5qbJB4m2IMvhmXCOMl9Ps4T6dvmcA66UscrkML0lU6WR0IfswAL9QNpEL63mpLvrtDMiLnOMYCT8rhkC-eIXjhDNk6wGlx-nMJeZzyrvairQOD48KnxhY9vc-tahh7rgKoJeR1mwfoJIVfBNRwlNTSrLkrDZFAU15fvGofmKCrrvlUgUka6tpD80A1-Dm3KRE9knS0m1UHssQ6-KOFdGSndZ70ayGV5pY-n-zDsMYAzDNQMwvb2AhUddiO6VOViXztUqiYuVX5eqCnL7z-bndTcDAqfyohvw8txH5bvc1VR0XcQPjGzJ6EVkdZso2T4b5NoFufzlIP3DPjoFE37VKEGmnI-QMhz9m_IwuJ2U0WXBP9Q4pJkVPqwbIZzm-g338ZETis17D3r52v4hDsq5mN7vzV5KcRHs5l1uivdS5Wj5SQ0t96xmndOEOUISyIxGWeeDGIVSImnK6GuLEfrO4Vsi9gc4Qi8KU5aDBZ0rsbTM-hgNObqBTs-AebwR9gspWCqW7Cigfnezbf1bHAyvPjoLaJ_2IxjoF9KZxjPieYRuXMoDpdhvT5_0cfEsUQF8HjR1qBPku_asce3UtQGvIhMikw=s0")
            ),
            Dependency(
                dependencyName = "Coil",
                author = "Coil",
                url = "https://github.com/coil-kt/coil",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/52722434")
            ),
            Dependency(
                dependencyName = "FlagKit",
                author = "WANG Jie",
                url = "https://github.com/wangjiejacques/flagkit",
                icon = DependencyIcon.Image(url = "https://avatars3.githubusercontent.com/u/2981971")
            ),
            Dependency(
                dependencyName = "Shakey",
                author = "LinkedIn",
                url = "https://github.com/linkedin/shaky-android",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/357098?s=200")
            ),
            Dependency(
                dependencyName = "Accompanist",
                author = "Google",
                url = "https://google.github.io/accompanist/",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/1342004?s=200&v=4")
            ),
            Dependency(
                dependencyName = "Lottie",
                author = "AirBnB",
                url = "https://github.com/airbnb/lottie-android",
                icon = DependencyIcon.Image(url = "https://avatars2.githubusercontent.com/u/698437")
            ),
            Dependency(
                dependencyName = "ThreeTen",
                author = "Jake Wharton",
                url = "https://github.com/JakeWharton/ThreeTenABP",
                icon = DependencyIcon.Image(url = "https://avatars0.githubusercontent.com/u/66577")
            ),
            Dependency(
                dependencyName = "SkeletonLayout",
                author = "Faltenreich",
                url = "https://github.com/Faltenreich/SkeletonLayout",
                icon = DependencyIcon.Image(url = "https://avatars3.githubusercontent.com/u/7239950")
            )
        )

    private val rssDependencies: List<Dependency>
        get() = allSupportedSourcesUseCase.getSources()
            .map { supportedSource ->
                Dependency(
                    dependencyName = "RSS: ${supportedSource.title}",
                    author = supportedSource.title,
                    url = supportedSource.contactLink,
                    icon = DependencyIcon.Icon(
                        icon = R.drawable.about_this_app_dependency_rss_icon,
                        backgroundColor = (supportedSource.colour.toColorInt() and 0x00FFFFFF) or 0x79000000
                    )
                )
            }

    private val assetDependencies: List<Dependency>
        get() = listOf(
            Dependency(
                dependencyName = "Lottie: LottieFiles",
                author = "LottieFiles",
                icon = DependencyIcon.Image("https://assets4.lottiefiles.com/avatars/300_8243-998405194.jpg"),
                url = "https://lottiefiles.com/LottieFiles"
            ),
            Dependency(
                dependencyName = "Lottie: Baback Jafari",
                author = "Baback Jafari",
                icon = DependencyIcon.Image("https://assets4.lottiefiles.com/avatars/300_8243-998405194.jpg"),
                url = "https://lottiefiles.com/LottieFiles"
            ),
            Dependency(
                dependencyName = "FlatIcon: Freepik",
                author = "Freepik",
                icon = DependencyIcon.Image("https://img-authors.flaticon.com/freepik.jpg"),
                url = "https://www.flaticon.com/authors/freepik"
            ),
            Dependency(
                dependencyName = "IconFinder: Weatherful Icon Pack",
                author = "Rasmus Neilson",
                icon = DependencyIcon.Image("https://www.iconfinder.com/static/img/favicons/favicon-194x194.png"),
                url = "https://www.iconfinder.com/iconsets/weatherful"
            )
        )

    companion object {
        val GITHUB: String = "https://www.github.com/thementalgoose"
    }
}