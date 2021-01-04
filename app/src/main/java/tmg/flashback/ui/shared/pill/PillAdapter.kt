package tmg.flashback.ui.shared.pill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R

class PillAdapter(
        private val pillClicked: (PillItem) -> Unit
): RecyclerView.Adapter<PillViewHolder>() {

    var list: MutableList<PillItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillViewHolder {
        return PillViewHolder(
                pillClicked,
                LayoutInflater.from(parent.context).inflate(R.layout.view_link_pill, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PillViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}