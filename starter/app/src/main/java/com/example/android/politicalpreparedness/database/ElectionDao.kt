package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

/**
 * Data Access Object for the elections table.
 */
@Dao
interface ElectionDao {

    /**
     * Observes list of elections.
     *
     * @return all elections.
     */
    @Query("SELECT * FROM election_table")
    fun observeElections(): LiveData<List<Election>>

    /**
     * Observes a single election.
     *
     * @param electionId the election id.
     * @return the election with electionId.
     */
    @Query("SELECT * FROM election_table WHERE id = :electionId")
    fun observeElectionById(electionId: Int): LiveData<Election>

    /**
     * Select all elections from the elections table.
     *
     * @return all elections.
     */
    @Query("SELECT * FROM election_table")
    suspend fun getElections(): List<Election>

    /**
     * Select an election by id.
     *
     * @param electionId the election id.
     * @return the election with electionId.
     */
    @Query("SELECT * FROM election_table WHERE id = :electionId")
    suspend fun getElectionById(electionId: Int): Election?

    /**
     * Insert an election in the database. If the election already exists, replace it.
     *
     * @param election the election to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    /**
     * Delete an election by id.
     *
     * @return the number of elections deleted. This should always be 1.
     */
    @Query("DELETE FROM election_table WHERE id = :electionId")
    suspend fun deleteElectionById(electionId: Int): Int

    /**
     * Delete all elections.
     */
    @Query("DELETE FROM election_table")
    suspend fun deleteElections()
}