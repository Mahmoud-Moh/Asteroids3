
package com.udacity.asteroidradar.DataBase


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid

@Entity(tableName = "asteroid_table")
data class AsteroidEntity(
    @PrimaryKey(autoGenerate = true)var id: Long,
    @ColumnInfo(name = "code_name") var codename: String,
    @ColumnInfo(name = "close_Approach_Date")var closeApproachDate: String,
    @ColumnInfo(name = "absolute_Magnitude")var absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_Diameter")var estimatedDiameter: Double,
    @ColumnInfo(name = "relative_Velocity")var relativeVelocity: Double,
    @ColumnInfo(name = "distance_FromEarth")var distanceFromEarth: Double,
    @ColumnInfo(name = "isPotentially_Hazardous")var isPotentiallyHazardous: Boolean)

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
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
