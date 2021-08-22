package com.iyxan23.yx

import com.iyxan23.yx.html.*
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
            HtmlElement("text", arrayListOf(
                HtmlElementInner.Text("hello world!")
            ), arrayListOf(
                HtmlAttribute("attributeName", "attributeValue"),
                HtmlAttribute("stringAttribute", "hello world")
            ))
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
            HtmlElement("text")
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
            HtmlElement("a", inner = arrayListOf(
                HtmlElementInner.Element(HtmlElement("b", inner = arrayListOf(
                    HtmlElementInner.Element(HtmlElement("c"))
                )))
            ))
        )

        assert(elements == expectation) {
            println("Parsed elements doesn't match the expectation\n")
            println("Expectation: $expectation")
            println("Elements:    $elements")
        }
    }
}