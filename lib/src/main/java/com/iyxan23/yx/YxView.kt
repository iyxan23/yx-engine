package com.iyxan23.yx

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.iyxan23.yx.html.HtmlParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class YxView : View {

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(context, attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (attrs != null) {
            // TODO: 4/29/21 Do attrs stuff here
        }
    }

    suspend fun loadUrl(url: String) {
        Log.d(TAG, "loadUrl: Loading url $url")

        withContext(Dispatchers.IO) {
            runCatching {
                val urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection

                try {
                    val webPageStream: InputStream = BufferedInputStream(urlConnection.inputStream)

                    val resultStream = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var length: Int

                    while (webPageStream.read(buffer).also { length = it } != -1) {
                        resultStream.write(buffer, 0, length)
                    }

                    val result: String = resultStream.toString("UTF-8")

                    parse(result)

                } finally {
                    urlConnection.disconnect()
                }
            }
        }
    }

    private fun parse(html: String) {
        // TODO: 4/29/21 dis bor
    }

    companion object {
        private const val TAG = "YxView"
    }
}