package com.pobochii.someapp.users

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pobochii.someapp.data.UsersDataRepository
import com.pobochii.someapp.data.UsersServiceProvider
import com.pobochii.someapp.domain.users.GetUsersSortedUseCase
import com.pobochii.someapp.domain.users.GetUsersUseCase

class UsersViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            error("Unsupported view model's class $modelClass")
        }
        val usersRepo = UsersDataRepository(UsersServiceProvider.create())
        val getUsersUseCase = GetUsersUseCase(usersRepo)
        val getUsers = GetUsersSortedUseCase(getUsersUseCase)
        return UsersViewModel(application, getUsers) as T
    }

}