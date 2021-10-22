package tmg.flashback.debug

import android.os.Bundle
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.debug.databinding.ActivityDebugBinding
import tmg.notifications.receiver.LocalNotificationBroadcastReceiver

class DebugActivity: BaseActivity() {

    private lateinit var binding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendNotification.setOnClickListener {
            val intent = LocalNotificationBroadcastReceiver.intent(
                context = applicationContext,
                channelId = "race",
                title = "This is a debug notification!",
                description = "This is a long description inside the notification"
            )
            sendBroadcast(intent)
        }
    }
}