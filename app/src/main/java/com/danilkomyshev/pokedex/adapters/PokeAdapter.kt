package com.danilkomyshev.pokedex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.danilkomyshev.pokedex.databinding.PokeListEntryBinding
import com.danilkomyshev.pokedex.entity.PokeListEntry

class PokeAdapter(private val clickListener: PokeListClickListener) :
    ListAdapter<PokeListEntry, PokeAdapter.PokeViewHolder>(PokeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeViewHolder {
        return PokeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PokeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class PokeViewHolder private constructor(private val binding: PokeListEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PokeListEntry, clickListener: PokeListClickListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PokeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PokeListEntryBinding.inflate(inflater, parent, false)

                return PokeViewHolder(binding)
            }
        }
    }

    class PokeDiffCallback : DiffUtil.ItemCallback<PokeListEntry>() {
        override fun areItemsTheSame(oldItem: PokeListEntry, newItem: PokeListEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokeListEntry, newItem: PokeListEntry): Boolean {
            return oldItem == newItem
        }
    }
}