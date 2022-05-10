package com.example.github.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    // FIXME Inject the APIs
    private val retrofit = Retrofit.Builder()
        .baseUrl(GITHUB_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: RepositoriesService = retrofit.create(RepositoriesService::class.java)

    // FIXME Convert to LiveData before passing to view
    val repositories = MutableLiveData<List<RepositoryDTO>>()

    // FIXME Fetch on initialization or observation
    fun fetchItems() {
        // FIXME Use the proper scope
        GlobalScope.launch(Dispatchers.Main) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: Response?
            withContext(Dispatchers.IO) {
                response = service.getRepositories(QUERY, SORT, ORDER).execute().body()
            }
            repositories.value = response?.items
        }
    }

    // FIXME Reuse the same code as fetch items
    fun refresh() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: Response?
            withContext(Dispatchers.IO) {
                response = service.getRepositories(QUERY, SORT, ORDER).execute().body()
            }
            repositories.value = response?.items
        }
    }
}