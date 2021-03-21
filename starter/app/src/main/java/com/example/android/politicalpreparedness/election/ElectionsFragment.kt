package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    private lateinit var electionListAdapter: ElectionListAdapter

    private val viewModel: ElectionsViewModel by viewModels {
        ElectionsViewModelFactory(
            (requireContext().applicationContext as MyApplication).electionRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentElectionBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        electionListAdapter = ElectionListAdapter {
            it.name
        }

        binding.textUpcomingElectionsList.apply {
            adapter = electionListAdapter
        }

        viewModel.elections.observe(owner = viewLifecycleOwner) { list ->
            electionListAdapter.submitList(list)
        }

        viewModel.fetchElections()

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}