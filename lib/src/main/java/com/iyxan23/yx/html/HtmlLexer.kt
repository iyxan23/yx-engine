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

    fun doLexicalAnalysis(): List<HtmlToken> =
        ArrayList<HtmlToken>().apply {
            var token: HtmlToken? = nextToken()

            while (token != null) {
                add(token)
                token = nextToken()
            }
        }

    private fun nextToken(): HtmlToken? {
        when (currentChar) {
            '<' -> {
                if (nextChar == '/') return HtmlToken.TagClose
                goBack()
                return HtmlToken.TagOpen
            }

            '>' -> return HtmlToken.TagInsideClose

            '/' -> {
                if (nextChar == '>') return HtmlToken.TagCloseEarly
                goBack()
                TODO("Don't know what to do here")
            }

            '=' -> return HtmlToken.Equal
            null -> return null
            else -> return HtmlToken.Word(readWord())
        }
    }

    /**
     * Reads the text until space or newline
     */
    private fun readWord(): String =
        StringBuilder().apply {
            while (nextChar.let { (it !in listOf(' ', '\n')) or (it != null) }) {
                append(currentChar)
            }

            // go back when we got to the things
            if (currentChar in listOf(' ', '\n')) goBack()
        }.toString()
}