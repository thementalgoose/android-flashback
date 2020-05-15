package tmg.flashback.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import tmg.flashback.R
import tmg.labelledprogressbar.dpToPx
import kotlin.math.max
import kotlin.math.min

private const val defaultBarSize: Float = 16f
private const val defaultBackgroundColour: Int = Color.LTGRAY
private const val defaultBarColour: Int = Color.CYAN
private const val defaultDuration: Long = 600L

class ArcView: View, ValueAnimator.AnimatorUpdateListener {

    companion object {

        private const val startAngle: Float = 120f
        private const val endAngle: Float = 300f
    }

    var barWidth: Float = defaultBarSize.dpToPx(context)
        set(value) {
            field = value
            barPaint.strokeWidth = value
            backgroundPaint.strokeWidth = value
        }

    @ColorInt
    var backgroundColour: Int = defaultBackgroundColour
        set(value) {
            field = value
            backgroundPaint.color = value
        }

    @ColorInt
    var barColour: Int = defaultBarColour
        set(value) {
            field = value
            barPaint.color = value
        }

    var duration: Long = defaultDuration

    private var progress: Float = 0.5f

    private var cWidth: Float = 0f
    private var cHeight: Float = 0f
    private var bounds: RectF = RectF()
    private var firstRun: Boolean = true

    private val backgroundPaint: Paint = Paint().apply { this.style = Paint.Style.STROKE }
    private val barPaint: Paint = Paint().apply { this.style = Paint.Style.STROKE }
    private lateinit var valueAnimator: ValueAnimator

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

    private fun initView(attrs: AttributeSet?, defStyleAttr: Int = 0) {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.ArcView, defStyleAttr, 0)
            .apply {
                try {
                    barColour = getColor(R.styleable.ArcView_av_barColour, barColour)
                    backgroundColour = getColor(R.styleable.ArcView_av_backgroundColour, backgroundColour)
                    barWidth = getDimension(R.styleable.ArcView_av_barSize, barWidth)
                } finally {
                    recycle()
                }
            }
    }

    //region Methods

    fun setProgress(progress: Float) {
        this.progress = progress.coerceIn(0.0f, 1.0f)
    }

    fun animateProgress(withProgress: Float, fromZero: Boolean = true) {
        if (fromZero) {
            progress = 0.0f
        }
        valueAnimator = ValueAnimator.ofFloat(progress, withProgress)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = (duration)
        valueAnimator.addUpdateListener(this)
        valueAnimator.start()
    }

    //endregion

    private fun initFirstRun() {
        cWidth = width.toFloat()
        cHeight = height.toFloat()
        bounds = RectF(
            barWidth / 2f,
            barWidth / 2f,
            cWidth - (barWidth / 2f),
            cHeight - (barWidth / 2f)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (firstRun) {
            initFirstRun()
            firstRun = false
        }

        canvas?.drawArc(bounds, startAngle, endAngle, false, backgroundPaint)
        canvas?.drawArc(bounds, startAngle, endAngle * progress, false, barPaint)
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        if (animation == valueAnimator) {
            progress = animation.animatedValue as Float
            invalidate()
        }
    }
}