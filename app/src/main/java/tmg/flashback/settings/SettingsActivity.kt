package tmg.flashback.settings

import android.content.Intent
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.bottom_sheet_theme.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.koin.android.ext.android.inject
import tmg.components.about.AboutThisAppActivity
import tmg.components.about.AboutThisAppDependency
import tmg.components.prefs.AppPreferencesAdapter
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.settings.release.ReleaseActivity
import tmg.flashback.utils.bottomsheet.BottomSheetAdapter
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*

class SettingsActivity : BaseActivity() {

    private val viewModel: SettingsViewModel by inject()
    private lateinit var adapter: AppPreferencesAdapter

    private lateinit var themeBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var themeAdapter: BottomSheetAdapter

    override fun layoutId(): Int = R.layout.activity_settings

    override fun initViews() {

        setupSettings()
        setupBottomSheet()

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observe(viewModel.outputs.themePreferences) {
            themeAdapter.list = it
        }

        observeEvent(viewModel.outputs.openThemePicker) {
            themeBottomSheet.expand()
        }

        observeEvent(viewModel.outputs.openAbout) {
            showAbout()
        }

        observeEvent(viewModel.outputs.openSuggestions) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, "thementalgoose@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
        }

        observeEvent(viewModel.outputs.openRelease) {
            startActivity(Intent(this, ReleaseActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSettings() {

        initToolbar(R.id.toolbar, true, indicator = R.drawable.ic_back)
        toolbarLayout.header.text = getString(R.string.settings_title)

        adapter = SettingsAdapter(
            prefClicked = { key ->
                viewModel.inputs.preferenceClicked(key.toEnum<SettingsOptions> { it.key }, null)
            },
            prefSwitchClicked = { key, value ->
                val settingsOption = key.toEnum<SettingsOptions> { it.key }
                viewModel.inputs.preferenceClicked(settingsOption, value)
                notifySwitchClicked(settingsOption)
            }
        )
        rvSettings.adapter = adapter
        rvSettings.layoutManager = LinearLayoutManager(this)
    }

    private fun notifySwitchClicked(option: SettingsOptions?) {
        if (option == SettingsOptions.CRASH) {
            Toast.makeText( this, R.string.settings_help_crash_reporting_notify,Toast.LENGTH_LONG).show()
        }
        if (option == SettingsOptions.SHAKE) {
            Toast.makeText( this, R.string.settings_help_shake_to_report_notify,Toast.LENGTH_LONG).show()
        }
    }

    private fun setupBottomSheet() {
        themeAdapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.pickTheme(ThemePref.values()[it.id])
            }
        )
        themeList.adapter = themeAdapter
        themeList.layoutManager = LinearLayoutManager(this)

        themeBottomSheet = BottomSheetBehavior.from(themeLayout)
        themeBottomSheet.isHideable = true
        themeBottomSheet.hidden()
        themeBottomSheet.addBottomSheetCallback(BottomSheetFader(vBackground, "theme"))
        vBackground.setOnClickListener { themeBottomSheet.hidden() }
    }

    private fun showAbout() {
        startActivity(
            AboutThisAppActivity.intent(
                this,
                isDarkMode = false,
                name = getString(R.string.about_name),
                nameDesc = getString(R.string.about_desc),
                imageUrl = "https://lh3.googleusercontent.com/l7T59nmp3joS3CUWEw-_mfmZAwXMHmiikR6uvQLFTXGy1-BlUICXxJZ9UCcvQlWhvQ",
                thankYou = getString(R.string.dependency_thank_you),
                footnote = getString(R.string.about_additional),
                appVersion = BuildConfig.VERSION_NAME,
                appName = getString(R.string.app_name),
                email = "thementalgoose@gmail.com",
                play = "https://play.google.com/store/apps/details?id=tmg.flashback",
                dependencies = listOf(
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
                        order = 3,
                        dependencyName = "InboxRecyclerView",
                        author = "Saket Narayan",
                        url = "https://github.com/saket/InboxRecyclerView",
                        imageUrl = "https://avatars0.githubusercontent.com/u/2387680"
                    ),
                    AboutThisAppDependency(
                        order = 4,
                        dependencyName = "FlagKit",
                        author = "WANG Jie",
                        url = "https://github.com/wangjiejacques/flagkit",
                        imageUrl = "https://avatars3.githubusercontent.com/u/2981971"
                    ),
                    AboutThisAppDependency(
                        order = 5,
                        dependencyName = "CircleImageView",
                        author = "Henning Dodenhof",
                        url = "https://github.com/hdodenhof/CircleImageView",
                        imageUrl = "https://avatars2.githubusercontent.com/u/1824223"
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
                        order = 8,
                        dependencyName = "MaterialProgressBar",
                        author = "Hai Zhang",
                        url = "https://github.com/zhanghai/MaterialProgressBar",
                        imageUrl = "https://avatars2.githubusercontent.com/u/4469895"
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
                        url = "https://github.com/thementalgoose/android-components",
                        imageUrl = "https://avatars1.githubusercontent.com/u/5982159"
                    ),
                    AboutThisAppDependency(
                        order = 12,
                        dependencyName = "Pull to dismiss",
                        author = "Fatih Sokmen",
                        url = "https://github.com/fatihsokmen/pull-to-dismiss",
                        imageUrl = "https://avatars2.githubusercontent.com/u/2427299"
                    )
                )
            )
        )
    }
}