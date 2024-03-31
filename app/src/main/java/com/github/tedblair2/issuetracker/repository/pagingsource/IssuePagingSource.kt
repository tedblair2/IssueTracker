package com.github.tedblair2.issuetracker.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.tedblair2.issuetracker.model.SimpleIssue
import com.github.tedblair2.issuetracker.repository.IssuesService

class IssuePagingSource(
    private val issuesService: IssuesService,
    private val username:String
):PagingSource<String,SimpleIssue>() {

    override fun getRefreshKey(state: PagingState<String, SimpleIssue>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, SimpleIssue> {
        return try{
            val endCursor=params.key
            val response=issuesService.getIssues(username, endCursor)
            val issues=response.data?.nodes ?: emptyList()
            LoadResult.Page(
                data = issues,
                nextKey = response.data?.endCursor,
                prevKey = null
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}