package com.github.tedblair2.issuetracker.helpers

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.github.tedblair2.issuetracker.repository.UserService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val userService: UserService
):HttpInterceptor {

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        val userData=userService.getUser().first()
        val token=""
        return chain.proceed(request.newBuilder().addHeader("Authorization","Bearer $token").build())
    }
}