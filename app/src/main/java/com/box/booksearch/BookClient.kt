package com.box.booksearch

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class BookClient {
  private val apiBaseUrl = "http://openlibrary.org/"
  private val client = AsyncHttpClient()

  private fun getApiUrl(relativeUrl: String): String {
    return apiBaseUrl + relativeUrl
  }

  // Method for accessing the search API
  fun getBooks(query: String, handler: JsonHttpResponseHandler) {
    try {
      val url = getApiUrl("search.json?q=")
      client.get(url + URLEncoder.encode(query, "utf-8"), handler)
    } catch (e: UnsupportedEncodingException) {
      e.printStackTrace()
    }
  }
}