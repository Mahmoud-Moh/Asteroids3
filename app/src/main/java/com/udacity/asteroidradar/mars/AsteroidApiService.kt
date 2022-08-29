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

package com.example.android.marsrealestate.network

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.ListOfAsteroidsContainer
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"

enum class AsteroidFilter(val value: String) { TODAY("today"), WEEK("week"), SAVED("saved") }


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService{

    @GET("neo/rest/v1/feed?api_key=9DsMVVwbworZ5iQOQ8RRK16JEEgjrCWOzRUsa3jz")
    suspend fun getAsteroids(@Query("start_date") start_date:String, @Query("end_date") end_date:String):
            Response<String>

    @GET("planetary/apod?api_key=9DsMVVwbworZ5iQOQ8RRK16JEEgjrCWOzRUsa3jz")
    fun getImage():
            Call<PictureOfDay>

}

object AsteroidApi{
    val retrofitService : AsteroidApiService by lazy{
        retrofit.create(AsteroidApiService::class.java)
    }
}

