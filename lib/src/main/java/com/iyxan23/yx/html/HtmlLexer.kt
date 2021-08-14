package com.iyxan23.yx.html

import com.iyxan23.yx.BaseParser

/**
 * This lexer turns a html text into a list of tokens that will later be parsed by the parser
 */
class HtmlLexer(
    text: String
): BaseParser<String, Char>(text) {

    override fun getItem(value: String, index: Int): Char? = value.getOrNull(index)

    fun doLexicalAnalysis(): List<HtmlToken> {
        val result = ArrayList<HtmlToken>()
        var token: HtmlToken?

        loop@ while (true) {
            token = when (nextItem) {
                '<' -> {
                    if (nextItem == '/') {
                        HtmlToken.TagClose
                    } else {
                        goBack()
                        HtmlToken.TagOpen
                    }
                }

                '>' -> HtmlToken.TagInsideClose

                '/' -> {
                    if (nextItem == '>') {
                        HtmlToken.TagCloseEarly
                    } else {
                        goBack()
                        // there is no reason to parse this, just read it as a word
                        HtmlToken.Word(readWord())
                    }
                }

                '=' -> HtmlToken.Equal

                ' ', '\n' -> continue@loop
                null -> break@loop

                else -> {
                    goBack()
                    HtmlToken.Word(readWord())
                }
            }

            result.add(token)
        }

        return result
    }

    /**
     * Reads the text until space, newline or other html tokens
     */
    private fun readWord(): String =
        StringBuilder().apply {
            while (nextItem.let { (it !in listOf(' ', '\n', '=', '>', '<', '/')) and (it != null) }) {
                append(currentItem)
            }

            if (currentItem in listOf('=', '>', '<', '/')) goBack()
        }.toString()
}