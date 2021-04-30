package com.iyxan23.yx.html

data class HtmlElement(
    var tag: String,
    var value: String,
    var attributes: List<HtmlAttribute>,
    var childs: List<HtmlElement>
)