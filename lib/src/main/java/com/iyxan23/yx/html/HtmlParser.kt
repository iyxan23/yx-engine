package com.iyxan23.yx.html

import android.util.Log
import com.iyxan23.yx.BaseParser

/**
 * This class parses a list of html tokens into a single HTML element that will be the html tag
 */
class HtmlParser(
    private val tokens: List<HtmlToken>
) {
    companion object {
        private const val TAG = "HtmlParser"
    }

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
            val result = ArrayList<HtmlElement>()

            while (nextItem != null) {
                if (currentItem == HtmlToken.TagOpen) {
                    parseHtmlTag()?.let { result.add(it) }
                } else {
                    Log.w(TAG, "parse: $index is not a tag opening, skipping")
                    continue
                }
            }

            return result
        }

        private fun parseHtmlTag(): HtmlElement? {
            // check if this is not a word (tag name)
            if (nextItem !is HtmlToken.Word) {
                Log.w(TAG, "parseHtmlTag: Token after TagOpen is not a word at $index, skipping")
                return null
            }

            val tagName = (currentItem as HtmlToken.Word).word

            // now let's parse it's attributes
            val attributes = ArrayList<HtmlAttribute>()

            while (nextItem != HtmlToken.TagInsideClose) {
                // check if this is a word
                if (currentItem !is HtmlToken.Word) {
                    // ok, check if this is a tag close early then
                    if (currentItem is HtmlToken.TagCloseEarly)
                        // hmm it is, let's return directly
                        return HtmlElement(tagName, "", attributes, emptyList())

                    Log.w(TAG, "parseHtmlTag: got $currentItem inside a tag, skipping this")
                    continue
                }

                // alr this word must be the attribute name
                val attributeName = (currentItem as HtmlToken.Word).word

                // check if the next is not an equal sign
                if (peekNext != HtmlToken.Equal) {
                    // ok this attribute is just a flag or smth, it doesn't have any value
                    attributes.add(HtmlAttribute(attributeName))
                    continue
                }

                // the next IS an equal sign! ok, let's jump that equal sign and get the value
                // (which is the next token)
                advance()

                if (nextItem !is HtmlToken.Word) {
                    // hmm, weird "$attributeName=???", we're going to pretend this never happened
                    Log.w(TAG, "parseHtmlTag: found an attribute name with an equal " +
                            "sign, but no value (at $index)")
                    attributes.add(HtmlAttribute(attributeName))
                    continue
                }

                // ok, this IS a word! this must be the value!
                var attributeValue = (currentItem as HtmlToken.Word).word

                // because the Lexer can only parse the word, if this is a string, we need to parse it
                if (attributeValue.startsWith('"')) {
                    // FIXME: 8/14/21 the value of a string can contain one of the html token!!

                    // drop the " at the start
                    attributeValue = attributeValue.drop(1)

                    // ok we need to read this string
                    while (peekNext is HtmlToken.Word) {
                        advance()

                        val curItem = (currentItem as HtmlToken.Word).word

                        // add it to the value
                        attributeValue += " $curItem"

                        if (curItem.endsWith('"')) {
                            // drop the "
                            attributeValue = attributeValue.dropLast(1)

                            // and stop parsing the string
                            break
                        }
                    }
                }

                // alright we're done
                attributes.add(HtmlAttribute(attributeName, attributeValue))
            }

            // we're done parsing the attributes, let's parse it's inner!
            var innerText = ""
            val children = ArrayList<HtmlElement>()

            // TODO: 8/15/21 Mix children and innerText together because we can't know if a tag is
            //               inside the innerText

            // loop until the next item is a tag close
            while (nextItem != HtmlToken.TagClose) {
                if (currentItem == HtmlToken.TagOpen) {
                    parseHtmlTag()?.let { children.add(it) }

                } else if (currentItem is HtmlToken.Word) {
                    innerText += " ${(currentItem as HtmlToken.Word).word}"

                } else {
                    Log.w(TAG, "parseHtmlTag: Unknown token $currentItem, skipping")
                    continue
                }
            }

            // after the close, we need to skip until the last close inside token
            // < ... ">"
            while (true) if (nextItem == HtmlToken.TagInsideClose) break

            // remove the first space
            if (innerText.length > 1) innerText = innerText.drop(1)

            return HtmlElement(tagName, innerText, attributes, children)
        }
    }
}