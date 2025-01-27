package tmg.flashback.presentation.aboutthisapp

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.aboutthisapp.configuration.Colours
import tmg.aboutthisapp.configuration.Configuration
import tmg.aboutthisapp.configuration.Dependency
import tmg.aboutthisapp.configuration.DependencyIcon
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.repositories.ContactRepository
import tmg.flashback.rss.usecases.AllSupportedSourcesUseCase
import tmg.flashback.style.darkColours
import tmg.flashback.style.dynamic
import tmg.flashback.style.lightColours
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import javax.inject.Inject

class AboutThisAppConfigProvider @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val buildConfigManager: BuildConfigManager,
    private val deviceRepository: DeviceRepository,
    private val contactRepository: ContactRepository,
    private val themeRepository: ThemeRepository,
    private val allSupportedSourcesUseCase: AllSupportedSourcesUseCase,
) {
    fun getConfig(): Configuration {
        val lightColours = when (themeRepository.theme == Theme.MATERIAL_YOU) {
            true -> dynamicColors(isLight = true) ?: lightColors
            false -> lightColors
        }
        val darkColours = when (themeRepository.theme == Theme.MATERIAL_YOU) {
            true -> dynamicColors(isLight = false) ?: darkColors
            false -> darkColors
        }
        return Configuration(
            imageRes = R.mipmap.ic_launcher,
            appName = context.getString(R.string.app_name),
            appVersion = buildConfigManager.versionName,
            appPackageName = buildConfigManager.applicationId,
            dependencies = staticDependencies + rssDependencies + assetDependencies,
            header = context.getString(string.dependency_thank_you),
            footnote = context.getString(string.about_additional),
            email = contactRepository.contactEmail,
            github = "https://www.github.com/thementalgoose",
            debugInfo = "${deviceRepository.deviceUdid}\n${deviceRepository.installationId}",
            lightColors = lightColours,
            darkColors = darkColours
        )
    }

    //region Colours

    private fun dynamicColors(isLight: Boolean): Colours? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return null
        }
        val colours = when (isLight) {
            true -> lightColours.dynamic(dynamicLightColorScheme(context), isLightMode = true)
            false -> darkColours.dynamic(dynamicDarkColorScheme(context), isLightMode = false)
        }
        return Colours(
            colorPrimary = colours.primary.toArgb(),
            background = colours.backgroundPrimary.toArgb(),
            surface = colours.backgroundSecondary.toArgb(),
            primary = colours.backgroundTertiary.toArgb(),
            onBackground = colours.contentPrimary.toArgb(),
            onSurface = colours.contentSecondary.toArgb(),
            onPrimary = colours.contentTertiary.toArgb(),
        )
    }

    private val lightColors = Colours(
        colorPrimary = lightColours.primary.toArgb(),
        background = lightColours.backgroundPrimary.toArgb(),
        surface = lightColours.backgroundSecondary.toArgb(),
        primary = lightColours.backgroundTertiary.toArgb(),
        onBackground = lightColours.contentPrimary.toArgb(),
        onSurface = lightColours.contentSecondary.toArgb(),
        onPrimary = lightColours.contentTertiary.toArgb(),
    )
    private val darkColors = Colours(
        colorPrimary = darkColours.primary.toArgb(),
        background = darkColours.backgroundPrimary.toArgb(),
        surface = darkColours.backgroundSecondary.toArgb(),
        primary = darkColours.backgroundTertiary.toArgb(),
        onBackground = darkColours.contentPrimary.toArgb(),
        onSurface = darkColours.contentSecondary.toArgb(),
        onPrimary = darkColours.contentTertiary.toArgb(),
    )

    //endregion

    //region Dependencies

    private val staticDependencies: List<Dependency>
        get() = listOf(
            Dependency(
                dependencyName = "Ergast API",
                author = "Ergast",
                url = "https://ergast.com/mrd/",
                icon = DependencyIcon.Image(url = "https://i.imgur.com/mWzkikf.png", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "Jetpack",
                author = "Google",
                url = "https://developer.android.com/jetpack",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/6955922?s=200&v=4", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "Firebase",
                author = "Google",
                url = "https://firebase.google.com/",
                icon = DependencyIcon.Image(url = "https://avatars2.githubusercontent.com/u/1335026", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "Retrofit",
                author = "Square",
                url = "https://square.github.io/retrofit/",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/82592", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "OkHttp",
                author = "Square",
                url = "https://square.github.io/okhttp/",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/82592", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "Glide",
                author = "Bump Technologies",
                url = "https://github.com/bumptech/glide",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/423539", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "Coil",
                author = "Coil",
                url = "https://github.com/coil-kt/coil",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/52722434", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "FlagKit",
                author = "WANG Jie",
                url = "https://github.com/wangjiejacques/flagkit",
                icon = DependencyIcon.Image(url = "https://avatars3.githubusercontent.com/u/2981971", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "Shakey",
                author = "LinkedIn",
                url = "https://github.com/linkedin/shaky-android",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/357098?s=200", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "Accompanist",
                author = "Google",
                url = "https://google.github.io/accompanist/",
                icon = DependencyIcon.Image(url = "https://avatars.githubusercontent.com/u/1342004?s=200&v=4", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "ThreeTen",
                author = "Jake Wharton",
                url = "https://github.com/JakeWharton/ThreeTenABP",
                icon = DependencyIcon.Image(url = "https://avatars0.githubusercontent.com/u/66577", backgroundColor = Color.WHITE)
            ),
            Dependency(
                dependencyName = "SkeletonLayout",
                author = "Faltenreich",
                url = "https://github.com/Faltenreich/SkeletonLayout",
                icon = DependencyIcon.Image(url = "https://avatars3.githubusercontent.com/u/7239950", backgroundColor = Color.WHITE)
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
                dependencyName = "FlatIcon: Freepik",
                author = "Freepik",
                icon = DependencyIcon.Image("https://cdn-teams-slug.flaticon.com/freepik.jpg", backgroundColor = Color.WHITE),
                url = "https://www.flaticon.com/authors/freepik"
            ),
            Dependency(
                dependencyName = "IconFinder: Weatherful Icon Pack",
                author = "Rasmus Neilson",
                icon = DependencyIcon.Image("https://www.iconfinder.com/static/img/favicons/favicon-194x194.png", backgroundColor = Color.WHITE),
                url = "https://www.iconfinder.com/iconsets/weatherful"
            )
        )

    //endregion
}