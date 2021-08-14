package com.iyxan23.yx

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.iyxan23.yx.html.HtmlElement
import com.iyxan23.yx.html.HtmlLexer
import com.iyxan23.yx.html.HtmlParser
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.properties.Delegates

class YxView : View {
    companion object { private const val TAG = "YxView" }

    // Constructors ================================================================================
    constructor(context: Context?) : super(context) { init(context, null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (attrs != null) {
            // TODO: 4/29/21 Do attrs stuff here
        }
    }
    // Status stuff ================================================================================
    private var statusListener: ((LoadingStatus) -> Unit)? = null

    var status by Delegates.observable(LoadingStatus.IDLE) { _, _, status ->
        statusListener?.invoke(status)
    }
        private set

    fun setStatusListener(listener: (LoadingStatus) -> Unit) { statusListener = listener }
    fun removeStatusListener() { statusListener = null }

    // Loading stuff ===============================================================================
    private val executor = Executors.newCachedThreadPool()
    private val loadingLock = ReentrantLock()

    fun loadUrl(url: String) {
        Log.d(TAG, "loadUrl: Loading url $url")

        executor.submit {
            loadingLock.withLock {
                // fetch url
                status = LoadingStatus.FETCHING
                val htmlData = Net.getRequest(url)

                // parse the html data
                status = LoadingStatus.PARSING
                val elements = parse(htmlData)
            }
        }
    }

    private fun parse(html: String): HtmlElement {
        // first, we need to lex this html into a bunch of tokens
        val tokens = HtmlLexer(html).doLexicalAnalysis()

        // then parse it using HtmlParser
        return HtmlParser(tokens).parse()
    }

    // Displaying ==================================================================================
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // TODO: 8/14/21 do rendering stuff here
    }
}