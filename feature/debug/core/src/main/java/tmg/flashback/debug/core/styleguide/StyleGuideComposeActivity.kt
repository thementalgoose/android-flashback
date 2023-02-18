package tmg.flashback.debug.core.styleguide

import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import javax.inject.Inject

@AndroidEntryPoint
class StyleGuideComposeActivity: BaseActivity() {

    @Inject
    lateinit var changeNightModeUseCase: ChangeNightModeUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                StyleGuideComposeLayout(
                    actionUpClicked = {
                        finish()
                    },
                    changeNightMode = {
                        when (styleManager.isDayMode) {
                            true -> changeNightModeUseCase.setNightMode(NightMode.NIGHT)
                            false -> changeNightModeUseCase.setNightMode(NightMode.DAY)
                        }
                    }
                )
            }
        }
    }
}