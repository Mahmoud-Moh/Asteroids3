package com.udacity.asteroidradar.DataBase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid


@Dao
interface AsteroidDao {
    @Query("select * from asteroid_table")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid_table WHERE close_Approach_Date IN (:days)")
    fun getAsteroidsthisWeek(days: List<String>): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid_table WHERE close_Approach_Date = :today")
    fun getAsteroidsToday(today: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: AsteroidEntity)

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()

}

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}