package com.pobochii.someapp.data

object UsersServiceProvider {
    fun create(): UsersService {
        return RetrofitProvider.create()
            .create(UsersService::class.java)
    }
}