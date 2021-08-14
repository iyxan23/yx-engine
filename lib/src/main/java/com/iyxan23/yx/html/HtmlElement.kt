package com.iyxan23.yx.html

data class HtmlElement(
    var tag: String,
    var innerText: String,
    var attributes: List<HtmlAttribute>,
    var children: List<HtmlElement>
)