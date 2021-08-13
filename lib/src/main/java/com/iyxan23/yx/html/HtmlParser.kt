package com.iyxan23.yx.html

class HtmlParser(
    private var htmlText: String
) {
    fun parse(): List<HtmlElement> {
        val tokens = HtmlLexer(htmlText).doLexicalAnalysis()
        TODO()
    }
}