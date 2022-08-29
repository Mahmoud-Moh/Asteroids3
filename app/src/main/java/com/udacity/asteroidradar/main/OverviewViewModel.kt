/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.example.android.marsrealestate.network.AsteroidApi
import com.example.android.marsrealestate.network.AsteroidFilter
import com.google.gson.Gson
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DataBase.asDomainModel
import com.udacity.asteroidradar.DataBase.getDatabase
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.getToday
import com.udacity.asteroidradar.repository.getWeek
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


enum class MarsApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    val ImageObject = MutableLiveData<PictureOfDay?>()


    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val _status = MutableLiveData<MarsApiStatus>()

    val status: LiveData<MarsApiStatus>
        get() = _status

    private val database = getDatabase(application)
    private val repo = AsteroidRepository(database)
    init {
        viewModelScope.launch {
            repo.refreshAsteroidsInit(AsteroidFilter.SAVED)
        }
        loadDayImage()
    }

    var playlist : LiveData<List<Asteroid>> = repo.asteroids

    fun loadDayImage(){
        AsteroidApi.retrofitService.getImage().enqueue(object: Callback<PictureOfDay>{
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                if(response.body()?.mediaType == "video")
                    ImageObject.value = null
                else
                    ImageObject.value = response.body()
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                ImageObject.value = null
            }

        })
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

    fun getToday(): String{
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime).toString()
    }

    fun updateFilter(filter: AsteroidFilter) {
        viewModelScope.launch {
            repo.refreshAsteroidsInit(filter)
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    class Factory(val app: Application?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return app?.let { OverviewViewModel(it) } as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
