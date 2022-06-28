package com.pobochii.someapp.data

import com.pobochii.someapp.domain.users.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import retrofit2.Response
import retrofit2.mock.Calls

class UsersDataRepositoryTest {

    @Test
    fun findAll_Success_EmptyList() {
        val success = Response.success(BaseResponse<UserData>())
        val fetchUsersResponse = Calls.response(success)
        val service = mock<UsersService> {
            on { fetchUsers() } doReturn fetchUsersResponse
        }

        val repository = UsersDataRepository(service)

        val findAllResponse = runBlocking { repository.findAll() }

        verify(service, times(1)).fetchUsers()
        assert(
            (findAllResponse is Result.Success)
                .and((findAllResponse as Result.Success).data.isEmpty())
        )
    }

    @Test
    fun findAll_Error_400() {
        val success = Response.success(
            BaseResponse<UserData>(
                errorId = 400,
                errorMessage = "An malformed parameter was passed",
                errorName = "bad_parameter"
            )
        )
        val fetchUsersResponse = Calls.response(success)
        val service = mock<UsersService> {
            on { fetchUsers() } doReturn fetchUsersResponse
        }

        val repository = UsersDataRepository(service)

        val findAllResponse = runBlocking { repository.findAll() }

        verify(service, times(1)).fetchUsers()
        assert(findAllResponse is Result.Error)
        val result = findAllResponse as Result.Error
        assert(result.message == success.body()?.error)
    }

    @Test
    fun findById_Success() {
        val userID = 123
        val fetchUsersResponse =
            Calls.response(Response.success(BaseResponse(items = listOf(UserData(userId = userID)))))
        val fetchTagsResponse =
            Calls.response(Response.success(BaseResponse<TagData>()))
        val service = mock<UsersService> {
            on { fetchUser(userID) } doReturn fetchUsersResponse
            on { fetchUserTopTags(userID) } doReturn fetchTagsResponse
        }

        val repository = UsersDataRepository(service)

        val findUserResponse = runBlocking { repository.findById(userID) }

        verify(service, times(1)).fetchUser(userID)
        assert(findUserResponse is Result.Success)
        val result = findUserResponse as Result.Success
        assertEquals(result.data.id, userID)
    }

    @Test
    fun findById_Success_NoUser() {
        val userID = 123
        val fetchUsersResponse = Calls.response(Response.success(BaseResponse<UserData>()))
        val service = mock<UsersService> {
            on { fetchUser(userID) } doReturn fetchUsersResponse
        }

        val repository = UsersDataRepository(service)

        val findUserResponse = runBlocking { repository.findById(userID) }

        verify(service, times(1)).fetchUser(userID)
        assert(findUserResponse is Result.Error)
        val result = findUserResponse as Result.Error
        assertEquals(result.message, "User not found")
    }

    @Test
    fun findById_Error_400() {
        val userId = 123
        val success = Response.success(
            BaseResponse<UserData>(
                errorId = 400,
                errorMessage = "ids",
                errorName = "bad_parameter"
            )
        )
        val fetchUserResponse = Calls.response(success)
        val service = mock<UsersService> {
            on { fetchUser(userId) } doReturn fetchUserResponse
        }

        val repository = UsersDataRepository(service)

        val findUserResponse = runBlocking { repository.findById(userId) }

        verify(service, times(1)).fetchUser(userId)
        assert(findUserResponse is Result.Error)
        val result = findUserResponse as Result.Error
        assert(result.message == success.body()?.error)
    }
}