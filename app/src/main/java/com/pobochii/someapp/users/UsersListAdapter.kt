package com.pobochii.someapp.users

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pobochii.someapp.databinding.UserListItemBinding

/**
 * Items adapter for users RecyclerView
 */
class UsersListAdapter(
    diffCallback: DiffUtil.ItemCallback<UserListItem>,
    private val onItemClicked: (userId: Int) -> Unit
) :
    ListAdapter<UserListItem, UserListItemViewHolder>(diffCallback), Filterable {

    private var sourceItems: List<UserListItem> = emptyList()

    init {
        setHasStableIds(true)
    }

    fun submitItems(items: List<UserListItem>) {
        sourceItems = items.toMutableList()
        submitList(items)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = UserListItemBinding.inflate(inflater, parent, false)
        return UserListItemViewHolder(viewBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            if (constraint.isEmpty()) {
                return results.apply {
                    values = sourceItems
                    count = sourceItems.size
                }
            }
            val filtered =
                sourceItems.filterIndexed { _, item -> item.text.contains(constraint, true) }
            return results.apply {
                values = filtered
                count = filtered.size
            }
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            submitList(results.values as MutableList<UserListItem>)
        }

    }
}

/**
 * RecyclerView item's view holder
 */
class UserListItemViewHolder(
    private val viewBinding: UserListItemBinding,
    private val onItemClicked: (userId: Int) -> Unit
) :
    ViewHolder(viewBinding.root) {
    fun bind(item: UserListItem) {
        viewBinding.root.apply {
            text = item.text
            setOnClickListener {
                onItemClicked(item.id)
            }
        }
    }
}

data class UserListItem(val id: Int, val text: String)

class UsersDiffCallback : DiffUtil.ItemCallback<UserListItem>() {
    override fun areItemsTheSame(oldItem: UserListItem, newItem: UserListItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserListItem, newItem: UserListItem) =
        oldItem == newItem

}