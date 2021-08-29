package tmg.common.ui.settings.appearance.nightmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.databinding.FragmentBottomSheetNightModeBinding
import tmg.common.databinding.FragmentBottomSheetThemeBinding
import tmg.core.ui.base.BaseBottomSheetFragment
import tmg.core.ui.bottomsheet.BottomSheetAdapter
import tmg.core.ui.model.NightMode
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class NightModeBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetNightModeBinding>() {

    private val viewModel: NightMoveViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetNightModeBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Appearance Night Mode Picker")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
                itemClicked = {
                    viewModel.inputs.selectNightMode(NightMode.values()[it.id])
                }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.themePreferences) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.nightModeUpdated) { (value, isSameSelection) ->
            if (!isSameSelection) {
                when (value) {
                    NightMode.DEFAULT -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                    NightMode.DAY -> setDefaultNightMode(MODE_NIGHT_NO)
                    NightMode.NIGHT -> setDefaultNightMode(MODE_NIGHT_YES)
                }
            }
            dismiss()
        }
    }

}