package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.repository.ElectionRepository

class MyApplication : Application() {
    val electionRepository: ElectionRepository
        get() = ServiceLocator.provideElectionRepository(this)
}