package com.iyxan23.yx.html

/**
 * Stores width and height in px
 */
data class Size(
    var width: Int,
    var height: Int
) {
    fun getMax(otherSize: Size): Size =
        Size(
            if (otherSize.width > width) otherSize.width else width,
            if (otherSize.height > height) otherSize.height else height
        )

    fun getMin(otherSize: Size): Size =
        Size(
            if (otherSize.width >= width) width else otherSize.width,
            if (otherSize.height >= height) height else otherSize.height
        )
}
