package com.danilkomyshev.pokedex.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.danilkomyshev.pokedex.databinding.FavListEntryBinding
import com.danilkomyshev.pokedex.entity.FavouriteList

class FavAdapter(private val clickListener: FavListClickListener) :
    ListAdapter<FavouriteList, FavAdapter.FavViewHolder>(FavDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        return FavViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class FavViewHolder private constructor(private val binding: FavListEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FavouriteList, clickListener: FavListClickListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = FavListEntryBinding.inflate(inflater, parent, false)

                return FavViewHolder(binding)
            }
        }
    }

    class FavDiffCallback : DiffUtil.ItemCallback<FavouriteList>() {
        override fun areItemsTheSame(oldItem: FavouriteList, newItem: FavouriteList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavouriteList, newItem: FavouriteList): Boolean {
            return oldItem == newItem
        }
    }
}