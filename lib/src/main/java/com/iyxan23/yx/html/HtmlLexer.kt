package com.iyxan23.yx.html

object HtmlLexer {

    /**
     * This function will analyse the syntax and will output a list of
     * token and strings that later be parsed, displayed, and do all of that fun stuff
     */
    fun doLexicalAnalysis(data: String): List<Any> {

        val tokens: ArrayList<Any> = ArrayList()

        val builder: StringBuilder = StringBuilder()

        var readingTagName = false
        var readingAttributeName = false
        var readingAttributeValue = false
        var readingInnerTag = false
        var insideTag = false       // <..>
        var insideInnerTag = false  // < >..</ >
        var afterTagName = false

        var charBefore: Char = ' '

        data.forEach { ch ->
            // String reading
            when {
                // TODO: 5/11/21 Detect ""

                readingAttributeValue and (ch == ' ') and (charBefore != '\\') -> {
                    // End of reading attribute value
                    tokens.add(HtmlToken.ATTRIBUTE_VALUE)
                    tokens.add(builder.toString().trim())

                    builder.clear()
                }

                ////////////////////////////////////////////////////////////////////////////////////

                readingInnerTag and (ch == '<') and (charBefore != '\\') -> {
                    // End of reading inner tag
                    tokens.add(HtmlToken.TEXT_VALUE)
                    tokens.add(builder.toString().trim())

                    builder.clear()
                }

                ////////////////////////////////////////////////////////////////////////////////////

                readingTagName and ((ch == ' ') or (ch == '>')) and (charBefore != '\\') -> {
                    // End of reading tag name
                    tokens.add(HtmlToken.TAG_NAME)
                    tokens.add(builder.toString().trim())

                    builder.clear()
                }

                ////////////////////////////////////////////////////////////////////////////////////

                readingAttributeName and (ch == '=') and (charBefore != '\\') -> {
                    // End of reading attribute name
                    tokens.add(HtmlToken.ATTRIBUTE_NAME)
                    tokens.add(builder.toString().trim())

                    builder.clear()

                    readingTagName = false
                    afterTagName = true
                    readingAttributeValue = true
                }

                readingAttributeName or readingTagName or readingInnerTag or readingAttributeValue
                        and ch.isLetter() -> {
                    builder.append(ch)
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
                    tokens.add(HtmlToken.CLOSE_TAG)

                    insideTag = false
                    insideInnerTag = true
                    readingInnerTag = true
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
                    if (ch.isLetter()

                        and !readingTagName
                        and !readingAttributeName
                        and !readingInnerTag
                        and !readingAttributeValue
                    ) {
                        if (insideTag and !afterTagName) {
                            // <..
                            builder.append(ch)

                            readingTagName = true

                        } else if (insideTag and afterTagName) {
                            // <TagName ..
                            builder.append(ch)

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