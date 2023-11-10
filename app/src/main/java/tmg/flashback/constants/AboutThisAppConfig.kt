package tmg.flashback.constants

import android.content.Context
import androidx.core.graphics.toColorInt
import tmg.aboutthisapp.configuration.Configuration
import tmg.aboutthisapp.configuration.Dependency
import tmg.aboutthisapp.configuration.DependencyIcon
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.rss.repo.model.SupportedArticleSource

/**
 * About This App Configuration and data
 */
object AboutThisAppConfig {

    // Configuration item
    fun configuration(
        context: Context,
        appVersion: String,
        deviceUdid: String,
        contactEmail: String,
        rssSources: List<SupportedArticleSource>
    ): Configuration {
        val dependenciesList = getDependencies()
        val list = dependenciesList +
                getRssSourcesDependencies(context, rssSources)
        val assets = listOf(
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
                ),
        )
        return Configuration(
                appName = context.getString(R.string.app_name),
                imageRes = R.mipmap.ic_launcher,
                email = contactEmail,
                header = context.getString(R.string.dependency_thank_you),
                footnote = context.getString(R.string.about_additional),
                debugInfo = deviceUdid,
                appVersion = appVersion,
                appPackageName = BuildConfig.APPLICATION_ID,
                dependencies = (list + assets)
        )
    }

    private fun getDependencies(): List<Dependency> {
        return listOf(
        )
    }

    private fun getRssSourcesDependencies(context: Context, sources: List<SupportedArticleSource>): List<Dependency> {
        return sources.mapIndexed { index, supportedSource ->
            Dependency(
                    dependencyName = context.getString(R.string.about_this_app_dependency_rss, supportedSource.title),
                    author = supportedSource.title,
                    url = supportedSource.contactLink,
                    icon = DependencyIcon.Icon(
                            icon = R.drawable.about_this_app_dependency_rss_icon,
                            backgroundColor = (supportedSource.colour.toColorInt() and 0x00FFFFFF) or 0x79000000
                    )
            )
        }
    }
}