package com.example.github.repositories.data

class LocalDataStore private constructor() {

    companion object {
        val instance = LocalDataStore()
    }

    private val bookmarks = mutableMapOf<Int, RepositoryDTO>()

    fun bookmarkRepo(repositoryDTO: RepositoryDTO, bookmarked: Boolean) {
        if (bookmarked)
            bookmarks[repositoryDTO.id!!] = repositoryDTO
        else
            bookmarks.remove(repositoryDTO.id)
    }

    fun getBookmarks() = bookmarks.values
}