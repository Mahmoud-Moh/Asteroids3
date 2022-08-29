package com.udacity.asteroidradar.repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.example.android.marsrealestate.network.AsteroidApi
import com.example.android.marsrealestate.network.AsteroidFilter
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DataBase.AsteroidDao
import com.udacity.asteroidradar.DataBase.AsteroidDatabase
import com.udacity.asteroidradar.DataBase.AsteroidEntity
import com.udacity.asteroidradar.DataBase.asDomainModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.mars.asDatabaseModel
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {


    var asteroids : LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }
    var list: List<Asteroid> = listOf()
    suspend fun refreshAsteroidsInit(filter: AsteroidFilter) {
        withContext(Dispatchers.IO) {
            try {
                database.asteroidDao.clear()
                val response = when(filter){
                    AsteroidFilter.TODAY -> AsteroidApi.retrofitService.getAsteroids(getToday(), getToday())
                    AsteroidFilter.WEEK -> AsteroidApi.retrofitService.getAsteroids(getToday(), endofWeek())
                    AsteroidFilter.SAVED -> AsteroidApi.retrofitService.getAsteroids(getToday(), endofWeek())}
                val root = JSONObject(response.body())
                list = parseAsteroidsJsonResult(root).toList()
                database.asteroidDao.insertAll(*list.asDatabaseModel().toTypedArray())
            }catch (se: SocketTimeoutException) {
                Log.e(TAG, "Error: ${se.message}")

            } catch (ex: Throwable) {
                Log.e(TAG, "Error ${ex.message}")

            }

        }
    }


}

fun getToday(): String{
    val calendar = Calendar.getInstance()
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime).toString()
}
fun getWeek(): List<String>{
    val calendar = Calendar.getInstance()
    var days: MutableList<String> = mutableListOf()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        days.add(dateFormat.format(currentTime).toString())
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return days
}

fun endofWeek(): String{
    val calendar = Calendar.getInstance()
    var days: MutableList<String> = mutableListOf()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        days.add(dateFormat.format(currentTime).toString())
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return days.get(days.size-1)
}
