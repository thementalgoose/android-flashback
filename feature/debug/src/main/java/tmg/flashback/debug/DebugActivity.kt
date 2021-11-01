package tmg.flashback.debug

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import tmg.core.ui.base.BaseActivity
import tmg.flashback.debug.databinding.ActivityDebugBinding
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.notifications.receiver.LocalNotificationBroadcastReceiver

class DebugActivity: BaseActivity() {

    private lateinit var binding: ActivityDebugBinding

    private val overviewRepository: OverviewRepository by inject()
    private val circuitRepository: CircuitRepository by inject()
    private val driverRepository: DriverRepository by inject()
    private val constructorRepository: ConstructorRepository by inject()

    @Suppress("EXPERIMENTAL_API_USAGE")
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

        binding.networkOverview.setOnClickListener {
            GlobalScope.launch {
                log("Sending request")
                val result = overviewRepository.fetchOverview()
                log("Result $result")
                runOnUiThread {
                    Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.networkDrivers.setOnClickListener {
            GlobalScope.launch {
                log("Sending request")
                val result = driverRepository.fetchDrivers()
                log("Result $result")
                runOnUiThread {
                    Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.networkConstructors.setOnClickListener {
            GlobalScope.launch {
                log("Sending request")
                val result = constructorRepository.fetchConstructors()
                log("Result $result")
                runOnUiThread {
                    Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.networkCircuits.setOnClickListener {
            GlobalScope.launch {
                log("Sending request")
                val result = circuitRepository.fetchCircuits()
                log("Result $result")
                runOnUiThread {
                    Toast.makeText(applicationContext, "Result $result", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun log(msg: String) {
        Log.d("Debug", msg)
    }
}