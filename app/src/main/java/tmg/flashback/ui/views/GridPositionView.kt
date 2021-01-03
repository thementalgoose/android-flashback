package tmg.flashback.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import tmg.flashback.R
import tmg.utilities.extensions.dpToPx

class GridPositionView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs, defStyleAttr)
    }

    private var boxWidth: Float = 0.68f
    private var referenceWidth: Float = 0.5f

    private val boxPaint: Paint = Paint().apply { this.style = Paint.Style.STROKE }
    private val referencePaint: Paint = Paint().apply { this.style = Paint.Style.STROKE }

    private var cWidth: Float = 0f
    private var cHeight: Float = 0f

    private var firstRun: Boolean = true

    @ColorInt
    var boxColor: Int = Color.WHITE
        set(value) {
            field = value
            boxPaint.color = value
        }
    var boxLineWidth: Float = 2f.dpToPx(context.resources)
        set(value) {
            field = value
            boxPaint.strokeWidth = value
        }

    var referenceLineFromTop: Float = 6f.dpToPx(context.resources)
    @ColorInt
    var referenceColor: Int = Color.YELLOW
        set(value) {
            field = value
            referencePaint.color = value
        }
    var referenceLineWidth: Float = 2f.dpToPx(context.resources)
        set(value) {
            field = value
            referencePaint.strokeWidth = value
        }



    private fun initView(attributeSet: AttributeSet?, defStyleAttr: Int = -1) {
        context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.GridPositionView, defStyleAttr, 0)
            .apply {
                try {
                    boxColor = getColor(R.styleable.GridPositionView_boxColor, boxColor)
                    boxWidth = getFloat(R.styleable.GridPositionView_boxWidth, boxWidth)
                    boxLineWidth = getDimension(R.styleable.GridPositionView_boxLineWidth, boxLineWidth)
                    referenceColor = getColor(R.styleable.GridPositionView_referenceColor, referenceColor)
                    referenceWidth = getFloat(R.styleable.GridPositionView_referenceWidth, referenceWidth)
                    referenceLineWidth = getDimension(R.styleable.GridPositionView_boxLineWidth, referenceLineWidth)
                    referenceLineFromTop = getDimension(R.styleable.GridPositionView_referenceLineFromTop, referenceLineFromTop)
                } finally {
                    recycle()
                }
            }
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (firstRun) {
            cWidth = width.toFloat()
            cHeight = height.toFloat()
            firstRun = false
        }

        canvas?.drawLine(boxLineWidth / 2f, 0f, boxLineWidth / 2f, cHeight, boxPaint)
        canvas?.drawLine(0f, boxLineWidth / 2f, cWidth * boxWidth - (boxLineWidth / 2f), boxLineWidth / 2f, boxPaint)
        canvas?.drawLine(cWidth * boxWidth - (boxLineWidth / 2f), 0f, cWidth * boxWidth - (boxLineWidth / 2f), cHeight, boxPaint)

        canvas?.drawLine((1f - referenceWidth) * cWidth, referenceLineFromTop, cWidth, referenceLineFromTop, referencePaint)
    }
}