package com.example.rickandmorty.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CharacterResponse(
	val results: List<Character?>? = null
) : Parcelable

@Parcelize
data class Origin(
	val name: String? = null,
	val url: String? = null
) : Parcelable

@Parcelize
data class Character(
	val image: String? = null,
	val gender: String? = null,
	val species: String? = null,
	val created: String? = null,
	val origin: Origin? = null,
	val name: String? = null,
	val location: Location? = null,
	val episode: List<String?>? = null,
	val id: Int? = null,
	val type: String? = null,
	val url: String? = null,
	val status: String? = null
) : Parcelable

@Parcelize
data class Location(
	val name: String? = null,
	val url: String? = null
) : Parcelable
