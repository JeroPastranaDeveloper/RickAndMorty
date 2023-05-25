package com.example.rickandmorty.presentation.rickMorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.databinding.CharactersViewHolderBinding

class CharacterAdapter (private var characters: List<Character>, private val onCharacterClick: OnCharacterClick) :
    RecyclerView.Adapter<CharacterAdapter.CharactersViewHolder>() {

    inner class CharactersViewHolder(private val binding: CharactersViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            Glide.with(binding.root.context)
                .load(character.image)
                .into(binding.imageCharacter)

            binding.textName.text = character.name
            binding.textSpecie.text = character.species
            binding.textStatus.text = character.status

            binding.root.setOnClickListener {
                onCharacterClick.onClick(character)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CharactersViewHolderBinding.inflate(inflater, parent, false)
        return CharactersViewHolder(binding)
    }

    fun updateCharacters(newCharacters: List<Character?>) {
        characters = newCharacters.filterNotNull()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    interface OnCharacterClick {
        fun onClick(character: Character)
    }
}
