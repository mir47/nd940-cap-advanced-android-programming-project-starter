package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val listener: ElectionListener) :
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ELECTION_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val binding = ItemElectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ElectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it, listener) }
    }

    /**
     * ViewHolder for Election items. All work is done by data binding.
     */
    class ElectionViewHolder(private val binding: ItemElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Election, listener: ElectionListener) {
            binding.item = item
            binding.listener = listener
        }
    }

    fun interface ElectionListener {
        fun onItemClick(election: Election)
    }

    companion object {
        private val ELECTION_COMPARATOR = object : DiffUtil.ItemCallback<Election>() {
            override fun areItemsTheSame(old: Election, new: Election) = old.id == new.id
            override fun areContentsTheSame(old: Election, new: Election) = old == new
        }
    }
}