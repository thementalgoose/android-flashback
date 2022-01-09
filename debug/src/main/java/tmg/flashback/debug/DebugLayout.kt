package tmg.flashback.debug

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import tmg.flashback.ads.views.NativeBanner
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.style.text.TextSection

@Composable
fun DebugLayout(
    adId: String,
    fcmId: String,
    deviceUdid: String,
    configUrl: MutableState<TextFieldValue>,
    adIdClicked: (String) -> Unit,
    fcmIdClicked: (String) -> Unit,
    deviceUdidClicked: (String) -> Unit,
    configUrlSave: () -> Unit,
    configUrlClear: () -> Unit,
    syncClicked: () -> Unit,
    styleGuideLegacyClicked: () -> Unit,
    styleGuideComposeClicked: () -> Unit,
    composeTestClicked: () -> Unit,
    sendNotificationClicked: () -> Unit,
    networkRequestClicked: (String) -> Unit,
    advertConfigClicked: () -> Unit,
    modifier: Modifier = Modifier,
    nativeView: @Composable () -> Unit
) {
//    Scroll
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(
                top = AppTheme.dimensions.paddingXLarge,
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingMedium
            )
    ) {
        TextHeadline1(text = stringResource(id = R.string.debug_title))
        Spacer(modifier = Modifier.height(16.dp))
        TextBody1(text = "Ads ID")
        TextBody2(text = adId, modifier = Modifier
            .fillMaxWidth()
            .clickable { adIdClicked(adId) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextBody1(text = "FCM ID")
        TextBody2(text = fcmId, modifier = Modifier
            .fillMaxWidth()
            .clickable { fcmIdClicked(fcmId) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextBody1(text = "Device UDID")
        TextBody2(text = adId, modifier = Modifier
            .fillMaxWidth()
            .clickable { deviceUdidClicked(deviceUdid) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Sync")
        ButtonPrimary(text = "Sync Clicked", onClick = syncClicked)
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Style Guide")
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonPrimary(
                text = "Style Guide",
                modifier = Modifier.weight(1f),
                onClick = styleGuideLegacyClicked
            )
            Spacer(modifier = Modifier.width(16.dp))
            ButtonPrimary(
                text = "Compose Guide",
                modifier = Modifier.weight(1f),
                onClick = styleGuideComposeClicked
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Compose test")
        ButtonPrimary(text = "Compose test activity", onClick = composeTestClicked)
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Config URL")
        InputPrimary(text = configUrl, placeholder = "https://flashback.pages.dev")
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonPrimary(
                text = "Save",
                modifier = Modifier.weight(1f),
                onClick = configUrlSave
            )

            Spacer(modifier = Modifier.width(16.dp))
            ButtonPrimary(
                text = "Clear",
                modifier = Modifier.weight(1f),
                onClick = configUrlClear
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Notifications")
        ButtonPrimary(text = "Send notification", onClick = sendNotificationClicked)
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Network Requests")
        ButtonPrimary(text = "overview.json", onClick = { networkRequestClicked("overview.json") })
        Spacer(modifier = Modifier.height(4.dp))
        ButtonPrimary(text = "drivers.json", onClick = { networkRequestClicked("drivers.json") })
        Spacer(modifier = Modifier.height(4.dp))
        ButtonPrimary(text = "constructors.json", onClick = { networkRequestClicked("constructors.json") })
        Spacer(modifier = Modifier.height(4.dp))
        ButtonPrimary(text = "circuits.json", onClick = { networkRequestClicked("circuits.json") })
        Spacer(modifier = Modifier.height(16.dp))
        TextSection(text = "Advert")
        ButtonPrimary(text = "Advert config", onClick = advertConfigClicked)
        nativeView()
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        val configInput = remember { mutableStateOf(TextFieldValue("")) }
        DebugLayout(
            adId = "ad-id",
            fcmId = "fcm-id",
            deviceUdid = "device-udid",
            configUrl = configInput,
            adIdClicked = { },
            fcmIdClicked = { },
            deviceUdidClicked = { },
            configUrlSave = { },
            configUrlClear = { },
            syncClicked = { },
            styleGuideLegacyClicked = { },
            styleGuideComposeClicked = { },
            composeTestClicked = { },
            sendNotificationClicked = { },
            networkRequestClicked = { },
            advertConfigClicked = { },
            nativeView = { }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        val configInput = remember { mutableStateOf(TextFieldValue("")) }
        DebugLayout(
            adId = "ad-id",
            fcmId = "fcm-id",
            deviceUdid = "device-udid",
            configUrl = configInput,
            adIdClicked = { },
            fcmIdClicked = { },
            deviceUdidClicked = { },
            configUrlSave = { },
            configUrlClear = { },
            syncClicked = { },
            styleGuideLegacyClicked = { },
            styleGuideComposeClicked = { },
            composeTestClicked = { },
            sendNotificationClicked = { },
            networkRequestClicked = { },
            advertConfigClicked = { },
            nativeView = { }
        )
    }
}
