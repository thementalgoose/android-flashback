package tmg.flashback.statistics.ui.shared.constructorlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.formula1.model.SlimConstructor
import tmg.flashback.statistics.databinding.ViewDriverConstructorsBinding

class ConstructorListAdapter: RecyclerView.Adapter<ConstructorListViewHolder>() {

    var list: List<SlimConstructor> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstructorListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ConstructorListViewHolder(
            ViewDriverConstructorsBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ConstructorListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}