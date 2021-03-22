package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.repository.DataRepository

class MyApplication : Application() {
    val dataRepository: DataRepository
        get() = ServiceLocator.provideDataRepository(this)
}