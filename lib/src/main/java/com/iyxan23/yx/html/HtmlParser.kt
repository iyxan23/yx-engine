package com.iyxan23.yx.html

class HtmlParser(var data: String) {
    fun parse(): List<HtmlElement> {
        TODO()
    }

    /**
     * This function will analyse the syntax and will output a list of
     * token and strings that later be parsed, displayed, and do all of that fun stuff
     */
    private fun doLexicalAnalysis(): List<Any> {

        val tokens: ArrayList<Any> = ArrayList()

        var builder: StringBuilder? = null

        var readingTagName = false
        var readingAttributeName = false
        var insideTag = false       // <..>
        var insideInnerTag = false  // < >..</ >
        var afterTagName = false

        var charBefore: Char = ' '

        data.forEach { ch ->
            // String reading
            when {
                readingTagName and (ch == '=') and (charBefore != '\\') -> {
                    // End of reading tag name
                    tokens.add(HtmlTokens.TAG_NAME)
                    tokens.add(builder.toString())
                }

                ////////////////////////////////////////////////////////////////////////////////////

                readingAttributeName and (ch == '<') and (charBefore != '\\') -> {
                    // End of reading tag name
                    tokens.add(HtmlTokens.ATTRIBUTE_NAME)
                    tokens.add(builder.toString())

                    readingTagName = false
                    afterTagName = true
                }

                readingAttributeName or readingTagName and ch.isLetter() -> {
                    builder!!.append(ch)
                }
            }

            // Imma call this "Token checking"
            when (ch) {
                '<' -> {
                    insideTag = true
                }

                '>' -> {
                    insideTag = false
                    insideInnerTag = true
                }

                '/' -> {
                    if (insideTag and !afterTagName) {
                        // </..>
                        tokens.add(HtmlTokens.OPEN_CLOSING_TAG)

                    } else if (insideTag and afterTagName) {
                        // <.. />
                        tokens.add(HtmlTokens.NO_CHILD_CLOSE)
                    }
                }

                else -> {
                    if (ch.isLetter()) {
                        if (insideTag and !afterTagName) {
                            // <..
                            builder = StringBuilder()
                            readingTagName = true

                        } else if (insideTag and afterTagName) {
                            // <TagName ..
                            builder = StringBuilder()
                            readingAttributeName = true

                        }
                    }
                }
            }

            charBefore = ch
        }

        return tokens
    }
}