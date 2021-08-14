package com.iyxan23.yx

abstract class BaseParser<ValueType, ItemType>(
    private val items: ValueType
) {
    protected var index = -1
    protected var currentItem: ItemType? = null
    protected val nextItem get() = advance()
    protected val previousItem get() = getItem(items, index - 1)

    protected fun advance(): ItemType? {
        index++
        currentItem = getItem(items, index)
        return currentItem
    }

    protected fun goBack(): ItemType? {
        index--
        currentItem = getItem(items, index)
        return currentItem
    }

    abstract fun getItem(value: ValueType, index: Int): ItemType?
}