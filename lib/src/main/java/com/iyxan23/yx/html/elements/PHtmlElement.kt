package com.iyxan23.yx.html.elements

import android.graphics.Canvas
import android.graphics.Paint
import com.iyxan23.yx.html.HtmlAttribute
import com.iyxan23.yx.html.HtmlElement
import com.iyxan23.yx.html.HtmlElementInner
import com.iyxan23.yx.html.Size
import com.iyxan23.yx.sp

class PHtmlElement(
    tag: String,
    inner: ArrayList<HtmlElementInner>,
    attributes: ArrayList<HtmlAttribute>
) : HtmlElement(tag, inner, attributes) {

    // current temporary preference is that the text's size will be 16sp
    private val textSize = 16.sp
    private val textPaint = Paint().apply {
        textSize = this@PHtmlElement.textSize
        color = 0xFF000000.toInt()
    }

    override fun measure(min: Size, max: Size): Size {
        TODO("Update with the new inner")
    }

    override fun draw(canvas: Canvas) {
        TODO("Update with the new inner")
    }
}