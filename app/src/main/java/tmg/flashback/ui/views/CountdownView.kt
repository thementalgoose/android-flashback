package tmg.flashback.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import tmg.flashback.R

class CountdownView: View {

    private val textPaint: Paint = Paint().apply {
        isAntiAlias = true
    }

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

    private fun initView(attributeSet: AttributeSet?, defStyleAttr: Int = -1) {
        context.theme
                .obtainStyledAttributes(attributeSet, R.styleable.CountdownView, defStyleAttr, 0)
                .apply {
                    try {

                    } finally {
                        recycle()
                    }
                }
    }

//    override fun onSaveInstanceState(): Parcelable? {
//        val parcelable = super.onSaveInstanceState()
//
//    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}