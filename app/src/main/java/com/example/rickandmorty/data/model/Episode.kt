package com.example.rickandmorty.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.rickandmorty.data.constants.Constants
import com.google.gson.annotations.SerializedName

@Parcelize
data class EpisodeResponse(
	val results: List<Episode?>? = null
) : Parcelable

@Parcelize
data class Episode(
	@SerializedName(Constants.airDate)
	val airDate: String? = null,
	val characters: List<String?>? = null,
	val created: String? = null,
	val name: String? = null,
	val episode: String? = null,
	val id: Int? = null,
	val url: String? = null
) : Parcelable