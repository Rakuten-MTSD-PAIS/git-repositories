package com.example.github.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.repositories.data.GITHUB_URL
import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(GITHUB_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: GitHubEndpoints = retrofit.create(GitHubEndpoints::class.java)

    val user = MutableLiveData<UserDTO>()
    val repositories = MutableLiveData<List<RepositoryDTO>>()

    fun fetchUser(username: String) {
        // FIXME Use the proper scope
        GlobalScope.launch(Dispatchers.IO) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            val response = service.getUser(username).execute()
            user.postValue(response.body()!!)
        }
    }

    fun fetchRepositories(reposUrl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            val response = service.getUserRepositories(reposUrl).execute()
            repositories.postValue(response.body()!!)
        }
    }
}