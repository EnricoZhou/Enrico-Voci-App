package com.voci.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.voci.app.data.local.database.Update
import com.voci.app.data.util.Area
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(update: Update)

    @androidx.room.Update
    suspend fun update(update: Update)

    @Query("DELETE FROM updates")
    suspend fun deleteAllUpdates()

    @Query("DELETE FROM updates WHERE id = :updateId")
    suspend fun deleteById(updateId: String)

    @Query("SELECT * FROM updates")
    fun getAllUpdates(): Flow<List<Update>>

    @Query("SELECT * FROM updates WHERE homelessId = :homelessId")
    fun getUpdatesByHomelessId(homelessId: String): Flow<List<Update>>

    @Query("SELECT * FROM updates")
    fun getAllUpdatesSnapshot(): List<Update>

    @Query("SELECT * FROM updates WHERE area = :area")
    fun getUpdatesByArea(area: Area): Flow<List<Update>>

    @Query("SELECT * FROM updates WHERE id = :id LIMIT 1")
    suspend fun getUpdateById(id: String): Update?

    @Transaction
    suspend fun insertOrUpdate(update: Update) {
        val existingUpdate = getUpdateById(update.id)
        if (existingUpdate == null) {
            insert(update)
        } else {
            update(update)
        }
    }
}