package com.example.github.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso

class DetailFragment(private val repository: RepositoryDTO) : Fragment() {

    private var title: TextView? = null
    private var image: ImageView? = null
    private var date: TextView? = null
    private var textView: TextView? = null
    private var url: TextView? = null
    private var source: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        title = view.findViewById(R.id.title)
        image = view.findViewById(R.id.image)
        date = view.findViewById(R.id.date)
        textView = view.findViewById(R.id.content)
        url = view.findViewById(R.id.url)
        source = view.findViewById(R.id.source)

        title!!.text = repository.name
        date!!.text = "Created at: " + repository.created_at
        Picasso.get().load(repository.owner!!.avatar_url).into(image)
        textView!!.text = repository.description
        url!!.text = repository.html_url
        return view
    }
}