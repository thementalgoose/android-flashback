package tmg.flashback.ui.dashboard.season.viewholders

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultFillFormatter
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.LegendRenderer
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlinx.android.synthetic.main.view_dashboard_season_graph.view.*


class GraphViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    init {
        setupChart()
    }

    fun bind() {

        val lineDataSet = LineDataSet(
            listOf(
                Entry(0f, 0f),
                Entry(1f, 25f),
                Entry(2f, 43f),
                Entry(3f, 51f),
                Entry(4f, 73f)
            ), "Points"
        )
        lineDataSet.color = Color.BLUE
        lineDataSet.isHighlightEnabled = false
        lineDataSet.valueTextColor = Color.BLACK

        val lineDataSet2 = LineDataSet(
            listOf(
                Entry(0f, 0f),
                Entry(1f, 18f),
                Entry(2f, 19f),
                Entry(3f, 23f),
                Entry(4f, 94f)
            ), "Bottas"
        )
        lineDataSet2.valueFormatter = DefaultValueFormatter(0)
        lineDataSet2.color = Color.GREEN
        lineDataSet2.isHighlightEnabled = false
        lineDataSet2.setDrawCircles(false)
        lineDataSet2.setDrawCircleHole(false)
        lineDataSet2.valueTextColor = Color.BLACK

        val lineData = LineData(
            lineDataSet,
            lineDataSet2
        )

        itemView.chart.isLogEnabled = true
        itemView.chart.data = lineData

        itemView.chart.invalidate()
    }

    private fun setupChart() {
        itemView.chart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)

            legend.isEnabled = true

            isDragEnabled = false
            isScaleXEnabled = false
            isScaleYEnabled = false
            setDrawBorders(false)
            setDrawMarkers(false)
            setDrawGridBackground(false)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.disableGridDashedLine()
            xAxis.disableAxisLineDashedLine()

            axisLeft.setDrawAxisLine(false)

            axisRight.isEnabled = false
            axisRight.enableGridDashedLine(10f, 10f, 0f)
        }
    }

    private fun setupData() {

    }
}