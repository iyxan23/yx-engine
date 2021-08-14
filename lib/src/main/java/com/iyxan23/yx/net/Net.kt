package com.iyxan23.yx.net

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object Net {
    @Throws(IOException::class)
    fun getRequest(
        url: String,
        properties: Map<String, String> = emptyMap()
    ): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        for (entry in properties.entries) {
            connection.setRequestProperty(entry.key, entry.value)
        }

        val responseStream: InputStream = connection.inputStream

        // read the stream
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024 * 16)
        var length: Int

        while (responseStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }

        return result.toString("UTF-8")
    }
}