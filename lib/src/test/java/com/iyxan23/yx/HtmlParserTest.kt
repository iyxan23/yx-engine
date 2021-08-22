package com.iyxan23.yx

import com.iyxan23.yx.html.*
import org.junit.Test

class HtmlParserTest {
    @Test
    fun `Test plain parser - Simple HTML`() {
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
    fun `Test plain parser - Close Early Tag`() {
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
    fun `Test plain parser - Nested HTML`() {
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

    @Test
    fun `Test structure fixer & parser - Simple HTML`() {
        val html = """
            <p>hello world!</p>
        """.trimIndent()

        // lex then parse using the regular HtmlParser
        val tokens = HtmlLexer(html).doLexicalAnalysis()
        val rootElement = HtmlParser(tokens).parse()

        val expectation =
            HtmlElement("html", inner = arrayListOf(
                HtmlElementInner.Element(HtmlElement("head")),
                HtmlElementInner.Element(HtmlElement("body", inner = arrayListOf(
                    HtmlElementInner.Element(HtmlElement("p", inner = arrayListOf(
                        HtmlElementInner.Text("hello world!")
                    )))
                )))
            ))

        assert(rootElement == expectation) {
            println("Parsed elements doesn't match the expectation\n")
            println("Expectation: $expectation")
            println("Elements:    $rootElement")
        }
    }

    @Test
    fun `Test structure fixer & parser - Double html tag`() {
        // these flags and the inner must be combined together
        val html = """
            <html flag1>
                1
            </html>
            <html flag2>
                2
            </html>
        """.trimIndent()

        // lex then parse using the regular HtmlParser
        val tokens = HtmlLexer(html).doLexicalAnalysis()
        val rootElement = HtmlParser(tokens).parse()

        val expectation =
            HtmlElement("html", attributes = arrayListOf(
                HtmlAttribute("flag1"),
                HtmlAttribute("flag2")
            ), inner = arrayListOf(
                HtmlElementInner.Element(HtmlElement("head")),
                HtmlElementInner.Element(HtmlElement("body", inner = arrayListOf(
                    HtmlElementInner.Text("1"),
                    HtmlElementInner.Text("2")
                )))
            ))

        assert(rootElement == expectation) {
            println("Parsed elements doesn't match the expectation\n")
            println("Expectation: $expectation")
            println("Elements:    $rootElement")
        }
    }
}