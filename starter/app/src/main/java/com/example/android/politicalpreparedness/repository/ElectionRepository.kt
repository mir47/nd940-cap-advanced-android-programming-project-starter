package com.example.android.politicalpreparedness.repository

interface ElectionRepository {
    suspend fun getElections()
}