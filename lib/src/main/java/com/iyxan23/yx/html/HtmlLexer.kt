package com.iyxan23.yx.html

/**
 * This lexer turns a html text into a list of tokens that will later be parsed by the parser
 */
class HtmlLexer(
    private val text: String
) {
    private var index = -1
    private var currentChar: Char? = null

    private val nextChar get() = advance()

    private fun advance(): Char? {
        index++
        currentChar = text.getOrNull(index)
        return currentChar
    }

    private fun goBack(): Char? {
        index--
        currentChar = text.getOrNull(index)
        return currentChar
    }

    fun doLexicalAnalysis(): List<HtmlToken> {
        val result = ArrayList<HtmlToken>()
        var token: HtmlToken?

        loop@ while (true) {
            token = when (nextChar) {
                '<' -> {
                    if (nextChar == '/') {
                        HtmlToken.TagClose
                    } else {
                        goBack()
                        HtmlToken.TagOpen
                    }
                }

                '>' -> HtmlToken.TagInsideClose

                '/' -> {
                    if (nextChar == '>') {
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
            while (nextChar.let { (it !in listOf(' ', '\n', '=', '>', '<', '/')) and (it != null) }) {
                append(currentChar)
            }

            if (currentChar in listOf('=', '>', '<', '/')) goBack()
        }.toString()
}