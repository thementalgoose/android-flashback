package tmg.flashback

import android.content.Context
import androidx.annotation.DrawableRes
import org.threeten.bp.Year
import tmg.components.about.AboutThisAppConfiguration
import tmg.components.about.AboutThisAppDependency

const val minimumSupportedYear = 1950
val currentYear: Int
    get() = Year.now().value
val allYears: List<Int>
    get() = (minimumSupportedYear..currentYear).map { it }

const val showComingSoonMessageForNextDays = 10

const val bottomSheetFastScrollDuration = 300

fun isValidVersion(version: Int?): Boolean =
    version == null || (version >= BuildConfig.VERSION_CODE)


enum class TrackLayout(
    val circuitId: String,
    @DrawableRes
    val icon: Int
) {
    ADELAIDE("adelaide", R.drawable.circuit_adelaide),
    ALBERT_PARK("albert_park", R.drawable.circuit_albert_park),
    ALGARVE("algarve", R.drawable.circuit_algarve),
    AMERICAS("americas", R.drawable.circuit_americas),
    BAHRAIN("bahrain", R.drawable.circuit_bahrain),
    BAK("BAK", R.drawable.circuit_bak),
    CATALUNYA("catalunya", R.drawable.circuit_catalunya),
    HANOI("hanoi", R.drawable.circuit_hanoi),
    HOCKENHEIMRING("hockenheimring", R.drawable.circuit_hockenheimring),
    HUNGARORING("hungaroring", R.drawable.circuit_hungaroring),
    INDIANAPOLIS("indianapolis", R.drawable.circuit_indianapolis),
    INTERLAGOS("interlagos", R.drawable.circuit_interlagos),
    IMOLA("imola", R.drawable.circuit_imola),
    ISTANBUL("istanbul", R.drawable.circuit_istanbul),
    MAGNY_COURS("magny_cours", R.drawable.circuit_magny_cours),
    MARINA_BAY("marina_bay", R.drawable.circuit_marina_bay),
    MONACO("monaco", R.drawable.circuit_monaco),
    MONZA("monza", R.drawable.circuit_monza),
    MUGELLO("mugello", R.drawable.circuit_mugello),
    NURBURGRING("nurburgring", R.drawable.circuit_nurburgring),
    RED_BULL_RING("red_bull_ring", R.drawable.circuit_red_bull_ring),
    RICARD("ricard", R.drawable.circuit_ricard),
    RODRIGUEZ("rodriguez", R.drawable.circuit_rodriguez),
    SEPANG("sepang", R.drawable.circuit_sepang),
    SHANGHAI("shanghai", R.drawable.circuit_shanghai),
    SILVERSTONE("silverstone", R.drawable.circuit_silverstone),
    SOCHI("sochi", R.drawable.circuit_sochi),
    SPA("spa", R.drawable.circuit_spa),
    SUZUKA("suzuka", R.drawable.circuit_suzuka),
    VALENCIA("valencia", R.drawable.circuit_valencia),
    VILLENEUVE("villeneuve", R.drawable.circuit_villeneuve),
    YAS_MARINA("yas_marina", R.drawable.circuit_yas_marina),
    YEONGAM("yeongam", R.drawable.circuit_yeongam),
    ZANDVOORT("zandvoort", R.drawable.circuit_zandvoort);
}


val coloursDecade: Map<String, String> = mapOf(
    "1950" to "#9fa8da",
    "1960" to "#ce93d8",
    "1970" to "#ef9a9a",
    "1980" to "#90caf9",
    "1990" to "#a5d6a7",
    "2000" to "#81d4fa",
    "2010" to "#b0bec5",
    "2020" to "#f48fb1",
    "2030" to "#b39ddb",
    "2040" to "#c5e1a5",
    "2050" to "#80cbc4",
    "2060" to "#b0bec5",
    "2070" to "#ffcc80",
    "2080" to "#ffab91",
    "2090" to "#80deea"
)

val colours: List<Pair<String, String>> = listOf( // Dark to light
    "#ffcdd2" to "#ef9a9a",
    "#f8bbd0" to "#f48fb1",
    "#e1bee7" to "#ce93d8",
    "#d1c4e9" to "#b39ddb",
    "#c5cae9" to "#9fa8da",
    "#bbdefb" to "#90caf9",
    "#b3e5fc" to "#81d4fa",
    "#b2ebf2" to "#80deea",
    "#b2dfdb" to "#80cbc4",
    "#c8e6c9" to "#a5d6a7",
    "#dcedc8" to "#c5e1a5",
    "#f0f4c3" to "#e6ee9c",
    "#ffe0b2" to "#ffcc80",
    "#ffccbc" to "#ffab91",
    "#cfd8dc" to "#b0bec5"
)

fun configuration(context: Context, isDarkMode: Boolean = false) = AboutThisAppConfiguration(
    isDarkMode = isDarkMode,
    name = context.getString(R.string.about_name),
    nameDesc = context.getString(R.string.about_desc),
    imageUrl = "https://lh3.googleusercontent.com/l7T59nmp3joS3CUWEw-_mfmZAwXMHmiikR6uvQLFTXGy1-BlUICXxJZ9UCcvQlWhvQ",
    thankYou = context.getString(R.string.dependency_thank_you),
    footnote = context.getString(R.string.about_additional),
    appVersion = BuildConfig.VERSION_NAME,
    appName = context.getString(R.string.app_name),
    email = "thementalgoose@gmail.com",
    play = "https://play.google.com/store/apps/details?id=tmg.flashback",
    dependencies = dependencies,
    insetsForNavigationBar = true
)

val dependencies = listOf(
    AboutThisAppDependency(
        order = -1,
        dependencyName = "Ergast API",
        author = "Ergast",
        url = "https://ergast.com/mrd/",
        imageUrl = "https://pbs.twimg.com/profile_images/204468195/logo_400x400.png"
    ),
    AboutThisAppDependency(
        order = 0,
        dependencyName = "Firebase",
        author = "Google",
        url = "https://firebase.google.com/",
        imageUrl = "https://avatars2.githubusercontent.com/u/1335026"
    ),
    AboutThisAppDependency(
        order = 1,
        dependencyName = "Glide",
        author = "Bump Technologies",
        url = "https://github.com/bumptech/glide",
        imageUrl = "https://lh3.googleusercontent.com/OOjYcooPxIC4PHWxKGg5tfVm5qbJB4m2IMvhmXCOMl9Ps4T6dvmcA66UscrkML0lU6WR0IfswAL9QNpEL63mpLvrtDMiLnOMYCT8rhkC-eIXjhDNk6wGlx-nMJeZzyrvairQOD48KnxhY9vc-tahh7rgKoJeR1mwfoJIVfBNRwlNTSrLkrDZFAU15fvGofmKCrrvlUgUka6tpD80A1-Dm3KRE9knS0m1UHssQ6-KOFdGSndZ70ayGV5pY-n-zDsMYAzDNQMwvb2AhUddiO6VOViXztUqiYuVX5eqCnL7z-bndTcDAqfyohvw8txH5bvc1VR0XcQPjGzJ6EVkdZso2T4b5NoFufzlIP3DPjoFE37VKEGmnI-QMhz9m_IwuJ2U0WXBP9Q4pJkVPqwbIZzm-g338ZETis17D3r52v4hDsq5mN7vzV5KcRHs5l1uivdS5Wj5SQ0t96xmndOEOUISyIxGWeeDGIVSImnK6GuLEfrO4Vsi9gc4Qi8KU5aDBZ0rsbTM-hgNObqBTs-AebwR9gspWCqW7Cigfnezbf1bHAyvPjoLaJ_2IxjoF9KZxjPieYRuXMoDpdhvT5_0cfEsUQF8HjR1qBPku_asce3UtQGvIhMikw=s0"
    ),
    AboutThisAppDependency(
        order = 2,
        dependencyName = "Koin",
        author = "Koin",
        url = "https://github.com/InsertKoinIO/koin",
        imageUrl = "https://avatars1.githubusercontent.com/u/38280958"
    ),
    AboutThisAppDependency(
        order = 4,
        dependencyName = "FlagKit",
        author = "WANG Jie",
        url = "https://github.com/wangjiejacques/flagkit",
        imageUrl = "https://avatars3.githubusercontent.com/u/2981971"
    ),
    AboutThisAppDependency(
        order = 6,
        dependencyName = "BugShaker Android",
        author = "Stuart Kent",
        url = "https://github.com/stkent/bugshaker-android",
        imageUrl = "https://avatars0.githubusercontent.com/u/6463980"
    ),
    AboutThisAppDependency(
        order = 7,
        dependencyName = "ThreeTen",
        author = "Jake Wharton",
        url = "https://github.com/JakeWharton/ThreeTenABP",
        imageUrl = "https://avatars0.githubusercontent.com/u/66577"
    ),
    AboutThisAppDependency(
        order = 9,
        dependencyName = "SkeletonLayout",
        author = "Faltenreich",
        url = "https://github.com/Faltenreich/SkeletonLayout",
        imageUrl = "https://avatars3.githubusercontent.com/u/7239950"
    ),
    AboutThisAppDependency(
        order = 10,
        dependencyName = "Lottie",
        author = "AirBnB",
        url = "https://github.com/airbnb/lottie-android",
        imageUrl = "https://avatars2.githubusercontent.com/u/698437"
    ),
    AboutThisAppDependency(
        order = 11,
        dependencyName = "Components + Utilities",
        author = "Jordan Fisher",
        url = "https://github.com/thementalgoose/android-utilities",
        imageUrl = "https://avatars1.githubusercontent.com/u/5982159"
    ),
    AboutThisAppDependency(
        order = 13,
        dependencyName = "Inbox Recycler View",
        author = "Saket Narayan",
        url = "https://github.com/saket/InboxRecyclerView",
        imageUrl = "https://avatars3.githubusercontent.com/u/2387680"
    ),
    AboutThisAppDependency(
        order = 12,
        dependencyName = "IndicatorFastScroll",
        author = "Reddit",
        url = "https://github.com/reddit/IndicatorFastScroll",
        imageUrl = "https://avatars3.githubusercontent.com/u/14248"
    )
)