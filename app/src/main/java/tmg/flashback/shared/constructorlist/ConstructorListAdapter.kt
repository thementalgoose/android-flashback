package tmg.flashback.shared.constructorlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.R
import tmg.flashback.repo.models.stats.SlimConstructor

class ConstructorListAdapter: RecyclerView.Adapter<ConstructorListViewHolder>() {

    var list: List<SlimConstructor> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstructorListViewHolder {
        return ConstructorListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_driver_constructors, parent, false))
    }

    override fun onBindViewHolder(holder: ConstructorListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}