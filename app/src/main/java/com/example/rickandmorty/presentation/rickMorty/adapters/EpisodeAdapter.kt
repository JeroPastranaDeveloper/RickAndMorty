package com.example.rickandmorty.presentation.rickMorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.data.model.Episode
import com.example.rickandmorty.databinding.EpisodesViewHolderBinding

class EpisodeAdapter(private var episodes: List<Episode>, private val onEpisodeClick: OnEpisodeClick) :
    RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    inner class EpisodeViewHolder(private val binding: EpisodesViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode) {
            binding.textDate.text = episode.airDate
            binding.textName.text = episode.name
            binding.textSeason.text = binding.root.context.getString(R.string.season, episode.episode?.substring(1, 3))
            binding.textEpisode.text = binding.root.context.getString(R.string.episode, episode.episode?.substring(4, 6))

            binding.root.setOnClickListener {
                onEpisodeClick.onClick(episode)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EpisodesViewHolderBinding.inflate(inflater, parent, false)
        return EpisodeViewHolder(binding)
    }

    fun updateEpisodes(newEpisodes: List<Episode>) {
        episodes = newEpisodes
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodes[position])
    }

    override fun getItemCount(): Int = episodes.size

    interface OnEpisodeClick {
        fun onClick(episode: Episode)
    }
}