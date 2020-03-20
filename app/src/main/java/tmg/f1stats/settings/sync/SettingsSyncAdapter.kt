package tmg.f1stats.settings.sync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_sync.view.*
import tmg.f1stats.R
import tmg.f1stats.repo.models.Sync
import tmg.utilities.difflist.DiffListAdapter
import tmg.utilities.difflist.DiffListComparator

class SettingsSyncAdapter: DiffListAdapter<Sync, ViewHolder>(SyncComparator()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Sync) {
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_sync, parent, false))
    }
}


class SyncComparator: DiffListComparator<Sync> {
    override fun areContentsTheSame(o1: Sync, o2: Sync): Boolean {
        return o1.progress == o2.progress
    }

    override fun areItemsTheSame(o1: Sync, o2: Sync): Boolean {
        return o1.id == o2.id
    }
}

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(model: Sync) {
        itemView.tvProgress.text = model.progress.type
        if (model.round != null) {
            itemView.tvDescription.text = "Season ${model.season} Round ${model.round} ${model.errorMsg ?: ""}"
        }
        else if (model.round != null) {
            itemView.tvDescription.text = "Season ${model.season} ${model.errorMsg ?: ""}"
        }
    }
}
