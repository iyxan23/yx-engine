package com.iyxan23.yx

import com.iyxan23.yx.html.HtmlLexer
import com.iyxan23.yx.html.HtmlParser
import org.junit.Test

class HtmlParserTest {
    @Test
    fun `Test - Simple HTML`() {
        val html = """
            <text attributeName=attributeValue stringAttribute="hello world">
            hello world!
            </text>
        """.trimIndent()

        // lex it
        val tokens = HtmlLexer(html).doLexicalAnalysis()
        val element = HtmlParser.HtmlParserImpl(tokens).parse()

        println(element)
    }
}