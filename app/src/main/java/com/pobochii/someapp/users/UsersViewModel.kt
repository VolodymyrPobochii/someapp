package com.pobochii.someapp.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pobochii.someapp.domain.users.GetUsersSortedUseCase
import com.pobochii.someapp.domain.users.Result
import com.pobochii.someapp.domain.users.exhausted
import com.pobochii.someapp.domain.users.exhaustive
import com.pobochii.someapp.utils.isInternetAvailable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class UsersViewModel(
    application: Application,
    getUsers: GetUsersSortedUseCase
) : AndroidViewModel(application) {

    val users: StateFlow<Result<List<UserListItem>>> = flow {
        if (!application.applicationContext.isInternetAvailable()) {
            emit(Result.Error("No internet connection"))
            return@flow
        }
        emit(
            when (val result = getUsers()) {
                is Result.Error -> result
                is Result.Success -> Result.Success(
                    result.data.asSequence()
                        .map {
                            UserListItem(it.id, "${it.id} ${it.name}")
                        }.toList()
                )
            }.exhaustive()
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Result.Busy)
}