package com.example.github.repositories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.data.OwnerDTO
import com.squareup.picasso.Picasso

class UserFragment(private val user: OwnerDTO) : Fragment() {

    private val viewModel = UserViewModel()

    private var title: TextView? = null
    private var image: ImageView? = null
    private var detail: TextView? = null
    private var url: TextView? = null
    private var list: RecyclerView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        title = view.findViewById(R.id.title)
        image = view.findViewById(R.id.image)
        detail = view.findViewById(R.id.detail)
        url = view.findViewById(R.id.url)
        list = view.findViewById(R.id.list)

        title!!.text = user.login
        Picasso.get().load(user.avatar_url.toUri()).into(image)

        viewModel.fetchUser(user.login)
        viewModel.user.observeForever {
            detail!!.text = "Twitter handle: " + it.twitter_username
            viewModel.fetchRepositories(it.repos_url!!)
        }
        viewModel.repositories.observeForever {
            list!!.adapter = RepositoryAdapter(it.toMutableList(), requireActivity())
        }
        return view
    }
}