package tmg.flashback.statistics.ui.dashboard.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewSeasonListHeroBinding

class HeroViewHolder(
    private val binding: ViewSeasonListHeroBinding,
    private val settingsClicked: () -> Unit
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.settingsButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        settingsClicked()
    }
}