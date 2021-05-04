package tmg.flashback.shared.ui.ui.theme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.shared.ui.base.BaseBottomSheetFragment
import tmg.flashback.shared.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.databinding.FragmentBottomSheetThemeBinding
import tmg.flashback.ui.SplashActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ThemeBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetThemeBinding>() {

    private val viewModel: ThemeViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetThemeBinding.inflate(inflater)

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
                activity?.let {
                    it.finishAffinity()
                    startActivity(Intent(it, SplashActivity::class.java))
                }
            }
            dismiss()
        }
    }

}