package com.iyxan23.yx

import com.iyxan23.yx.html.HtmlAttribute
import com.iyxan23.yx.html.HtmlElement
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
        val elements = HtmlParser.HtmlParserImpl(tokens).parse()

        val expectation = listOf(
            HtmlElement("text", "hello world!", listOf(
                HtmlAttribute("attributeName", "attributeValue"),
                HtmlAttribute("stringAttribute", "hello world")
            ), emptyList())
        )

        assert(elements == expectation) {
            println("Parsed elements doesn't match the expectation\n")
            println("Expectation: $expectation")
            println("Elements:    $elements")
        }
    }

    @Test
    fun `Test - Close Early Tag`() {
        val html = """
            <text/>
        """.trimIndent()

        // lex it
        val tokens = HtmlLexer(html).doLexicalAnalysis()
        val elements = HtmlParser.HtmlParserImpl(tokens).parse()

        val expectation = listOf(
            HtmlElement("text", "", emptyList(), emptyList())
        )

        assert(elements == expectation) {
            println("Parsed elements doesn't match the expectation\n")
            println("Expectation: $expectation")
            println("Elements:    $elements")
        }
    }

    @Test
    fun `Test - Nested HTML`() {
        val html = """
            <a>
            <b>
            <c/>
            </b>
            </a>
        """.trimIndent()

        // lex it
        val tokens = HtmlLexer(html).doLexicalAnalysis()
        val elements = HtmlParser.HtmlParserImpl(tokens).parse()

        val expectation = listOf(
            HtmlElement("a", "", emptyList(), listOf(
                HtmlElement("b", "", emptyList(), listOf(
                    HtmlElement("c", "", emptyList(), emptyList())
                ))
            ))
        )

        assert(elements == expectation) {
            println("Parsed elements doesn't match the expectation\n")
            println("Expectation: $expectation")
            println("Elements:    $elements")
        }
    }
}