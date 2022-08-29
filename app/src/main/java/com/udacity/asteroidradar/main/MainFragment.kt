package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.marsrealestate.network.AsteroidFilter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this, OverviewViewModel.Factory(activity?.application)).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel



      binding.asteroidRecycler.adapter = AsteroidListAdapter(AsteroidListAdapter.OnClickListener {
          viewModel.displayAsteroidDetails(it)
      })

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        viewModel.ImageObject.observe(viewLifecycleOwner){imageobject ->
            if(imageobject != null){
                try {
                    Picasso.get().load(imageobject.url)
                        .into(binding.activityMainImageOfTheDay)
                    binding.activityMainImageOfTheDay.contentDescription = "Image of the day, ${imageobject.title}"
                }catch(e: Exception){
                    Picasso.get().load(R.drawable.placeholder_picture_of_day)
                        .into(binding.activityMainImageOfTheDay)
                    binding.activityMainImageOfTheDay.contentDescription = "${R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet}"

                }
            }
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_saved_asteroids_menu -> AsteroidFilter.SAVED
                R.id.show_week_asteroids_menu -> AsteroidFilter.WEEK
                else -> AsteroidFilter.TODAY
            }
        )
        return true
    }
}
