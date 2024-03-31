package com.github.tedblair2.issuetracker.repository

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.github.tedblair2.issuetracker.helpers.EmailValidator
import com.github.tedblair2.issuetracker.model.Response
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignInServiceImpl @Inject constructor(
    private val userService: UserService,
    private val context: Context
) : SignInService {

    private val auth=Firebase.auth
    private val emailValidator=EmailValidator()

    override fun signIn(email: String, activity: Activity): Flow<Response<Unit>> = flow{
        val provider= OAuthProvider.newBuilder("github.com")
        provider.addCustomParameter("login",email)
        provider.scopes= listOf("repo")
        val pendingResult=auth.pendingAuthResult
        if (!emailValidator.validateEmail(email)){
            emit(Response.Error("Enter correct email format"))
        }else if (pendingResult != null){
            try {
                val authResult=pendingResult.await()
                val credential=authResult.credential as OAuthCredential
                val accessToken=credential.accessToken
                val profile=authResult.additionalUserInfo?.profile
                val name=profile?.get("login") as String
                accessToken?.let {
                    userService.addUserData(name, accessToken)
                }
                Toast.makeText(context, "Successfully logged In", Toast.LENGTH_SHORT).show()
                emit(Response.Success(Unit))
            }catch (e:Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                emit(Response.Error(""))
            }
        }else {
            try {
                val authResult=auth.startActivityForSignInWithProvider(activity,provider.build()).await()
                val credential=authResult.credential as OAuthCredential
                val accessToken=credential.accessToken
                val profile=authResult.additionalUserInfo?.profile
                val name=profile?.get("login") as String
                accessToken?.let {
                    userService.addUserData(name, accessToken)
                }
                Toast.makeText(context, "Successful sign Up", Toast.LENGTH_SHORT).show()
                emit(Response.Success(Unit))
            }catch (e:Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                emit(Response.Error(""))
            }
        }
    }

    override fun checkSignIn(): Flow<Response<Unit>> = flow{
        if (auth.currentUser != null){
            emit(Response.Success(Unit))
        }else{
            emit(Response.Error("User not logged in"))
        }
    }

    override fun signOut(): Flow<Response<Unit>> = flow{
        auth.signOut()
        emit(Response.Success(Unit))
    }
}