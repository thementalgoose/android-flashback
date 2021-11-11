package tmg.flashback.common.ui.releasenotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.common.constants.ReleaseNotes
import tmg.flashback.common.databinding.ViewReleaseNoteBinding
import tmg.utilities.difflist.GenericDiffCallback

class ReleaseAdapter: RecyclerView.Adapter<ReleaseViewHolder>() {

    var list: List<ReleaseNotes> = emptyList()
        set(value) {
            val result = DiffUtil.calculateDiff(GenericDiffCallback(field, value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReleaseViewHolder(ViewReleaseNoteBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ReleaseViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}