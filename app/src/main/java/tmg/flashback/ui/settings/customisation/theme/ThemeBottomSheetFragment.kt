package tmg.flashback.ui.settings.customisation.theme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_bottom_sheet_theme.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.dsl.koinApplication
import tmg.flashback.R
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.ui.BaseBottomSheetFragment
import tmg.flashback.core.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.ui.SplashActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ThemeBottomSheetFragment: BaseBottomSheetFragment() {

    private val viewModel: ThemeViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun layoutId() = R.layout.fragment_bottom_sheet_theme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
                itemClicked = {
                    viewModel.inputs.selectTheme(Theme.values()[it.id])
                }
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.themePreferences) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.themeUpdated) { (_, isSameSelection) ->
            if (!isSameSelection) {
                activity?.let {
                    it.finishAffinity()
                    startActivity(Intent(it, SplashActivity::class.java))
                }
            }
            dismiss()
        }
    }

}