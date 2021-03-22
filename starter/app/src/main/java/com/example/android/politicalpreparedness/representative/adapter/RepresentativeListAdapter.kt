package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter(
    private val clickListener: RepresentativeListener
) : ListAdapter<Representative, RepresentativeViewHolder>(REPRESENTATIVE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepresentativeViewHolder.from(parent)

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun interface RepresentativeListener {
        fun onItemClick(representative: Representative)
    }

    companion object {
        private val REPRESENTATIVE_COMPARATOR = object : DiffUtil.ItemCallback<Representative>() {
            override fun areItemsTheSame(old: Representative, new: Representative) = old.official == new.official
            override fun areContentsTheSame(old: Representative, new: Representative) = old == new
        }
    }
}

class RepresentativeViewHolder(private val binding: ItemRepresentativeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Representative) {
        binding.item = item
//        binding.representativePhoto.setImageResource(R.drawable.ic_profile)

        //TODO: Show social links ** Hint: Use provided helper methods
        //TODO: Show www link ** Hint: Use provided helper methods

        binding.executePendingBindings()
    }

    //TODO: Add companion object to inflate ViewHolder (from)

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
//            enableLink(binding.facebookIcon, facebookUrl)
        }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) {
//            enableLink(binding.twitterIcon, twitterUrl)
        }
    }

    private fun showWWWLinks(urls: List<String>) {
//        enableLink(binding.wwwIcon, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
            .map { channel -> "https://www.facebook.com/${channel.id}" }
            .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
            .map { channel -> "https://www.twitter.com/${channel.id}" }
            .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRepresentativeBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }
}
