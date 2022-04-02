package tmg.flashback.statistics.ui.weekend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Scaffold
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.base.BaseActivity

class WeekendActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content: OverviewRace = intent.extras?.getParcelable(keyOverviewRace)!!

        setContent {
            AppTheme {
                Scaffold(content = {
                    WeekendScreen(
                        model = content,
                        backClicked = {
                            finish()
                        }
                    )
                })
            }
        }
    }

    companion object {
        private const val keyOverviewRace = "overviewRace"

        fun intent(context: Context, overviewRace: OverviewRace): Intent {
            return Intent(context, WeekendActivity::class.java).apply {
                putExtra(keyOverviewRace, overviewRace)
            }
        }
    }
}