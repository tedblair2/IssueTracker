package com.github.tedblair2.issuetracker.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.tedblair2.issuetracker.model.SimpleIssue
import com.github.tedblair2.issuetracker.model.State
import com.github.tedblair2.issuetracker.repository.IssuesService
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class IssuePagingSource(
    private val issuesService: IssuesService,
    private val username:String,
    private val state:List<State>,
    private val repositoryFilterList: List<String> = listOf(),
    private val labels:List<String> = emptyList(),
    private val startDate: LocalDate?=null,
    private val endDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
):PagingSource<String,SimpleIssue>() {

    override fun getRefreshKey(state: PagingState<String, SimpleIssue>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, SimpleIssue> {
        return try{
            val endCursor=params.key
            val response=issuesService.getIssues(username,
                endCursor,state,
                repositoryFilterList = repositoryFilterList,
                labels = labels,
                endDate = endDate,
                startDate = startDate)
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