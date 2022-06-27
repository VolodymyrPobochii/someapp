package com.pobochii.someapp.userdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pobochii.someapp.domain.users.GetUserDetailsUseCase
import com.pobochii.someapp.domain.users.Result
import com.pobochii.someapp.domain.users.User
import com.pobochii.someapp.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    application: Application,
    private val getUser: GetUserDetailsUseCase
) : AndroidViewModel(application) {

    private val _user: MutableStateFlow<com.pobochii.someapp.Result<UserDetailsItem>> =
        MutableStateFlow(com.pobochii.someapp.Result.Busy)
    val user: StateFlow<com.pobochii.someapp.Result<UserDetailsItem>> = _user


    fun loadUser(userId: Int) {
        viewModelScope.launch {
            if (!getApplication<Application>().isInternetAvailable()) {
                _user.value = com.pobochii.someapp.Result.Error("No internet connection")
                return@launch
            }
            _user.value = when (val result = getUser(userId)) {
                is Result.Error -> com.pobochii.someapp.Result.Error(result.message)
                is Result.Success -> com.pobochii.someapp.Result.Success(result.data.asUserDetailsItem())

            }
        }
    }
}

data class UserDetailsItem(
    val userName: String = "",
    val reputation: String = "",
    val topTags: String = "",
    val badges: String = "",
    val location: String = "",
    val creationDate: String = "",
    val profileImage: String = ""
)

fun User.asUserDetailsItem() = UserDetailsItem(
    name,
    reputation.toString(),
    topTags.subList(0, (topTags.size * 0.1).roundToInt())
        .joinToString { tag -> tag.name },
    badges.toString(),
    location,
    creation.toString(),
    profileImage
)