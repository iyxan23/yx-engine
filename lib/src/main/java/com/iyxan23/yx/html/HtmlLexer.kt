package com.iyxan23.yx.html

object HtmlLexer {

    /**
     * This function will analyse the syntax and will output a list of
     * token and strings that later be parsed, displayed, and do all of that fun stuff
     */
    fun doLexicalAnalysis(data: String): List<Any> {

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
                    tokens.add(HtmlToken.TAG_NAME)
                    tokens.add(builder.toString())
                }

                ////////////////////////////////////////////////////////////////////////////////////

                readingAttributeName and (ch == '<') and (charBefore != '\\') -> {
                    // End of reading attribute name
                    tokens.add(HtmlToken.ATTRIBUTE_NAME)
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
                    tokens.add(HtmlToken.OPEN_TAG)

                    insideTag = true
                    insideInnerTag = false
                }

                '>' -> {
                    insideTag = false
                    insideInnerTag = true
                }

                '/' -> {
                    if (insideTag and !afterTagName) {
                        // </..>
                        tokens.add(HtmlToken.OPEN_CLOSING_TAG)

                    } else if (insideTag and afterTagName) {
                        // <.. />
                        tokens.add(HtmlToken.NO_CHILD_CLOSE)
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