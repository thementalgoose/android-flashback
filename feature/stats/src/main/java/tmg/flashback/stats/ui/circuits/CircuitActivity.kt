package tmg.flashback.stats.ui.circuits

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class CircuitActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val circuitId: String = (intent?.extras ?: savedInstanceState)?.getString(keyCircuitId)!!
        val circuitName: String = (intent?.extras ?: savedInstanceState)?.getString(keyCircuitName)!!
        setContent {
            AppTheme {
                CircuitScreenVM(
                    circuitId = circuitId,
                    circuitName = circuitName
                )
            }
        }
    }

    companion object {

        private const val keyCircuitId: String = "circuitId"
        private const val keyCircuitName: String = "circuitName"

        fun intent(context: Context, circuitId: String, circuitName: String): Intent {
            return Intent(context, CircuitActivity::class.java).also {
                it.putExtra(keyCircuitId, circuitId)
                it.putExtra(keyCircuitName, circuitName)
            }
        }
    }
}