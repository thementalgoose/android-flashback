package tmg.f1stats.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import tmg.f1stats.R
import tmg.utilities.extensions.dpToPx
import kotlin.math.roundToInt

class RaceNumberView: TextView {
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

    private val horizontalPadding: Int = 2f.dpToPx(context.resources).roundToInt()
    private val verticalPadding: Int = 2f.dpToPx(context.resources).roundToInt()

    @ColorInt
    var colorHighlight: Int = Color.BLUE
        set(value) {
            setShadowLayer(8f, 0f, 0f, colorHighlight)
            field = value
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
    }
}