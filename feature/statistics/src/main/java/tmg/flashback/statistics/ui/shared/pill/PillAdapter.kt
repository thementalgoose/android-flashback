package tmg.flashback.statistics.ui.shared.pill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.databinding.ViewLinkPillBinding

class PillAdapter(
        private val pillClicked: (PillItem) -> Unit
): RecyclerView.Adapter<PillViewHolder>() {

    var list: List<PillItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PillViewHolder(
                pillClicked,
                ViewLinkPillBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PillViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}