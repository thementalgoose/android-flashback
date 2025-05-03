@file:OptIn(ExperimentalFoundationApi::class)

package tmg.flashback.sandbox.core.presentation

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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.sandbox.manager.BaseUrlLocalOverrideManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.data.repo.CircuitRepository
import tmg.flashback.data.repo.ConstructorRepository
import tmg.flashback.data.repo.DriverRepository
import tmg.flashback.data.repo.OverviewRepository
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.notifications.receiver.LocalNotificationBroadcastReceiver
import tmg.flashback.notifications.repository.NotificationIdsRepository
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.flashbackapi.api.NetworkConfigManager
import tmg.flashback.season.usecases.ContentSyncUseCase
import tmg.flashback.season.usecases.ScheduleNotificationsUseCase
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.utilities.extensions.copyToClipboard
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class SandboxActivity: BaseActivity() {

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
    lateinit var networkConfigManager: NetworkConfigManager

    @Inject
    lateinit var deviceRepository: DeviceRepository
    @Inject
    lateinit var adsRepository: AdsRepository
    @Inject
    lateinit var notificationIdsRepository: NotificationIdsRepository

    @Inject
    lateinit var scheduleNotificationsUseCase: ScheduleNotificationsUseCase
    @Inject
    lateinit var contentSyncUseCase: ContentSyncUseCase

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var applicationNavigationComponent: ApplicationNavigationComponent

    @Inject
    lateinit var permissionRepository: PermissionRepository


    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold { paddingValues ->
                    DebugScreen(
                        paddingValues = paddingValues,
                        actionUpClicked = { finish() }
                    ) {

                        Div()
                        this.DeviceInfo()

                        Div()
                        this.ConfigUrl()

                        Div()
                        this.Notifications()

                        Div()
                        this.Scheduling()

                        Div()
                        TextSection(text = "Sync")
                        ButtonPrimary(text = "App startup activity", onClick = {
                            startActivity(applicationNavigationComponent.syncActivityIntent(this@SandboxActivity))
                        })

                        Div()
                        this.NetworkRequests()

                        Div()
                    }
                }
            }
        }
    }

    @Composable
    private fun DebugScreen(
        actionUpClicked: () -> Unit,
        paddingValues: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                text = "Debug",
                action = HeaderAction.BACK,
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
            .longClick { copyToClipboard(notificationIdsRepository.remoteNotificationToken ?: "") }
            .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            TextBody1(text = "FCM ID:", modifier = Modifier.weight(1f))
            TextBody2(text = notificationIdsRepository.remoteNotificationToken ?: "", modifier = Modifier.weight(1f))
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
        TextBody2(text = "Input full config URL, or 8 digit hex code from cloudflare deployment")
        InputPrimary(text = configUrl, placeholder = "https://flashback.pages.dev")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonPrimary(
                text = "Save",
                modifier = Modifier.weight(1f),
                onClick = {
                    val url = configUrl.value.text
                    if (url.length == 8 && url.matches(Regex("[a-f0-9A-F]{8}"))) {
                        configUrl.applyConfigUrl("https://${url}.flashback.pages.dev")
                    } else {
                        configUrl.applyConfigUrl(url)
                    }
                }
            )
            ButtonPrimary(
                text = "Clear",
                modifier = Modifier.weight(1f),
                onClick = {
                    configUrl.applyConfigUrl(null)
                }
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonSecondary(
                text = networkConfigManager.defaultBaseUrl,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    configUrl.applyConfigUrl(null)
                }
            )
            ButtonSecondary(
                text = "https://sand.flashback.pages.dev",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    configUrl.applyConfigUrl("https://sand.flashback.pages.dev")
                }
            )
        }
    }

    private fun MutableState<TextFieldValue>.applyConfigUrl(
        url: String?,
        showAs: String = url ?: networkConfigManager.defaultBaseUrl
    ) {
        baseUrlLocalOverrideManager.localBaseUrl = url
        this.value = TextFieldValue(url ?: "")
        toast("Applying url: '$showAs'")
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
                    channelId = "notify_race",
                    title = "This is a debug notification!",
                    description = "This is a long description inside the notification"
                )
                sendBroadcast(intent)
            }
        )
    }

    @Composable
    private fun ColumnScope.Scheduling() {
        TextSection(text = "Scheduling")
        ButtonPrimary(
            text = "Schedule content sync",
            onClick = {
                contentSyncUseCase.scheduleNow(5L)
            }
        )
        ButtonPrimary(
            text = "Schedule alarms",
            onClick = {
                scheduleNotificationsUseCase.schedule()
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
        Toast.makeText(this@SandboxActivity, msg, Toast.LENGTH_LONG).show()
    }
}