package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by viewModels {
        VoterInfoViewModelFactory(
            (requireContext().applicationContext as MyApplication).electionRepository,
            VoterInfoFragmentArgs.fromBundle(requireArguments()).argElection
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        viewModel.electionState.observe(owner = viewLifecycleOwner) {
            if (ElectionState.SAVED == it) {
                binding.buttonFollow.text = getString(R.string.button_unfollow_election)
            } else {
                binding.buttonFollow.text = getString(R.string.button_follow_election)
            }
        }

        viewModel.openUrl.observe(owner = viewLifecycleOwner) { openUrl(it) }

        return binding.root
    }

    private fun openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}