package tmg.flashback.settings.ui.settings.appearance.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.settings.R
import tmg.flashback.settings.databinding.FragmentBottomSheetThemeBinding
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.ui.model.Theme
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

internal class ThemeBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetThemeBinding>() {

    private val viewModel: ThemeViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetThemeBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Appearance Color Theme Picker")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.selectTheme(Theme.values()[it.id])
            }
        )

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.themePreferences) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.themeUpdated) { (_, isSameSelection) ->
            if (!isSameSelection) {
                Toast.makeText(context, R.string.settings_theme_theme_applied_later, Toast.LENGTH_LONG).show()
            }
            dismiss()
        }
    }

}