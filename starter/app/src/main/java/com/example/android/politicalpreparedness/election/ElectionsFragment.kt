package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
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
            Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
        }

        binding.listUpcomingElections.apply {
            layoutManager = LinearLayoutManager(context)
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