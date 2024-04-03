package com.github.tedblair2.issuetracker.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.tedblair2.issuetracker.repository.IssuesService

class RepositoryPagingSource(
    private val username:String,
    private val issuesService: IssuesService,
    private val filter:String
):PagingSource<String,String>() {
    override fun getRefreshKey(state: PagingState<String , String>): String? {
        return null
    }
    override suspend fun load(params: LoadParams<String>): LoadResult<String , String> {
        return try {
            val endCursor=params.key
            val response=issuesService.getRepository(username, endCursor,filter)
            val repositories=response.data?.names ?: emptyList()
            LoadResult.Page(
                data = repositories,
                nextKey = response.data?.endCursor,
                prevKey = null
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}