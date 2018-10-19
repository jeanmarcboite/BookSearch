package com.box.booksearch

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
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
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import com.box.booksearch.R.id.action_search


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        lvBooks.adapter = BookAdapter(this, ArrayList<Book>())
        fetchBooks("Oscar Wilde")

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Fetch the data remotely
                fetchBooks(query)
                // Reset SearchView
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.isIconified = true
                searchItem.collapseActionView()
                // Set activity title to search query
                this@MainActivity.setTitle(query)
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

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

    private fun fetchBooks(query: String) {
        val client = BookClient()
        client.getBooks(query, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject?) {
                try {
                    var docs: JSONArray?
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
