package com.github.tedblair2.issuetracker.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.github.tedblair2.issuetracker.helpers.AuthenticationInterceptor
import com.github.tedblair2.issuetracker.repository.CommentsRepository
import com.github.tedblair2.issuetracker.repository.IssueRepository
import com.github.tedblair2.issuetracker.repository.IssuesService
import com.github.tedblair2.issuetracker.repository.IssuesServiceImpl
import com.github.tedblair2.issuetracker.repository.SignInService
import com.github.tedblair2.issuetracker.repository.SignInServiceImpl
import com.github.tedblair2.issuetracker.repository.UserService
import com.github.tedblair2.issuetracker.repository.UserServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun provideIoDispatcher():CoroutineDispatcher=Dispatchers.IO

    @Provides
    @Singleton
    fun provideUserService(@ApplicationContext context: Context):UserService{
        return UserServiceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSignInService(@ApplicationContext context: Context,userService: UserService):SignInService{
        return SignInServiceImpl(userService, context)
    }

    @Provides
    fun provideAuthInterceptor(userService: UserService):HttpInterceptor{
        return AuthenticationInterceptor(userService)
    }

    @Provides
    @Singleton
    fun provideApolloClient(httpInterceptor: HttpInterceptor):ApolloClient{
        return ApolloClient.Builder()
            .serverUrl("https://api.github.com/graphql")
            .addHttpInterceptor(httpInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideIssuesService(apolloClient: ApolloClient):IssuesService{
        return IssuesServiceImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideIssueRepository(issuesService: IssuesService):IssueRepository{
        return IssueRepository(issuesService)
    }

    @Provides
    @Singleton
    fun provideCommentsRepository(issuesService: IssuesService):CommentsRepository{
        return CommentsRepository(issuesService)
    }
}