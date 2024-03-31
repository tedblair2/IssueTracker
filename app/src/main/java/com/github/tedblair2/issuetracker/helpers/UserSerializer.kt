package com.github.tedblair2.issuetracker.helpers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.tedblair2.issuetracker.UserItem
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserSerializer:Serializer<UserItem> {

    override val defaultValue: UserItem
        get() = UserItem.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserItem {
        try {
            return UserItem.parseFrom(input)
        }catch (e:InvalidProtocolBufferException){
            throw CorruptionException("cannot read proto",e)
        }
    }

    override suspend fun writeTo(t: UserItem, output: OutputStream) {
        t.writeTo(output)
    }
}