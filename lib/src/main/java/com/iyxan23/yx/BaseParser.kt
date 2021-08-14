package com.iyxan23.yx

abstract class BaseParser<ItemType, ValueType>(
    private val items: ValueType
) {
    protected var index = -1
    protected var currentItem: ItemType? = null
    protected val nextItem get() = advance()

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