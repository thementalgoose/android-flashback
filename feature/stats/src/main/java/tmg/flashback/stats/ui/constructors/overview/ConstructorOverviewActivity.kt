package tmg.flashback.stats.ui.constructors.overview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity

class ConstructorOverviewActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val constructorId: String = intent.extras?.getString(keyConstructorId)!!
        val constructorName: String = intent.extras?.getString(keyConstructorName)!!

        setContent {
            AppTheme {
                ConstructorOverviewScreenVM(
                    constructorId = constructorId,
                    constructorName = constructorName,
                    actionUpClicked = { }
                )
            }
        }
    }

    companion object {

        private const val keyConstructorId: String = "constructorId"
        private const val keyConstructorName: String = "constructorName"

        fun intent(context: Context, constructorId: String, constructorName: String): Intent {
            return Intent(context, ConstructorOverviewActivity::class.java).apply {
                putExtra(keyConstructorId, constructorId)
                putExtra(keyConstructorName, constructorName)
            }
        }
    }
}