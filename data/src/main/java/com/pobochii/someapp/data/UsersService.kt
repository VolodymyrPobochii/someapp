package com.pobochii.someapp.data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersService {
    @GET("users?order=desc&sort=reputation&pagesize=30&site=stackoverflow")
    fun fetchUsers(): Call<FetchUsersResponse>

    @GET("users/{id}?site=stackoverflow")
    fun fetchUser(@Path("id") userId: Int): Call<FetchUserResponse>

    @GET("users/{id}/top-tags?site=stackoverflow")
    fun fetchUserTopTags(@Path("id") userId: Int): Call<FetchUserTopTagsResponse>
}

open class BaseResponse {
    @SerializedName("error_id")
    val errorId: Int = 0

    @SerializedName("error_message")
    val errorMessage: String = ""

    @SerializedName("error_name")
    val errorName: String = ""
}

val BaseResponse.hasError: Boolean
    get() = errorId > 0

val BaseResponse.error: String
    get() = "$errorName:$errorMessage"

data class FetchUsersResponse(val items: List<UserData>) : BaseResponse()
data class FetchUserResponse(val items: List<UserData>) : BaseResponse()
data class FetchUserTopTagsResponse(val items: List<TagData>) : BaseResponse()