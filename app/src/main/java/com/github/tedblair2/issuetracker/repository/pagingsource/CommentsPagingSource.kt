package com.github.tedblair2.issuetracker.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.tedblair2.issuetracker.model.Comment
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.repository.IssuesService

class CommentsPagingSource(
    private val issuesService: IssuesService,
    private val id:String
):PagingSource<String,Comment>() {

    override fun getRefreshKey(state: PagingState<String, Comment>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Comment> {
        return try {
            val endCursor=params.key
            when(val response=issuesService.getComments(id, endCursor)){
                is Response.Success->{
                    val data=response.data?.comments ?: emptyList()
                    LoadResult.Page(
                        data = data,
                        nextKey = response.data?.endCursor,
                        prevKey = null
                    )
                }
                is Response.Error->{
                    LoadResult.Error(Exception(response.error))
                }
                else->{
                    LoadResult.Page(
                        data = emptyList(),
                        nextKey = null,
                        prevKey = null
                    )
                }
            }
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}