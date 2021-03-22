package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse (
    val election: Election,
    @Transient val pollingLocations: String? = null, //TODO: Future Use
    @Transient val contests: String? = null, //TODO: Future Use
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
)