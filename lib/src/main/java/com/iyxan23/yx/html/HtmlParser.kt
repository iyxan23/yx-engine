package com.iyxan23.yx.html

import android.util.Log
import com.iyxan23.yx.BaseParser
import com.iyxan23.yx.Either

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
        // First, we will loop through the elements and try to get the html, head, body elements
        // and put it into these vars:
        var htmlElement: HtmlElement? = null
        var headElement: HtmlElement? = null
        var bodyElement: HtmlElement? = null

        // If we found other elements that aren't apart of the html / the root, we will need to
        // put them to either the body or the head in these arraylists, these children will get
        // added to the head / body element at the end

//        val headInner = ArrayList<HtmlElementInner>() // currently not needed YET
        val bodyInner = ArrayList<HtmlElementInner>()

        for (element in elements) {
            when (element.tag) {
                "html" -> {
                    htmlElement = htmlElement?.let {
                        // well this is awkward, there are multiple html tags..
                        Log.w(TAG, "fixStructure: multiple html tags!")

                        // for this situation, we're just going to mix them both
                        it.inner.addAll(element.inner)
                        it.attributes.addAll(element.attributes)

                        it
                    } ?: element /* alright, set this to htmlElement then */
                }

                "head" -> {
                    headElement = headElement?.let {
                        // well this is awkward, there are multiple head tags..
                        Log.w(TAG, "fixStructure: multiple head tags!")

                        // for this situation, we're just going to mix them both
                        it.inner.addAll(element.inner)
                        it.attributes.addAll(element.attributes)

                        it
                    } ?: element /* alright, set this to headElement then */
                }

                "body" -> {
                    bodyElement = bodyElement?.let {
                        // well this is awkward, there are multiple body tags..
                        Log.w(TAG, "fixStructure: multiple body tags!")

                        // for this situation, we're just going to mix them both
                        it.inner.addAll(element.inner)
                        it.attributes.addAll(element.attributes)

                        it
                    } ?: element /* alright, set this to bodyElement then */
                }

                else -> {
                    // random tag outta nowhere! put it on body!
                    // TODO: 8/22/21 move tags like script, link, title, etc to headChildren instead
                    bodyInner.add(HtmlElementInner.Element(element))
                }
            }
        }

        // create a html tag if it doesn't exists
        if (htmlElement == null) htmlElement = HtmlElement("html")

        // also move inner of html into body, since html can only contain the head and body tags
        if (htmlElement.inner.isNotEmpty()) {
            htmlElement.inner.mapNotNull {
                bodyInner.add(it)
                null
            }

            htmlElement.inner.clear()
        }

        // create head or body if they doesn't exists
        if (headElement == null) headElement = HtmlElement("head")
        if (bodyElement == null) bodyElement = HtmlElement("head")

        // put the inner vars into their element
//        headElement.inner.addAll(headInner)
        bodyElement.inner.addAll(bodyInner)

        // then add the head and the body into the html tag
        htmlElement.inner.add(HtmlElementInner.Element(headElement))
        htmlElement.inner.add(HtmlElementInner.Element(bodyElement))

        // done
        return htmlElement
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
            val attrsOrHtml = parseAttributes(tagName)
            val attributes: List<HtmlAttribute>

            when (attrsOrHtml) {
                is Either.Left<ArrayList<HtmlAttribute>> -> {
                    // this is an arraylist of attributes, means that the attributes parser is
                    // doing its thing
                    attributes = attrsOrHtml.value
                }

                is Either.Right<HtmlElement> -> {
                    // this is a html element, means that the attributes parser encountered a
                    // close early tag, which means it doesn't have any inner, so we return
                    // directly
                    return attrsOrHtml.value
                }
            }

            // we're done parsing the attributes, let's parse it's inner!
            val inner = parseInner()

            // after the close, we need to skip until the last close inside token
            // < ... ">"
            while (true) if (nextItem == HtmlToken.TagInsideClose) break

            return HtmlElement(tagName, ArrayList(inner), attributes)
        }

        /**
         * Parses an attribute of an html tag, this parse function should be called after the tag
         * name. This function returns either the attributes or the html element. Might sound odd
         * for a function that parses an attribute to return an html element, it does that when
         * it encountered a close early token `/>` where an html element doesn't have any inner.
         * So, to save some time, the [parse] function will just return the html element directly
         */
        private fun parseAttributes(tagName: String): Either<ArrayList<HtmlAttribute>, HtmlElement> {
            val attributes = ArrayList<HtmlAttribute>()

            while (nextItem != HtmlToken.TagInsideClose) {
                // check if this is a word
                if (currentItem !is HtmlToken.Word) {
                    // ok, check if this is a tag close early then
                    if (currentItem is HtmlToken.TagCloseEarly)
                        // hmm it is, let's return directly
                        return Either.Right(HtmlElement(tagName, ArrayList(), attributes))

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

            return Either.Left(attributes)
        }

        /**
         * All this function does is to parse an inner of an HTML tag
         */
        private fun parseInner(): List<HtmlElementInner> {
            val inner = ArrayList<HtmlElementInner>()

            // loop until the next item is a tag close
            while (nextItem != HtmlToken.TagClose) {
                if (currentItem == HtmlToken.TagOpen) {
                    // this is an element opening, parse it and add it as an element
                    parseHtmlTag()?.let {
                        inner.add(HtmlElementInner.Element(it))
                    }

                } else if (currentItem is HtmlToken.Word) {
                    // get the first item
                    val firstItem = inner.firstOrNull()

                    // if there is the first item and it's a text
                    if (firstItem != null && firstItem is HtmlElementInner.Text) {
                        // add the word
                        firstItem.text += " ${(currentItem as HtmlToken.Word).word}"

                    } else {
                        // if not, then create it
                        inner.add(
                            HtmlElementInner.Text(
                                (currentItem as HtmlToken.Word).word
                            )
                        )
                    }

                } else {
                    Log.w(TAG, "parseHtmlTag: Unknown token $currentItem, skipping")
                    continue
                }
            }

            return inner
        }
    }
}