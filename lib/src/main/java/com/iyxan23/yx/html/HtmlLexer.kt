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
            when (nextChar) {
                '<' -> {
                    token = if (nextChar == '/') {
                        HtmlToken.TagClose
                    } else {
                        goBack()
                        HtmlToken.TagOpen
                    }
                }

                '>' -> token = HtmlToken.TagInsideClose

                '/' -> {
                    if (nextChar == '>') {
                        token = HtmlToken.TagCloseEarly
                    } else {
                        goBack()
                        TODO("Don't know what to do here")
                    }
                }

                '=' -> token = HtmlToken.Equal
                ' ', '\n' -> continue@loop
                null -> token = null
                else -> {
                    goBack()
                    token = HtmlToken.Word(readWord())
                }
            }

            if (token == null) break
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