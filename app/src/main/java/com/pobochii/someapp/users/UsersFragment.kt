package com.pobochii.someapp.users

import android.os.Bundle
import android.view.*
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pobochii.someapp.R
import com.pobochii.someapp.Result
import com.pobochii.someapp.databinding.UsersFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private lateinit var usersListAdapter: UsersListAdapter
    private lateinit var viewBinding: UsersFragmentBinding
    private val viewModel by activityViewModels<UsersViewModel>()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.users_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(SearchQueryListener(usersListAdapter))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        usersListAdapter = UsersListAdapter(UsersDiffCallback()) { userId ->
            val toUserDetails = UsersFragmentDirections.navToUserDetails(userId)
            findNavController().navigate(toUserDetails)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = UsersFragmentBinding.inflate(inflater, container, false).apply {
            usersList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = usersListAdapter
            }
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.users.collect {
                    when (it) {
                        Result.Busy -> viewBinding.progressBar.visibility = View.VISIBLE
                        is Result.Error -> {
                            viewBinding.apply {
                                progressBar.visibility = View.GONE
                                noData.visibility = View.VISIBLE
                            }
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            viewBinding.progressBar.visibility = View.GONE
                            if (it.data.isEmpty()) {
                                viewBinding.noData.visibility = View.VISIBLE
                                return@collect
                            }
                            usersListAdapter.submitItems(it.data)
                        }
                    }
                }
            }
        }
    }

    class SearchQueryListener(private val adapter: Filterable) : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            adapter.filter.filter(newText)
            return true
        }

    }
}