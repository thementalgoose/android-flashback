package tmg.flashback.debug

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.notifications.receiver.LocalNotificationBroadcastReceiver
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.ui.drivers.stathistory.DriverStatHistoryType
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DebugActivity: BaseActivity() {

    @Inject
    protected lateinit var overviewRepository: OverviewRepository
    @Inject
    protected lateinit var circuitRepository: CircuitRepository
    @Inject
    protected lateinit var driverRepository: DriverRepository
    @Inject
    protected lateinit var constructorRepository: ConstructorRepository

    @Inject
    protected lateinit var baseUrlLocalOverrideManager: BaseUrlLocalOverrideManager

    @Inject
    protected lateinit var deviceRepository: DeviceRepository
    @Inject
    protected lateinit var adsManager: AdsManager
    @Inject
    protected lateinit var notificationRepository: NotificationRepository

    @Inject
    protected lateinit var applicationNavigationComponent: ApplicationNavigationComponent

    @Inject
    protected lateinit var statsNavigationComponent: StatsNavigationComponent

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val configUrl = remember { mutableStateOf(TextFieldValue(baseUrlLocalOverrideManager.localBaseUrl ?: "")) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Header(
                        text = "Debug",
                        icon = painterResource(id = R.drawable.ic_back),
                        iconContentDescription = null,
                        actionUpClicked = { finish() }
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                top = AppTheme.dimens.small,
                                start = AppTheme.dimens.medium,
                                end = AppTheme.dimens.medium,
                                bottom = AppTheme.dimens.medium
                            )
                    ) {
                        Text(
                            label = "ADs ID",
                            value = adsManager.getCurrentDeviceId(applicationContext) ?: ""
                        )
                        Text(
                            label = "FCM ID",
                            value = notificationRepository.remoteNotificationToken ?: ""
                        )
                        Text(
                            label = "Device UDID",
                            value = deviceRepository.deviceUdid
                        )

                        TextSection(
                            text = "Config URL",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        InputPrimary(text = configUrl, placeholder = "https://flashback.pages.dev")
                        Spacer(modifier = Modifier.height(4.dp))
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
                                    Toast.makeText(
                                        this@DebugActivity,
                                        "Set '${url}' to local override url",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )

                            Spacer(modifier = Modifier.width(16.dp))
                            ButtonPrimary(
                                text = "Clear",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    baseUrlLocalOverrideManager.localBaseUrl = null
                                    configUrl.value = TextFieldValue("")
                                    Toast.makeText(
                                        this@DebugActivity,
                                        "Cleared local override url",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }

                        Button(
                            label = "Driver Stats History",
                            value = "Wins",
                            buttonClicked = {
                                statsNavigationComponent.driverStatHistory(
                                    "vettel",
                                    "Sebastian Vettel",
                                    DriverStatHistoryType.PODIUMS
                                )
                            }
                        )
                        Button(
                            label = null,
                            value = "Poles",
                            buttonClicked = {
                                statsNavigationComponent.driverStatHistory(
                                    "vettel",
                                    "Sebastian Vettel",
                                    DriverStatHistoryType.POLES
                                )
                            }
                        )
                        Button(
                            label = null,
                            value = "Championships",
                            buttonClicked = {
                                statsNavigationComponent.driverStatHistory(
                                    "vettel",
                                    "Sebastian Vettel",
                                    DriverStatHistoryType.CHAMPIONSHIPS
                                )
                            }
                        )

                        Button(
                            label = "Sync",
                            value = "Sync Activity",
                            buttonClicked = {
                                startActivity(applicationNavigationComponent.syncActivityIntent(this@DebugActivity))
                            }
                        )
                        Button(
                            label = "Notifications",
                            value = "Test Notification",
                            buttonClicked = {
                                val intent = LocalNotificationBroadcastReceiver.intent(
                                    context = applicationContext,
                                    channelId = "race",
                                    title = "This is a debug notification!",
                                    description = "This is a long description inside the notification"
                                )
                                sendBroadcast(intent)
                            }
                        )
                        Button(
                            label = "Network Request",
                            value = "overview.json",
                            buttonClicked = { syncOverview() }
                        )
                        Button(
                            label = null,
                            value = "drivers.json",
                            buttonClicked = { syncDrivers() }
                        )
                        Button(
                            label = null,
                            value = "constructors.json",
                            buttonClicked = { syncConstructors() }
                        )
                        Button(
                            label = null,
                            value = "circuits.json",
                            buttonClicked = { syncCircuits() }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ColumnScope.Text(
        label: String,
        value: String
    ) {
        TextSection(
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(vertical = 4.dp),
            text = label
        )
        TextBody1(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            text = value
        )
    }

    @Composable
    private fun ColumnScope.Button(
        label: String?,
        value: String,
        buttonClicked: () -> Unit,
    ) {
        label?.let {
            TextSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(vertical = 4.dp),
                text = label
            )
        }
        ButtonPrimary(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            text = value,
            onClick = buttonClicked
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
}