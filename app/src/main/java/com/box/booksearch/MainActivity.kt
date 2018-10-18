package com.box.booksearch

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import com.box.booksearch.R.id.lvBooks
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONException
import android.text.method.TextKeyListener.clear
import org.json.JSONArray
import org.json.JSONObject
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        lvBooks.adapter = BookAdapter(this, ArrayList<Book>())
        fetchBooks()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchBooks() {
        val client = BookClient()
        client.getBooks("oscar Wilde", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject?) {
                try {
                    var docs: JSONArray? = null
                    if (response != null) {
                        // Get the docs json array
                        docs = response.getJSONArray("docs")
                        // Parse json array into array of model objects
                        val books = Book.fromJson(docs)
                        // Remove all books from the adapter
                        val bookAdapter = lvBooks.adapter as BookAdapter
                        bookAdapter.clear()
                        // Load model objects into the adapter
                        if (books != null) {
                            for (book in books) {
                                bookAdapter.add(book) // add book through the adapter
                            }
                        }
                        bookAdapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace()
                }

            }
        })
    }
}
