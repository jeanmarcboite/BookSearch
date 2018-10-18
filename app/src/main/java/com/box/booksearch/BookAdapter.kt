package com.box.booksearch

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import android.support.v7.widget.RecyclerView.ViewHolder
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.ImageView


class BookAdapter(context: Context, aBooks: ArrayList<Book>) : ArrayAdapter<Book>(context, 0, aBooks) {
    class BookViewHolder(itemView: View) : ViewHolder(itemView) {
        var ivCover: ImageView? = null
        var tvTitle: TextView? = null
        var tvAuthor: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        // Get the data item for this position
        val book = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        var viewHolder: BookViewHolder? // view lookup cache stored in tag
        var view = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_book, parent, false)
            viewHolder = BookViewHolder(view)
            viewHolder.ivCover = view.findViewById(R.id.ivBookCover) as ImageView
            viewHolder.tvTitle = view.findViewById(R.id.tvTitle) as TextView
            viewHolder.tvAuthor = view.findViewById(R.id.tvAuthor) as TextView
            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as BookViewHolder
        }
        // Populate the data into the template view using the data object
        viewHolder.tvTitle!!.text = book!!.title
        viewHolder.tvAuthor!!.text = book.author
        Picasso.get().load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(viewHolder.ivCover)
        // Return the completed view to render on screen
        return view
    }
}