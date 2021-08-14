package com.iyxan23.yx

import com.iyxan23.yx.html.HtmlLexer
import com.iyxan23.yx.html.HtmlToken
import org.junit.Test

class HtmlLexerTest {

    private fun <T> assertList(result: List<T>, expectation: List<T>) {
        result.forEachIndexed { index, item ->
            assert(expectation[index] == item) {
                println("Expected ${expectation[index]} at index $index, got $item")
                println("\nDifferences:")
                println("Expectation: $expectation")
                println("Result:      $result")
            }
        }
    }

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

        assertList(result, expectation)
    }
}