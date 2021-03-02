package tmg.flashback.core.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class CircleView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var circleColour: Int? = null
    private val circlePaint: Paint = Paint().apply {
        isAntiAlias = true
    }

    fun setCircleColour(@ColorInt circleColour: Int) {
        this.circleColour = circleColour
        circlePaint.color = circleColour
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        circleColour?.let {
            canvas?.drawOval(0f, 0f, width.toFloat(), height.toFloat(), circlePaint)
        }
    }
}