package com.github.tedblair2.issuetracker.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.tedblair2.issuetracker.model.Comment
import com.github.tedblair2.issuetracker.repository.pagingsource.CommentsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val issuesService: IssuesService
) {
    fun getComments(id:String):Flow<PagingData<Comment>>{
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {CommentsPagingSource(issuesService, id)}
        ).flow
    }
}