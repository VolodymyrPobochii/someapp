package com.pobochii.someapp.data

import android.util.LruCache
import com.pobochii.someapp.domain.users.Result
import com.pobochii.someapp.domain.users.User
import com.pobochii.someapp.domain.users.UsersRepository
import retrofit2.awaitResponse

/**
 * Implementation of [UsersRepository]
 * @property dataSource Users remote data source [UsersService]
 */
class UsersDataRepository(private val dataSource: UsersService) : UsersRepository {

    private val cache: LruCache<Int, User?> = LruCache(30)

    override suspend fun findAll(): Result<List<User>> {
        val fetchUsers = dataSource.fetchUsers()
        val awaitResponse = fetchUsers.awaitResponse()
        val response = awaitResponse.body() ?: return Result.Error("unknown")
        if (response.hasError) {
            return Result.Error(response.error)
        }
        val items = response.items
        val mapped = items.asSequence()
            .map(UserData::asUser)
            .toList()
        return Result.Success(mapped)
    }

    override suspend fun findById(id: Int): Result<User> {
        cache[id]?.let {
            return Result.Success(it)
        }
        val fetchUser = dataSource.fetchUser(id)
        val fetchUserResponse = fetchUser.awaitResponse().body() ?: return Result.Error("unknown")
        if (fetchUserResponse.hasError) {
            return Result.Error(fetchUserResponse.error)
        }
        val users = fetchUserResponse.items
        if (users.isEmpty()) {
            return Result.Error("User not found")
        }
        val userTopTags = dataSource.fetchUserTopTags(id)
        val userTopTagsResponse =
            userTopTags.awaitResponse().body() ?: return Result.Error("unknown")
        if (userTopTagsResponse.hasError) {
            return Result.Error(userTopTagsResponse.error)
        }
        val topTags = userTopTagsResponse.items.asSequence().map(TagData::asTag).toList()
        val user = users[0].asUser(topTags)
        cache.put(user.id, user)
        return Result.Success(user)
    }
}