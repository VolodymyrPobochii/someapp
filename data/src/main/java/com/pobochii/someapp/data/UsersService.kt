package com.pobochii.someapp.data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersService {
    @GET("users?order=desc&sort=reputation&pagesize=30&site=stackoverflow")
    fun fetchUsers(): Call<BaseResponse<UserData>>

    @GET("users/{id}?site=stackoverflow")
    fun fetchUser(@Path("id") userId: Int): Call<BaseResponse<UserData>>

    @GET("users/{id}/top-tags?site=stackoverflow")
    fun fetchUserTopTags(@Path("id") userId: Int): Call<BaseResponse<TagData>>
}

data class BaseResponse<out T>(
    val items: List<T> = emptyList(),
    @SerializedName("error_id") val errorId: Int = 0,
    @SerializedName("error_message") val errorMessage: String = "",
    @SerializedName("error_name") val errorName: String = ""
)

val BaseResponse<Any>.hasError: Boolean
    get() = errorId > 0

val BaseResponse<Any>.error: String
    get() = "$errorName:$errorMessage"