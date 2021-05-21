package tmg.flashback.constants

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import tmg.aboutthisapp.AboutThisAppConfiguration
import tmg.aboutthisapp.AboutThisAppDependency
import tmg.flashback.R
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.repo.model.SupportedSource

/**
 * About This App Configuration and data
 */
object AboutThisAppConfig {

    // Configuration item
    fun configuration(
        context: Context,
        appVersion: String,
        deviceUdid: String,
        rssSources: List<SupportedArticleSource>
    ): AboutThisAppConfiguration {
        val dependenciesList = getDependencies()
        val list = dependenciesList +
                getRssSourcesDependencies(context, dependenciesList.size, rssSources)
        val assets = listOf(
                AboutThisAppDependency(
                        order = list.size + 1,
                        dependencyName = "Lottie: LottieFiles",
                        author = "LottieFiles",
                        imageUrl = "https://assets4.lottiefiles.com/avatars/300_8243-998405194.jpg",
                        backgroundColor = Color.TRANSPARENT,
                        url = "https://lottiefiles.com/LottieFiles"
                ),
                AboutThisAppDependency(
                        order = list.size + 1,
                        dependencyName = "FlatIcon: Freepik",
                        author = "Freepik",
                        imageUrl = "https://img-authors.flaticon.com/freepik.jpg",
                        backgroundColor = Color.TRANSPARENT,
                        url = "https://www.flaticon.com/authors/freepik"
                ),
        )
        return AboutThisAppConfiguration(
                themeRes = R.style.FlashbackAppTheme_AboutThisApp,
                name = context.getString(R.string.app_name),
                nameDesc = context.getString(R.string.about_desc),
                imageRes = R.mipmap.ic_launcher,
                subtitle = context.getString(R.string.dependency_thank_you),
                footnote = context.getString(R.string.about_additional),
                guid = deviceUdid,
                appVersion = appVersion,
                appName = context.getString(R.string.app_name),
                appPackageName = "tmg.flashback",
                email = "thementalgoose@gmail.com",
                play = "https://play.google.com/store/apps/details?id=tmg.flashback",
                dependencies = list + assets
        )
    }

    private fun getDependencies(): List<AboutThisAppDependency> {
        return listOf(
                AboutThisAppDependency(
                        order = 0,
                        dependencyName = "Ergast API",
                        author = "Ergast",
                        url = "https://ergast.com/mrd/",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://pbs.twimg.com/profile_images/204468195/logo_400x400.png"
                ),
                AboutThisAppDependency(
                        order = 1,
                        dependencyName = "Firebase",
                        author = "Google",
                        url = "https://firebase.google.com/",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars2.githubusercontent.com/u/1335026"
                ),
                AboutThisAppDependency(
                        order = 2,
                        dependencyName = "Glide",
                        author = "Bump Technologies",
                        url = "https://github.com/bumptech/glide",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://lh3.googleusercontent.com/OOjYcooPxIC4PHWxKGg5tfVm5qbJB4m2IMvhmXCOMl9Ps4T6dvmcA66UscrkML0lU6WR0IfswAL9QNpEL63mpLvrtDMiLnOMYCT8rhkC-eIXjhDNk6wGlx-nMJeZzyrvairQOD48KnxhY9vc-tahh7rgKoJeR1mwfoJIVfBNRwlNTSrLkrDZFAU15fvGofmKCrrvlUgUka6tpD80A1-Dm3KRE9knS0m1UHssQ6-KOFdGSndZ70ayGV5pY-n-zDsMYAzDNQMwvb2AhUddiO6VOViXztUqiYuVX5eqCnL7z-bndTcDAqfyohvw8txH5bvc1VR0XcQPjGzJ6EVkdZso2T4b5NoFufzlIP3DPjoFE37VKEGmnI-QMhz9m_IwuJ2U0WXBP9Q4pJkVPqwbIZzm-g338ZETis17D3r52v4hDsq5mN7vzV5KcRHs5l1uivdS5Wj5SQ0t96xmndOEOUISyIxGWeeDGIVSImnK6GuLEfrO4Vsi9gc4Qi8KU5aDBZ0rsbTM-hgNObqBTs-AebwR9gspWCqW7Cigfnezbf1bHAyvPjoLaJ_2IxjoF9KZxjPieYRuXMoDpdhvT5_0cfEsUQF8HjR1qBPku_asce3UtQGvIhMikw=s0"
                ),
                AboutThisAppDependency(
                        order = 3,
                        dependencyName = "Koin",
                        author = "Koin",
                        url = "https://github.com/InsertKoinIO/koin",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars1.githubusercontent.com/u/38280958"
                ),
                AboutThisAppDependency(
                        order = 4,
                        dependencyName = "FlagKit",
                        author = "WANG Jie",
                        url = "https://github.com/wangjiejacques/flagkit",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars3.githubusercontent.com/u/2981971"
                ),
                AboutThisAppDependency(
                        order = 6,
                        dependencyName = "BugShaker Android",
                        author = "Stuart Kent",
                        url = "https://github.com/stkent/bugshaker-android",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars0.githubusercontent.com/u/6463980"
                ),
                AboutThisAppDependency(
                        order = 7,
                        dependencyName = "ThreeTen",
                        author = "Jake Wharton",
                        url = "https://github.com/JakeWharton/ThreeTenABP",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars0.githubusercontent.com/u/66577"
                ),
                AboutThisAppDependency(
                        order = 8,
                        dependencyName = "OverlappingPanels",
                        author = "Discord",
                        url = "https://github.com/discord/OverlappingPanels",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars3.githubusercontent.com/u/1965106"
                ),
                AboutThisAppDependency(
                        order = 9,
                        dependencyName = "SkeletonLayout",
                        author = "Faltenreich",
                        url = "https://github.com/Faltenreich/SkeletonLayout",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars3.githubusercontent.com/u/7239950"
                ),
                AboutThisAppDependency(
                        order = 10,
                        dependencyName = "Lottie",
                        author = "AirBnB",
                        url = "https://github.com/airbnb/lottie-android",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars2.githubusercontent.com/u/698437"
                ),
                AboutThisAppDependency(
                        order = 11,
                        dependencyName = "Labelled Progress Bar",
                        author = "Jordan Fisher",
                        url = "https://github.com/thementalgoose/android-labelled-progress-bar",
                        backgroundColor = Color.TRANSPARENT,
                        imageUrl = "https://avatars1.githubusercontent.com/u/5982159"
                )
        )
    }

    private fun getRssSourcesDependencies(context: Context, startIndex: Int, sources: List<SupportedArticleSource>): List<AboutThisAppDependency> {
        return sources.mapIndexed { index, supportedSource ->
            AboutThisAppDependency(
                    order = startIndex + index,
                    dependencyName = context.getString(R.string.about_this_app_dependency_rss, supportedSource.title),
                    author = supportedSource.title,
                    url = supportedSource.contactLink,
                    imageUrl = "",
                    backgroundColor = (supportedSource.colour.toColorInt() and 0x00FFFFFF) or 0x79000000,
                    imageRes = R.drawable.about_this_app_dependency_rss
            )
        }
    }
}