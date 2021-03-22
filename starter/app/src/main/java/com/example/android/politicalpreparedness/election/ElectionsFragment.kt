package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    private lateinit var upcomingElectionsAdapter: ElectionListAdapter
    private lateinit var savedElectionsAdapter: ElectionListAdapter

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

        upcomingElectionsAdapter = ElectionListAdapter {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it)
            )
        }

        savedElectionsAdapter = ElectionListAdapter {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it)
            )
        }

        binding.listUpcomingElections.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingElectionsAdapter
        }

        binding.listSavedElections.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = savedElectionsAdapter
        }

        viewModel.elections.observe(owner = viewLifecycleOwner) { elections ->
            upcomingElectionsAdapter.submitList(elections)
        }

        viewModel.savedElections.observe(owner = viewLifecycleOwner) { elections ->
            savedElectionsAdapter.submitList(elections)
        }

        return binding.root
    }
}