package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListViewItemBinding


class AsteroidListAdapter(val onClickListener: OnClickListener ) :
    ListAdapter<Asteroid, AsteroidListAdapter.AsteroidViewHolder>(DiffCallback) {

    class AsteroidViewHolder(private var binding: ListViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }



    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val Asteroid: Asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(Asteroid)
        }
        holder.itemView.contentDescription = "An asteroid, with closeApproach date${Asteroid.closeApproachDate}\n" +
                "and its distance from earth is ${Asteroid.distanceFromEarth}" +
                "and its estimated diameter is ${Asteroid.estimatedDiameter}" +
                "the asteroid relative velocity is ${Asteroid.relativeVelocity}" +
                "the asteroid is ${potentiallyHazardousStr(Asteroid)}"
        holder.bind(Asteroid)
    }

    fun potentiallyHazardousStr(asteroid: Asteroid): String {
        if (asteroid.isPotentiallyHazardous)
            return "potentially hazardous"
        else
            return "not hazardous"
    }


    class OnClickListener(val clickListener: (Asteroid: Asteroid) -> Unit) {
        fun onClick(Asteroid: Asteroid) = clickListener(Asteroid)
    }
}
