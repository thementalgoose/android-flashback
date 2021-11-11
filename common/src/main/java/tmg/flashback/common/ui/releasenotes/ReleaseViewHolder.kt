package tmg.flashback.common.ui.releasenotes

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.common.constants.ReleaseNotes
import tmg.flashback.common.databinding.ViewReleaseNoteBinding
import tmg.utilities.extensions.views.getString

class ReleaseViewHolder(
    private val binding: ViewReleaseNoteBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ReleaseNotes) {
        val title = getString(item.title)
        binding.version.text = "${item.versionName} $title"
        binding.content.setText(item.release)
    }
}