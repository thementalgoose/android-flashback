package tmg.flashback.debug

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.views.NativeBanner
import tmg.flashback.debug.adverts.AdvertsActivity
import tmg.flashback.debug.compose.ComposeActivity
import tmg.flashback.debug.styleguide.StyleGuideActivity
import tmg.flashback.debug.styleguide.StyleGuideComposeActivity
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.notifications.receiver.LocalNotificationBroadcastReceiver
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.navigation.ApplicationNavigationProvider
import tmg.utilities.extensions.copyToClipboard

@SuppressLint("SetTextI18n")
class DebugActivity: BaseActivity() {

    private val overviewRepository: OverviewRepository by inject()
    private val circuitRepository: CircuitRepository by inject()
    private val driverRepository: DriverRepository by inject()
    private val constructorRepository: ConstructorRepository by inject()

    private val baseUrlLocalOverrideManager: BaseUrlLocalOverrideManager by inject()

    private val deviceRepository: DeviceRepository by inject()
    private val adsManager: AdsManager by inject()
    private val notificationRepository: NotificationRepository by inject()

    private val applicationNavigationProvider: ApplicationNavigationProvider by inject()

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val configUrl = remember { mutableStateOf(TextFieldValue(baseUrlLocalOverrideManager.localBaseUrl ?: "")) }
                DebugLayout(
                    adId = adsManager.getCurrentDeviceId(applicationContext) ?: "",
                    fcmId = notificationRepository.remoteNotificationToken ?: "",
                    deviceUdid = deviceRepository.deviceUdid,
                    configUrl = configUrl,
                    adIdClicked = {
                        copyToClipboard(it, it)
                    },
                    fcmIdClicked = {
                        copyToClipboard(it, it)
                    },
                    deviceUdidClicked = {
                        copyToClipboard(it, it)
                    },
                    configUrlSave = {
                        val url = configUrl.value.text
                        baseUrlLocalOverrideManager.localBaseUrl = url
                        Toast.makeText(this, "Set '${url}' to local override url", Toast.LENGTH_LONG).show()
                    },
                    configUrlClear = {
                        baseUrlLocalOverrideManager.localBaseUrl = null
                        configUrl.value = TextFieldValue("")
                        Toast.makeText(this, "Cleared local override url", Toast.LENGTH_LONG).show()
                    },
                    syncClicked = {
                        startActivity(applicationNavigationProvider.syncActivityIntent(this))
                    },
                    styleGuideLegacyClicked = {
                        startActivity(Intent(this, StyleGuideActivity::class.java))
                    },
                    styleGuideComposeClicked = {
                        startActivity(Intent(this, StyleGuideComposeActivity::class.java))
                    },
                    composeTestClicked = {
                        startActivity(Intent(this, ComposeActivity::class.java))
                    },
                    sendNotificationClicked = {
                        val intent = LocalNotificationBroadcastReceiver.intent(
                            context = applicationContext,
                            channelId = "race",
                            title = "This is a debug notification!",
                            description = "This is a long description inside the notification"
                        )
                        sendBroadcast(intent)
                    },
                    networkRequestClicked = {
                        when (it) {
                            "overview.json" -> syncOverview()
                            "drivers.json" -> syncDrivers()
                            "constructors.json" -> syncConstructors()
                            "circuits.json" -> syncCircuits()
                        }
                    },
                    advertConfigClicked = {
                        startActivity(Intent(this, AdvertsActivity::class.java))
                    },
                    nativeView = {
                        AndroidView(factory = { context ->
                            NativeBanner(context)
                        })
                    }
                )
            }
        }
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