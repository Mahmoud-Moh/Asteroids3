package com.udacity.asteroidradar.Worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.marsrealestate.network.AsteroidFilter
import com.udacity.asteroidradar.DataBase.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroidsInit(AsteroidFilter.SAVED)
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}
