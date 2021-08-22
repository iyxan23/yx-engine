package com.iyxan23.yx.html

import android.graphics.Bitmap
import android.graphics.Canvas

open class HtmlElement(
    val tag: String,
    val inner: ArrayList<HtmlElementInner> = ArrayList(),
    val attributes: ArrayList<HtmlAttribute> = ArrayList()
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

    /**
     * This function will be used to copy the current HtmlElement and will have optional parameters
     * of the content of the HtmlElement to be modified / changed
     */
    open fun copy(
        tag: String = this.tag,
        inner: List<HtmlElementInner> = this.inner,
        attributes: List<HtmlAttribute> = this.attributes
    ): HtmlElement {
        return HtmlElement(tag, inner, attributes)
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