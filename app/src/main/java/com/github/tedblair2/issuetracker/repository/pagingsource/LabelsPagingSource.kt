package com.github.tedblair2.issuetracker.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.tedblair2.issuetracker.repository.IssuesService

class LabelsPagingSource(
    private val issuesService: IssuesService,
    private val username:String
):PagingSource<String,String>() {
    override fun getRefreshKey(state: PagingState<String , String>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String , String> {
        return try {
            val endCursor=params.key
            val response=issuesService.getLabels(username, endCursor)
            val repositories=response.data?.labelNames ?: emptyList()
            LoadResult.Page(
                data = repositories,
                nextKey = response.data?.cursor,
                prevKey = null
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}