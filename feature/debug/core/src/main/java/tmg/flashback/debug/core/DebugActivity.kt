@file:OptIn(ExperimentalFoundationApi::class)

package tmg.flashback.debug.core

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.debug.manager.BaseUrlLocalOverrideManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.notifications.receiver.LocalNotificationBroadcastReceiver
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.releasenotes.ReleaseNotesNavigationComponent
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.results.ResultsNavigationComponentImpl
import tmg.flashback.results.repository.models.NotificationChannel
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.repository.PermissionRepository
import tmg.utilities.extensions.copyToClipboard
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DebugActivity: BaseActivity() {

    @Inject
    lateinit var overviewRepository: OverviewRepository
    @Inject
    lateinit var circuitRepository: CircuitRepository
    @Inject
    lateinit var driverRepository: DriverRepository
    @Inject
    lateinit var constructorRepository: ConstructorRepository

    @Inject
    lateinit var baseUrlLocalOverrideManager: BaseUrlLocalOverrideManager

    @Inject
    lateinit var deviceRepository: DeviceRepository
    @Inject
    lateinit var adsRepository: AdsRepository
    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var releaseNotesNavigationComponent: ReleaseNotesNavigationComponent
    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var applicationNavigationComponent: tmg.flashback.navigation.ApplicationNavigationComponent

    @Inject
    lateinit var statsNavigationComponent: ResultsNavigationComponentImpl

    @Inject
    lateinit var permissionRepository: PermissionRepository

    companion object {
        private const val keyReleaseNotesSeenVersion: String = "RELEASE_NOTES_SEEN_VERSION"
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                DebugScreen(actionUpClicked = { finish() }) {

                    Div()
                    this.DeviceInfo()

                    Div()
                    this.ConfigUrl()

                    Div()
                    this.Notifications()

                    Div()
                    TextSection(text = "Release Notes")
                    ButtonPrimary(text = "Release Notes", onClick = {
                        preferenceManager.save(keyReleaseNotesSeenVersion, 1)
                        releaseNotesNavigationComponent.releaseNotesNext()
                    })

                    Div()
                    TextSection(text = "Sync")
                    ButtonPrimary(text = "App startup activity", onClick = {
                        startActivity(applicationNavigationComponent.syncActivityIntent(this@DebugActivity))
                    })

                    Div()
                    this.NetworkRequests()

                    Div()
                }
            }
        }
    }

    @Composable
    private fun DebugScreen(
        actionUpClicked: () -> Unit,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                text = "Debug",
                icon = painterResource(id = R.drawable.ic_back),
                iconContentDescription = null,
                actionUpClicked = actionUpClicked
            )
            Column(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.medium
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                this.content()
            }
        }
    }

    @Composable
    private fun ColumnScope.DeviceInfo() {

        TextSection(text = "Information")
        TextBody2(text = "Useful IDs used for various services in use with the app. Long click to copy the value to clipboard")
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundSecondary)
            .longClick { copyToClipboard(adsRepository.getCurrentDeviceId(applicationContext) ?: "") }
            .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            TextBody1(text = "AD ID:", modifier = Modifier.weight(1f))
            TextBody2(text = adsRepository.getCurrentDeviceId(applicationContext) ?: "", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundSecondary)
            .longClick { copyToClipboard(notificationRepository.remoteNotificationToken ?: "") }
            .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            TextBody1(text = "FCM ID:", modifier = Modifier.weight(1f))
            TextBody2(text = notificationRepository.remoteNotificationToken ?: "", modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundSecondary)
            .longClick { copyToClipboard(deviceRepository.deviceUdid) }
            .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            TextBody1(text = "Device ID:", modifier = Modifier.weight(1f))
            TextBody2(text = deviceRepository.deviceUdid, modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundSecondary)
            .longClick { copyToClipboard(deviceRepository.installationId) }
            .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            TextBody1(text = "Install ID:", modifier = Modifier.weight(1f))
            TextBody2(text = deviceRepository.installationId, modifier = Modifier.weight(1f))
        }
    }

    @Composable
    private fun ColumnScope.ConfigUrl() {
        val configUrl = remember { mutableStateOf(TextFieldValue(baseUrlLocalOverrideManager.localBaseUrl ?: "")) }
        TextSection(text = "Config URL")
        InputPrimary(text = configUrl, placeholder = "https://flashback.pages.dev")
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonPrimary(
                text = "Save",
                modifier = Modifier.weight(1f),
                onClick = {
                    val url = configUrl.value.text
                    baseUrlLocalOverrideManager.localBaseUrl = url
                    toast("Set '${url}' to override")
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            ButtonPrimary(
                text = "Clear",
                modifier = Modifier.weight(1f),
                onClick = {
                    baseUrlLocalOverrideManager.localBaseUrl = null
                    configUrl.value = TextFieldValue("")
                    toast("Cleared URL")
                }
            )
        }
    }

    @Composable
    private fun ColumnScope.Notifications() {
        TextSection(text = "Notifications")
        if (!permissionRepository.isRuntimeNotificationsEnabled) {
            TextBody2(text = "Runtime notifications disabled. Notification will not be delivered")
        } else {
            TextBody2(text = "Runtime notifications enabled")
        }
        ButtonPrimary(
            text = "Send test notification",
            enabled = permissionRepository.isRuntimeNotificationsEnabled,
            onClick = {
                val intent = LocalNotificationBroadcastReceiver.intent(
                    context = applicationContext,
                    channelId = NotificationChannel.RACE.channelId,
                    title = "This is a debug notification!",
                    description = "This is a long description inside the notification"
                )
                sendBroadcast(intent)
            }
        )
    }

    @Composable
    private fun ColumnScope.NetworkRequests() {
        TextSection(text = "Requests")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ButtonTertiary(text = "overview.json", onClick = { syncOverview() })
            ButtonTertiary(text = "drivers.json", onClick = { syncDrivers() })
            ButtonTertiary(text = "constructors.json", onClick = { syncConstructors() })
            ButtonTertiary(text = "circuits.json", onClick = { syncCircuits() })
        }
    }

    @Composable
    private fun ColumnScope.Div() {
        Spacer(modifier = Modifier.height(12.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))
    }

    private fun Modifier.longClick(longClick: () -> Unit): Modifier {
        return this.combinedClickable(
            onClick = {},
            onLongClick = longClick
        )
    }

    private fun syncOverview() {
        GlobalScope.launch {
            log("Sending request")
            val result = overviewRepository.fetchOverview()
            log("Result $result")
            runOnUiThread {
                Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun syncDrivers() {
        GlobalScope.launch {
            log("Sending request")
            val result = driverRepository.fetchDrivers()
            log("Result $result")
            runOnUiThread {
                Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun syncConstructors() {
        GlobalScope.launch {
            log("Sending request")
            val result = constructorRepository.fetchConstructors()
            log("Result $result")
            runOnUiThread {
                Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun syncCircuits() {
        GlobalScope.launch {
            log("Sending request")
            val result = circuitRepository.fetchCircuits()
            log("Result $result")
            runOnUiThread {
                Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun log(msg: String) {
        Log.d("Debug", msg)
    }

    private fun toast(msg: String) {
        Toast.makeText(this@DebugActivity, msg, Toast.LENGTH_LONG).show()
    }
}