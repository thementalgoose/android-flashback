package tmg.flashback.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import org.koin.android.ext.android.inject
import tmg.components.about.AboutThisAppActivity
import tmg.components.about.AboutThisAppDependency
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.currentYear
import tmg.flashback.extensions.label
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ViewTypePref
import tmg.flashback.settings.release.ReleaseActivity
import tmg.flashback.supportedYears

class SettingsFragment : PreferenceFragmentCompat() {

    private val prefs: PrefsDB by inject()

    companion object {
        const val keyPreferenceCustomisationYear: String = "prefs_customisation_year"
        const val keyPreferenceCustomisationViewType: String = "prefs_customisation_view_type"
        const val keyPreferenceHelpAbout: String = "prefs_help_about"
        const val keyPreferenceHelpReleaseNotes: String = "prefs_help_release_notes"
        const val keyPreferenceHelpCrashReporting: String = "prefs_help_crash_reporting"
        const val keyPreferenceHelpShakeToReport: String = "prefs_help_shake_to_report"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        year()
        viewType()

        about()
        releaseNotes()
        crashReporting()
        shakeToReport()
    }

    private fun year() {
        findPreference<Preference>(keyPreferenceCustomisationYear)?.let { pref ->
            pref.summary =
                getString(R.string.settings_customisation_year_description, prefs.selectedYear)
            pref.setOnPreferenceClickListener {
                activity?.let { activity ->
                    val years: Array<CharSequence> =
                        supportedYears.map { it.toString() }.toTypedArray()
                    AlertDialog.Builder(activity)
                        .setTitle(R.string.settings_defaults_year_dialog_title)
                        .setSingleChoiceItems(years, years
                            .indexOfFirst { it == prefs.selectedYear.toString() }
                        ) { dialog, index ->
                            prefs.selectedYear =
                                years[index].toString().toIntOrNull() ?: currentYear
                            pref.summary = getString(
                                R.string.settings_customisation_year_description,
                                prefs.selectedYear
                            )
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                return@setOnPreferenceClickListener true
            }
        }
    }

    private fun viewType() {
        findPreference<Preference>(keyPreferenceCustomisationViewType)?.let { pref ->
            pref.isEnabled = BuildConfig.DEBUG
            pref.setOnPreferenceClickListener {
                activity?.let { activity ->
                    var selected: Int = -1
                    val items: Array<CharSequence> =
                        ViewTypePref.values().mapIndexed { index, data ->
                            if (data == prefs.viewType) {
                                selected = index
                            }
                            activity.getString(data.label)
                        }.toTypedArray()
                    AlertDialog.Builder(activity)
                        .setTitle(R.string.settings_customisation_view_type_title)
                        .setSingleChoiceItems(items, selected) { dialog, index ->
                            prefs.viewType = ViewTypePref.values()[index]
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                return@setOnPreferenceClickListener true
            }
        }
    }

    private fun about() {
        findPreference<Preference>(keyPreferenceHelpAbout)?.let { pref ->
            pref.setOnPreferenceClickListener {
                startActivity(
                    AboutThisAppActivity.intent(
                        requireContext(),
                        isDarkMode = false,
                        name = requireContext().getString(R.string.about_name),
                        nameDesc = requireContext().getString(R.string.about_desc),
                        imageUrl = "https://avatars1.githubusercontent.com/u/5982159",
                        thankYou = requireContext().getString(R.string.dependency_thank_you),
                        footnote = requireContext().getString(R.string.about_additional),
                        appVersion = BuildConfig.VERSION_NAME,
                        appName = requireContext().getString(R.string.app_name),
                        website = "https://jordanfisher.io",
                        email = "thementalgoose@gmail.com",
                        packageName = context?.packageName ?: "",
                        dependencies = listOf(
                            AboutThisAppDependency(
                                order = 0,
                                dependencyName = "Firebase",
                                author = "Google",
                                url = "https://firebase.google.com/",
                                imageUrl = "https://avatars2.githubusercontent.com/u/1335026"
                            ),
                            AboutThisAppDependency(
                                order = 3,
                                dependencyName = "Glide",
                                author = "Bump Technologies",
                                url = "https://github.com/bumptech/glide",
                                imageUrl = "https://lh3.googleusercontent.com/OOjYcooPxIC4PHWxKGg5tfVm5qbJB4m2IMvhmXCOMl9Ps4T6dvmcA66UscrkML0lU6WR0IfswAL9QNpEL63mpLvrtDMiLnOMYCT8rhkC-eIXjhDNk6wGlx-nMJeZzyrvairQOD48KnxhY9vc-tahh7rgKoJeR1mwfoJIVfBNRwlNTSrLkrDZFAU15fvGofmKCrrvlUgUka6tpD80A1-Dm3KRE9knS0m1UHssQ6-KOFdGSndZ70ayGV5pY-n-zDsMYAzDNQMwvb2AhUddiO6VOViXztUqiYuVX5eqCnL7z-bndTcDAqfyohvw8txH5bvc1VR0XcQPjGzJ6EVkdZso2T4b5NoFufzlIP3DPjoFE37VKEGmnI-QMhz9m_IwuJ2U0WXBP9Q4pJkVPqwbIZzm-g338ZETis17D3r52v4hDsq5mN7vzV5KcRHs5l1uivdS5Wj5SQ0t96xmndOEOUISyIxGWeeDGIVSImnK6GuLEfrO4Vsi9gc4Qi8KU5aDBZ0rsbTM-hgNObqBTs-AebwR9gspWCqW7Cigfnezbf1bHAyvPjoLaJ_2IxjoF9KZxjPieYRuXMoDpdhvT5_0cfEsUQF8HjR1qBPku_asce3UtQGvIhMikw=s0"
                            ),
                            AboutThisAppDependency(
                                order = 5,
                                dependencyName = "Koin",
                                author = "Koin",
                                url = "https://github.com/InsertKoinIO/koin",
                                imageUrl = "https://avatars1.githubusercontent.com/u/38280958"
                            ),
                            AboutThisAppDependency(
                                order = 6,
                                dependencyName = "FlagKit",
                                author = "WANG Jie",
                                url = "https://github.com/wangjiejacques/flagkit",
                                imageUrl = "https://avatars3.githubusercontent.com/u/2981971"
                            ),
                            AboutThisAppDependency(
                                order = 7,
                                dependencyName = "CircleImageView",
                                author = "Henning Dodenhof",
                                url = "https://github.com/hdodenhof/CircleImageView",
                                imageUrl = "https://avatars2.githubusercontent.com/u/1824223"
                            ),
                            AboutThisAppDependency(
                                order = 8,
                                dependencyName = "BugShaker Android",
                                author = "Stuart Kent",
                                url = "https://github.com/stkent/bugshaker-android",
                                imageUrl = "https://avatars0.githubusercontent.com/u/6463980"
                            ),
                            AboutThisAppDependency(
                                order = 9,
                                dependencyName = "ThreeTen",
                                author = "Jake Wharton",
                                url = "https://github.com/JakeWharton/ThreeTenABP",
                                imageUrl = "https://avatars0.githubusercontent.com/u/66577"
                            ),
                            AboutThisAppDependency(
                                order = 10,
                                dependencyName = "MaterialProgressBar",
                                author = "Hai Zhang",
                                url = "https://github.com/zhanghai/MaterialProgressBar",
                                imageUrl = "https://avatars2.githubusercontent.com/u/4469895"
                            ),
                            AboutThisAppDependency(
                                order = 11,
                                dependencyName = "SkeletonLayout",
                                author = "Faltenreich",
                                url = "https://github.com/Faltenreich/SkeletonLayout",
                                imageUrl = "https://avatars3.githubusercontent.com/u/7239950"
                            )
                        )
                    )
                )
                return@setOnPreferenceClickListener true
            }
        }
    }

    private fun releaseNotes() {
        findPreference<Preference>(keyPreferenceHelpReleaseNotes)?.let { pref ->
            pref.setOnPreferenceClickListener {
                startActivity(Intent(context, ReleaseActivity::class.java))
                return@setOnPreferenceClickListener true
            }
        }
    }

    private fun crashReporting() {
        val initialValue = prefs.crashReporting
        findPreference<SwitchPreference>(keyPreferenceHelpCrashReporting)?.let { pref ->
            pref.isChecked = initialValue
            pref.setOnPreferenceChangeListener { preference, newValue ->
                val value = newValue as Boolean
                pref.isChecked = value
                prefs.crashReporting = value
                Toast.makeText(
                    requireContext(),
                    getString(R.string.settings_help_crash_reporting_after_app_restart),
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnPreferenceChangeListener true
            }
        }
    }

    private fun shakeToReport() {
        val initialValue = prefs.shakeToReport
        findPreference<SwitchPreference>(keyPreferenceHelpShakeToReport)?.let { pref ->
            pref.isChecked = initialValue
            pref.setOnPreferenceChangeListener { preference, newValue ->
                val value = newValue as Boolean
                pref.isChecked = value
                prefs.shakeToReport = value
                Toast.makeText(
                    requireContext(),
                    getString(R.string.settings_help_shake_to_report_after_app_restart),
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnPreferenceChangeListener true
            }
        }
    }
}