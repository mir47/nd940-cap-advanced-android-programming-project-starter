package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

/**
 * Data Access Object for the elections table.
 */
@Dao
interface ElectionDao {

    /**
     * Insert an election in the database. If the election already exists, replace it.
     *
     * @param election the election to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    //TODO: Add select all election query

    //TODO: Add select single election query

    //TODO: Add delete query

    //TODO: Add clear query

}