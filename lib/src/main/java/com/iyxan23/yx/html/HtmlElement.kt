package com.iyxan23.yx.html

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

open class HtmlElement(
    val tag: String,
    val innerText: String,
    val attributes: List<HtmlAttribute>,
    val children: List<HtmlElement>
) {
    val childrenSizes = HashMap<HtmlElement, Size>()

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

        for (child in children) {
            val childSize = child.measure(min, max)
            childrenSizes[child] = childSize

            val sumHeight = size.height + childSize.height

            if (sumHeight >= max.height)
                size.height = sumHeight

            // get the largest width
            if (childSize.width > size.width && childSize.width <= max.width)
                size.width = childSize.width
        }

        return size
    }

    /**
     * This function draws the current html element, the passed Canvas argument will be a separate
     * bitmap and it will be appended to it's parent draw method at the location of the current
     * element, you don't need to calculate at which place you should draw.
     */
    open fun draw(canvas: Canvas) {
        val ourBitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)

        var xPos = 0
        for (child in children) {
            // create a bitmap for the child to draw on
            val size = childrenSizes[child]!!
            val childBitmap = Bitmap.createBitmap(ourBitmap, xPos, 0, size.width, size.height)
            child.draw(Canvas(childBitmap))

            xPos += size.height
        }

        canvas.drawBitmap(ourBitmap, Matrix(), null)
    }
}