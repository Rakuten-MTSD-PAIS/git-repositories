package com.example.github.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var retrofit = Retrofit.Builder().baseUrl(GITHUB_URL).addConverterFactory(GsonConverterFactory.create()).build()
    var service: RepositoriesService = retrofit.create(RepositoriesService::class.java)
    var container: RelativeLayout? = null
    var title: TextView? = null
    var image: ImageView? = null
    var date: TextView? = null
    var textView: TextView? = null
    var url: TextView? = null
    var source: TextView? = null
    var recyclerview: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.container)
        title = findViewById(R.id.title)
        image = findViewById(R.id.image)
        date = findViewById(R.id.date)
        textView = findViewById(R.id.content)
        url = findViewById(R.id.url)
        source = findViewById(R.id.source)
        recyclerview = findViewById(R.id.news_list)
        recyclerview!!.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            launch(Dispatchers.IO) {
                var response = service.getRepositories(QUERY, SORT, ORDER).execute()
                var adapter = CustomAdapter(response.body()!!.items.take(20) as MutableList) {
                    recyclerview!!.visibility = INVISIBLE
                    container!!.visibility = VISIBLE
                    title!!.text = it.name
                    date!!.text = "Created at: " + it.created_at
                    Picasso.get().load(it.owner!!.avatar_url).into(image)
                    textView!!.text = it.description
                    url!!.text = it.html_url
                }
                recyclerview!!.adapter = adapter
            }
        }
    }

    override fun onBackPressed() {
        if (container!!.visibility == VISIBLE)
        {
            recyclerview!!.visibility = VISIBLE
            container!!.visibility = INVISIBLE
        }
        else
        {
            super.onBackPressed()
        }
    }
}

class CustomAdapter(private val mList: MutableList<Repository>, val itemClicked: (Repository) -> Unit) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Items = mList[position]
        holder.container.setOnClickListener { itemClicked(Items) }
        holder.titleTxt.text = "#" + (position + 1) +": " + Items.full_name!!.toUpperCase()
        holder.descriptionTxt.text = if (Items.description!!.length > 150) Items.description!!.take(150).plus("...") else Items.description
        holder.authorTxt.text = Items.owner!!.login
    }

    override fun getItemCount(): Int = mList.size

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val container: RelativeLayout = itemView.findViewById(R.id.news_container)
        val titleTxt: TextView = itemView.findViewById(R.id.title)
        val descriptionTxt: TextView = itemView.findViewById(R.id.description)
        val authorTxt: TextView = itemView.findViewById(R.id.author)
    }
}

private const val GITHUB_URL = "https://api.github.com/"
private const val QUERY = "android+language:kotlin"
private const val SORT = "stars"
private const val ORDER = "desc"

data class Response(val total_count: String, val incomplete_results: Boolean, val items: MutableList<Repository>)
data class User(var login: String, var id: Int, var  node_id:String, var  avatar_url:String, var  gravatar_id:String, var  url:String, var  html_url:String, var followers_url:String, var  following_url:String, var  gists_url:String, var  starred_url:String, var  subscriptions_url:String, var  organizations_url:String, var  repos_url:String, var  events_url:String, var   received_events_url:String, var  type:String, var  site_admin:Boolean)
data class License(var key: String, var name: String, var spdx_id : String, var url : String, var node_id : String)
data class Repository(var id: Int?, var node_id: String?, var name: String?, var full_name: String?, var private : Boolean?, var owner : User?, var html_url : String?, var description : String?, var fork: Boolean?, var url: String?, var forks_url: String?, var keys_url : String?, var collaborators_url : String?, var teams_url: String?, var hooks_url: String?, var issue_events_url: String?, var events_url: String?, var assignees_url: String?, var branches_url : String?, var tags_url : String?, var blobs_url: String?, var git_tags_url: String?, var git_refs_url: String?, var trees_url: String?, var statuses_url : String?, var languages_url: String?, var stargazers_url : String?, var contributors_url: String?, var subscribers_url: String?, var subscription_url: String?, var commits_url: String?, var git_commits_url : String?, var comments_url : String?, var issue_comment_url: String?, var contents_url : String?, var compare_url: String?, var merges_url: String?, var archive_url: String?, var downloads_url: String?, var issues_url: String?, var pulls_url: String?, var milestones_url : String?, var notifications_url : String?, var labels_url: String?, var releases_url : String?, var deployments_url: String?, var created_at: String?, var updated_at: String?, var pushed_at: String?, var git_url: String?, var ssh_url: String?, var clone_url: String?, var svn_url: String?, var homepage: String?, var size: Int?, var stargazers_count: Int?, var watchers_count : Int?, var language: String?, var has_issues: Boolean?, var has_projects : Boolean?, var has_downloads: Boolean?, var has_wiki : Boolean?, var has_pages: Boolean?, var forks_count: Int?, var mirror_url: String?, var archived: Boolean?, var disabled: Boolean?, var open_issues_count: Int?, var license : License?, var allow_forking: Boolean?, var is_template: Boolean?, var topics: ArrayList<String> = arrayListOf(), var visibility: String?, var forks : Int?, var open_issues: Int?, var watchers: Int?, var default_branch : String?, var score : Int?)

interface RepositoriesService {
    @GET("search/repositories")
    fun getRepositories(@Query("q")  q: String, @Query("sort")  sort: String, @Query("order")  order: String): Call<Response>
}