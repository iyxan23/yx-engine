package com.iyxan23.yx.html

/**
 * Enum class containing pretty much all the necessary "tokens" of HTML
 */
sealed class HtmlToken {
    object TagOpen : HtmlToken()                    // <
    object TagInsideClose : HtmlToken()             // >
    object TagClose : HtmlToken()                   // </
    object TagCloseEarly : HtmlToken()              // />
    object Equal : HtmlToken()                      // =
    data class Word(val word: String) : HtmlToken() // text that ends in space
}