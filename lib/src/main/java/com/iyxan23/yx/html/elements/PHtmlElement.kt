package com.iyxan23.yx.html.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.iyxan23.yx.html.HtmlAttribute
import com.iyxan23.yx.html.HtmlElement
import com.iyxan23.yx.html.Size
import com.iyxan23.yx.sp

class PHtmlElement(
    tag: String,
    innerText: String,
    attributes: List<HtmlAttribute>,
    children: List<HtmlElement>
) : HtmlElement(tag, innerText, attributes, children) {

    // current temporary preference is that the text's size will be 16sp
    private val textSize = 16.sp
    private val textPaint = Paint().apply {
        textSize = this@PHtmlElement.textSize
        color = 0xFF000000.toInt()
    }

    override fun measure(min: Size, max: Size): Size {
        val bounds = Rect()
        textPaint.getTextBounds(innerText, 0, innerText.length, bounds)

        val height = if (bounds.height() > max.width) {
            max.width
        } else {
            if (bounds.height() < min.width) min.width
            else bounds.height()
        }

        val width  = if (bounds.width() > max.width) {
            max.width
        } else {
            if (bounds.width() < min.width) min.width
            else bounds.width()
        }

        return Size(width, height)
    }

    override fun draw(canvas: Canvas) {
        // TODO: 8/15/21 Mix children and innerText together because we can't know if a tag is
        //               inside the innerText
        canvas.drawText(innerText, 0f, 0f, textPaint)
    }
}