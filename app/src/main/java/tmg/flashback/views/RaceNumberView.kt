package tmg.flashback.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import tmg.flashback.R
import tmg.utilities.extensions.dpToPx
import kotlin.math.roundToInt

class RaceNumberView: androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs, defStyleAttr)
    }

    private val horizontalPadding: Int = 3f.dpToPx(context.resources).roundToInt()
    private val verticalPadding: Int = 2f.dpToPx(context.resources).roundToInt()

    @ColorInt
    var colorHighlight: Int = ContextCompat.getColor(context, R.color.colorTheme)
        set(value) {
            field = value
            setShadowLayer(10f, 0f, 0f, value)
            invalidate()
        }

    private fun initView(attributeSet: AttributeSet?, defStyleAttr: Int = -1) {
        context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.GridPositionView, defStyleAttr, 0)
            .apply {
                try {
                    colorHighlight = getColor(R.styleable.GridPositionView_boxColor, colorHighlight)
                } finally {
                    recycle()
                }
            }
        typeface = ResourcesCompat.getFont(context, R.font.yukarimobil)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        setTextColor(Color.WHITE)
    }
}