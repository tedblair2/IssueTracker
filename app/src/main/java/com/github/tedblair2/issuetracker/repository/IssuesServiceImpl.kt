package com.github.tedblair2.issuetracker.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.github.tedblair2.CommentsQuery
import com.github.tedblair2.IssueQuery
import com.github.tedblair2.IssuesQuery
import com.github.tedblair2.issuetracker.mappers.toCommentData
import com.github.tedblair2.issuetracker.mappers.toDetailedIssue
import com.github.tedblair2.issuetracker.mappers.toIssuePage
import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.Response
import javax.inject.Inject

class IssuesServiceImpl @Inject constructor(
    private val apolloClient: ApolloClient
): IssuesService {

    override suspend fun getIssues(username: String,endCursor:String?): Response<IssuePage>{
        val end=Optional.presentIfNotNull(endCursor)
        return try {
            val data=apolloClient
                .query(IssuesQuery(username,10,end))
                .execute()
                .data
                ?.user
                ?.issues
                ?.toIssuePage()
                ?: IssuePage()
            Response.Success(data)
        }catch (e: ApolloException){
            Response.Error(e.message ?: "Something went wrong!")
        }catch(e:Exception){
            Response.Error(e.message ?: "Something went wrong!")
        }
    }

    override suspend fun getIssue(id: String): Response<DetailedIssue> {
        return try {
            val execute=apolloClient
                .query(IssueQuery(id))
                .execute()
            if (execute.hasErrors()){
                Response.Error("Error fetching data!")
            }else{
                val data=execute
                    .data
                    ?.node
                    ?.onIssue
                    ?.toDetailedIssue()
                Response.Success(data = data)
            }
        }catch (e:ApolloException){
            Response.Error(e.message ?: "Something went wrong!")
        }catch (e:Exception){
            Response.Error(e.message ?: "Something went wrong!")
        }
    }

    override suspend fun getComments(id: String, endCursor: String?): Response<CommentData> {
        val end=Optional.presentIfNotNull(endCursor)
        return try {
            val execution=apolloClient
                .query(CommentsQuery(id,20,end))
                .execute()
            if (execution.hasErrors()){
                Response.Error("Error fetching data")
            }else{
                val data=execution
                    .dataAssertNoErrors
                    .node
                    ?.onIssue
                    ?.comments
                    ?.toCommentData() ?: CommentData()
                Response.Success(data = data)
            }
        }catch (e:ApolloException){
            Response.Error(e.message ?: "Error fetching data")
        }catch (e:Exception){
            Response.Error(e.message ?: "Error fetching data")
        }
    }
}