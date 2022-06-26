package com.pobochii.someapp.userdetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pobochii.someapp.data.UsersDataRepository
import com.pobochii.someapp.data.UsersServiceProvider
import com.pobochii.someapp.domain.users.GetUserDetailsUseCase

class UserDetailsViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
            error("Unsupported view model's class $modelClass")
        }
        val usersRepo = UsersDataRepository(UsersServiceProvider.create())
        val getUserDetails = GetUserDetailsUseCase(usersRepo)
        return UserDetailsViewModel(application, getUserDetails) as T
    }

}