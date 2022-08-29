package com.udacity.asteroidradar.mars

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DataBase.AsteroidEntity

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val Asteroids: List<NetworkAsteroid>)

/**
 * Asteroids represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkAsteroid(@Json(name = "id")
                                   val id: Long,
                                   @Json(name = "time")
                                   val codename: String,
                                   @Json(name = "time")
                                   val closeApproachDate: String,
                                   @Json(name = "time")
                                   val absoluteMagnitude: Double,
                                   @Json(name = "time")
                                   val estimatedDiameter: Double,
                                   @Json(name = "time")
                                   val relativeVelocity: Double,
                                   @Json(name = "time")
                                   val distanceFromEarth: Double,
                                   @Json(name = "time")
                                   val isPotentiallyHazardous: Boolean)

/**
 * Convert Network results to database objects
 */
fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return Asteroids.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}

fun NetworkAsteroidContainer.asDatabaseModel(): Array<AsteroidEntity> {
    return Asteroids.map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
}