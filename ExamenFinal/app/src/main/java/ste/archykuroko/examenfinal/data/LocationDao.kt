package ste.archykuroko.examenfinal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(item: LocationEntity)

    @Query("SELECT * FROM locations ORDER BY timestampMillis DESC")
    fun observeAllDesc(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations ORDER BY timestampMillis ASC")
    fun observeAllAsc(): Flow<List<LocationEntity>>

    @Query("DELETE FROM locations")
    suspend fun clear()
}
