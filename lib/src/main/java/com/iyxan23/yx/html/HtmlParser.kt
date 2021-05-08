package com.iyxan23.yx.html

class HtmlParser(var data: String) {
    fun parse(): List<HtmlElement> {
        val tokens: List<Any> = HtmlLexer.doLexicalAnalysis(data)
        TODO("tokens to List<HtmlElement>")
    }
}