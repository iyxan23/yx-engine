package com.iyxan23.yx.html

import android.graphics.Bitmap
import android.graphics.Canvas

open class HtmlElement(
    val tag: String,
    val inner: List<HtmlElementInner>,
    val attributes: List<HtmlAttribute>
) {
    /**
     * Measures the size of the current HTML element
     *
     * Try not to pass the maximum limit
     */
    open fun measure(min: Size, max: Size): Size {
        // TODO: 8/15/21 ID, classes, css stuff

        // we don't have css yet so we're going to just add children vertically without any spacing
        // also, since all html element can have children, every html elements must measure them
        val size = Size(0, 0)

        TODO("Measure text and elements")
    }

    /**
     * This function draws the current html element, the passed Canvas argument will be a separate
     * bitmap and it will be appended to it's parent draw method at the location of the current
     * element, you don't need to calculate at which place you should draw.
     */
    open fun draw(canvas: Canvas) {
        val ourBitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)

        var xPos = 0
        TODO("Draw text and elements")

//        canvas.drawBitmap(ourBitmap, Matrix(), null)
    }
}

/**
 * This class is used to represent the inner content of an html tag.
 *
 * Example:
 *
 * Input:
 * > `<p>Hello <b>World!</b></p>`
 *
 * Inner content of the P tag:
 * > ```[```
 *
 * > ```TagInnerContent.Text(text="Hello "),```
 *
 * > ```TagInnerContent.Element(tag="b" ... inner=[Text(text="World!")])```
 *
 * > ```]```
 */
sealed class HtmlElementInner {
    data class Text(var text: String) : HtmlElementInner()
    data class Element(val tag: HtmlElement) : HtmlElementInner()
}