package com.example.android.politicalpreparedness.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ElectionDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ElectionDatabase

    @Before
    fun initDB() {
        // Using an in-memory database so that the information stored here
        // disappears when the process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ElectionDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertElectionAndGetById() = runBlockingTest {
        // GIVEN - Insert an Election.
        val election = Election(
            id = 123,
            name = "name",
            electionDay = Date(),
            division = Division(id = "id", country = "country", state = "state")
        )
        database.electionDao.insertElection(election)

        // WHEN - Get the election by id from the database.
        val loaded = database.electionDao.getElectionById(election.id)

        // THEN - The loaded data contains the expected values.
        assertThat<Election>(loaded as Election, notNullValue())
        assertThat(loaded.id, `is`(election.id))
        assertThat(loaded.name, `is`(election.name))
        assertThat(loaded.electionDay, `is`(election.electionDay))
        assertThat(loaded.division.id, `is`(election.division.id))
        assertThat(loaded.division.country, `is`(election.division.country))
        assertThat(loaded.division.state, `is`(election.division.state))
    }
}