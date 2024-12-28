package com.example.vociapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vociapp.data.local.database.Request
import com.example.vociapp.data.types.Area
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {
    @Query("SELECT * FROM requests")
    fun getAllRequests(): Flow<List<Request>>

    @Query("SELECT * FROM requests")
    suspend fun getAllRequestsSnapshot(): List<Request>

    @Query("SELECT * FROM requests WHERE id = :requestId LIMIT 1")
    suspend fun getRequestById(requestId: String): Request

    @Query("SELECT * FROM requests WHERE area = :area")
    fun getRequestsByArea(area: Area): Flow<List<Request>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(request: Request)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(requestsList: List<Request>)

    @Transaction
    suspend fun insertOrUpdate(request: Request) {
        val existingRequest = getRequestById(request.id)
        if (existingRequest == null) {
            insert(request)
        } else {
            update(request)
        }
    }

    @Update
    suspend fun update(request: Request)

    @Delete
    suspend fun delete(request: Request)

    @Query("DELETE FROM requests WHERE id = :requestId")
    suspend fun deleteById(requestId: String)
}
