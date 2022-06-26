package com.pobochii.someapp.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pobochii.someapp.domain.users.GetUsersSortedUseCase
import com.pobochii.someapp.domain.users.Result
import com.pobochii.someapp.utils.isInternetAvailable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class UsersViewModel(
    application: Application,
    getUsers: GetUsersSortedUseCase
) : AndroidViewModel(application) {

    val users: StateFlow<com.pobochii.someapp.Result<List<UserListItem>>> = flow {
        if (!application.applicationContext.isInternetAvailable()) {
            emit(com.pobochii.someapp.Result.Error("No internet connection"))
            return@flow
        }
        emit(
            when (val result = getUsers()) {
                is Result.Error -> com.pobochii.someapp.Result.Error(result.message)
                is Result.Success -> com.pobochii.someapp.Result.Success(
                    result.data.asSequence()
                        .map {
                            UserListItem(it.id, "${it.id} ${it.name}")
                        }.toList()
                )
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, com.pobochii.someapp.Result.Busy)
}