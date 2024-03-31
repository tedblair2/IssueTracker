package com.github.tedblair2.issuetracker.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.tedblair2.issuetracker.UserItem
import com.github.tedblair2.issuetracker.helpers.UserSerializer
import com.github.tedblair2.issuetracker.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DATASTORE_FILE_NAME="userdata.pb"
private val Context.usersPreference:DataStore<UserItem> by dataStore(
    fileName = DATASTORE_FILE_NAME,
    serializer = UserSerializer
)
class UserServiceImpl @Inject constructor(
    context: Context
) : UserService {

    private val userPref=context.usersPreference

    override suspend fun addUserData(username:String,accessToken:String) {
        userPref.updateData {userItem ->
            userItem.toBuilder()
                .setAccessToken(accessToken)
                .setUsername(username)
                .build()
        }
    }

    override fun getUser():Flow<User> {
        return userPref.data
            .map {
                User(username = it.username,
                    accessToken = it.accessToken)
            }
    }
}