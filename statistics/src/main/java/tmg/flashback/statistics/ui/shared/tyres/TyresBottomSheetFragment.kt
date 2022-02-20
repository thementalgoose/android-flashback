package tmg.flashback.statistics.ui.shared.tyres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.threeten.bp.Year
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentBottomSheetTyresBinding
import tmg.flashback.ui.base.BaseBottomSheetFragment
import java.lang.NullPointerException

class TyresBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetTyresBinding>() {

    private val crashController: CrashController by inject()

    private val adapter: TyresAdapter = TyresAdapter()

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetTyresBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val season = arguments?.getInt(keySeason)
        logScreenViewed("Tyres Breakdown", mapOf(
            "season" to (season?.toString() ?: "null")
        ))

        val tyre = SeasonTyres.getBySeason(season ?: Year.now().value)
        if (season == null) {
            crashController.logException(NullPointerException("Season passed to tyres breakdown as null, or tyres found to be null"))
        }

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        binding.title.text = getString(R.string.tyres_list_title, season.toString())
        adapter.list = mutableListOf<TyreItem>().apply {
            add(TyreItem.Header(R.string.tyres_dry_compounds))
            addAll((tyre?.tyres ?: emptyList())
                .filter { it.tyre.isDry }
                .map { TyreItem.Item(it) }
            )
            add(TyreItem.Header(R.string.tyres_wet_compounds))
            addAll((tyre?.tyres ?: emptyList())
                .filter { !it.tyre.isDry }
                .map { TyreItem.Item(it) }
            )
        }
    }

    companion object {

        private const val keySeason = "season"

        fun instance(season: Int) = TyresBottomSheetFragment().apply {
            arguments = bundleOf(keySeason to season)
        }
    }
}