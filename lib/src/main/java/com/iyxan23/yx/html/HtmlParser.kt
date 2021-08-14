package com.iyxan23.yx.html

import com.iyxan23.yx.BaseParser

/**
 * This class parses a list of html tokens into a single HTML element that will be the html tag
 */
class HtmlParser(
    private val tokens: List<HtmlToken>
) {
    /**
     * Parses the tokens given at class construction into a single html element which is the html
     * tag
     */
    fun parse(): HtmlElement {
        // First, we will need to parse the tokens into a list of elements, why not directly to a
        // single html element? it's because this function only reads the token and builds it into
        // html elements, it doesn't know what an element is. Since we need the output to only be
        // the HTML tag, we need to correct the structure in the next function invocation
        val elements = parseToElements()

        return fixStructure(elements)
    }

    /**
     * Reads through the token and builds a list of html elements from it
     */
    private fun parseToElements(): List<HtmlElement> {
        return HtmlParserImpl(tokens).parse()
    }

    /**
     * Fixes the structure of the elements given to only have the html tag as the parent (don't
     * forget about the head and body tag needed)
     */
    private fun fixStructure(elements: List<HtmlElement>): HtmlElement {
        TODO("Not yet implemented")
    }

    /**
     * The class where the token reading and html element building is going to be
     */
    class HtmlParserImpl(
        tokens: List<HtmlToken>
    ) : BaseParser<List<HtmlToken>, HtmlToken>(tokens) {
        override fun getItem(value: List<HtmlToken>, index: Int): HtmlToken? =
            value.getOrNull(index)

        fun parse(): List<HtmlElement> {
            TODO()
        }
    }
}