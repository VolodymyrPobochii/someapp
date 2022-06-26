package com.pobochii.someapp.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pobochii.someapp.R
import com.pobochii.someapp.Result
import com.pobochii.someapp.databinding.UserDetailsFragmentBinding
import kotlinx.coroutines.launch

class UserDetailsFragment : Fragment() {
    private lateinit var viewBinding: UserDetailsFragmentBinding
    private val viewModel by viewModels<UserDetailsViewModel> {
        UserDetailsViewModelFactory(requireActivity().application)
    }
    private val args by navArgs<UserDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(args.userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = UserDetailsFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.user.collect {
                        when (it) {
                            Result.Busy -> viewBinding.apply {
                                progressBar.visibility = View.VISIBLE
                                detailsGroup.visibility = View.INVISIBLE
                            }
                            is Result.Error -> {
                                viewBinding.progressBar.visibility = View.GONE
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            }
                            is Result.Success -> {
                                val details = it.data
                                Glide.with(viewBinding.avatar)
                                    .load(details.profileImage)
                                    .apply(RequestOptions().circleCrop())
                                    .into(viewBinding.avatar)
                                viewBinding.apply {
                                    userName.text =
                                        getString(R.string.label_user_name, details.userName)
                                    reputation.text =
                                        getString(R.string.label_reputation, details.reputation)
                                    topTags.text =
                                        getString(R.string.label_top_tags, details.topTags)
                                    badges.text = getString(R.string.label_badges, details.badges)
                                    location.text =
                                        getString(R.string.label_location, details.location)
                                    creationDate.text =
                                        getString(R.string.label_create_date, details.creationDate)
                                    progressBar.visibility = View.GONE
                                    detailsGroup.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}