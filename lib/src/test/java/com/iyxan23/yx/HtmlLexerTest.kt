package com.iyxan23.yx

import com.iyxan23.yx.html.HtmlLexer
import com.iyxan23.yx.html.HtmlToken
import org.junit.Test
import kotlin.math.exp

class HtmlLexerTest {
    @Test
    fun `Test - Simple HTML`() {
        val html = """
            <hello world=hi test></hello>
            <hello/>
        """.trimIndent()

        val result = HtmlLexer(html).doLexicalAnalysis()
        val expectation = listOf(
            HtmlToken.TagOpen,
            HtmlToken.Word("hello"),
            HtmlToken.Word("world"),
            HtmlToken.Equal,
            HtmlToken.Word("hi"),
            HtmlToken.Word("test"),
            HtmlToken.TagInsideClose,
            HtmlToken.TagClose,
            HtmlToken.Word("hello"),
            HtmlToken.TagInsideClose,
            HtmlToken.TagOpen,
            HtmlToken.Word("hello"),
            HtmlToken.TagCloseEarly
        )

        result.forEachIndexed { index, htmlToken ->
            assert(expectation[index] == htmlToken) {
                println("Expected ${expectation[index]} at index $index, got $htmlToken")
                println()
                println(result)
                println(expectation)
            }
        }
    }
}