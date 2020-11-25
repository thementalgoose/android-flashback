package tmg.flashback.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.bottom_sheet_animation.*
import kotlinx.android.synthetic.main.bottom_sheet_theme.*
import org.koin.android.ext.android.inject
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.configuration
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.rss.ui.settings.RSSSettingsActivity
import tmg.flashback.settings.release.ReleaseActivity
import tmg.flashback.utils.bottomsheet.BottomSheetAdapter
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*

class SettingsActivity : BaseActivity() {

    private val viewModel: SettingsViewModel by inject()
    private lateinit var adapter: SettingsAdapter

    private lateinit var themeBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var animationBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var themeAdapter: BottomSheetAdapter
    private lateinit var animationAdapter: BottomSheetAdapter

    override fun layoutId(): Int = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSettings()
        setupBottomSheet()

        back.setOnClickListener { finish() }

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observe(viewModel.outputs.themePreferences) {
            themeAdapter.list = it
        }

        observeEvent(viewModel.outputs.openThemePicker) {
            themeBottomSheet.expand()
        }

        observe(viewModel.outputs.animationPreference) {
            animationAdapter.list = it
        }

        observeEvent(viewModel.outputs.openAnimationPicker) {
            animationBottomSheet.expand()
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

        observeEvent(viewModel.outputs.openNews) {
            startActivity(RSSSettingsActivity.intent(this))
        }
    }

    override fun onBackPressed() {
        if (themeBottomSheet.state != BottomSheetBehavior.STATE_HIDDEN || animationBottomSheet.state != BottomSheetBehavior.STATE_HIDDEN) {
            themeBottomSheet.hidden()
            animationBottomSheet.hidden()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSettings() {

        header.text = getString(R.string.settings_title)

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
        setupBottomSheetTheme()
        setupBottomSheetAnimation()

        vBackground.setOnClickListener {
            themeBottomSheet.hidden()
            animationBottomSheet.hidden()
        }
    }

    private fun setupBottomSheetTheme() {
        themeAdapter = BottomSheetAdapter(
                itemClicked = {
                    viewModel.inputs.pickTheme(ThemePref.values()[it.id])
                }
        )
        textList.adapter = themeAdapter
        textList.layoutManager = LinearLayoutManager(this)

        themeBottomSheet = BottomSheetBehavior.from(parentLayout)
        themeBottomSheet.isHideable = true
        themeBottomSheet.hidden()
        themeBottomSheet.addBottomSheetCallback(BottomSheetFader(vBackground, "theme"))
    }

    private fun setupBottomSheetAnimation() {
        animationAdapter = BottomSheetAdapter(
                itemClicked = {
                    viewModel.inputs.pickAnimationSpeed(BarAnimation.values()[it.id])
                }
        )

        animationList.adapter = animationAdapter
        animationList.layoutManager = LinearLayoutManager(this)

        animationBottomSheet = BottomSheetBehavior.from(animationLayout)
        animationBottomSheet.isHideable = true
        animationBottomSheet.hidden()
        animationBottomSheet.addBottomSheetCallback(BottomSheetFader(vBackground, "animation"))
    }

    private fun showAbout() {
        startActivity(
            AboutThisAppActivity.intent(
                context = this,
                configuration = configuration(this, !isLightTheme)
            )
        )
    }
}