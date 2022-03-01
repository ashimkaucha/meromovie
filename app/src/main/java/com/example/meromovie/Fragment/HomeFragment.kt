package com.example.meromovie.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meromovie.R
import com.example.meromovie.entity.Movie
import com.example.meromovie.movieAdapter
import com.example.meromovie.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    companion object{
        private var lstMovie:ArrayList<Movie> = ArrayList<Movie>()
    }

    private lateinit var RecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        RecyclerView = view.findViewById(R.id.recycler)
        getMovie()

        return view
    }

        private fun getMovie() {
        lstMovie = ArrayList<Movie>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val MovieRepo = MovieRepository()
                val MovieRes = MovieRepo.getAllMovieWithAPI()
                if (MovieRes.success == true) {
                    val movie = MovieRes.data
                    if (movie != null) {
                        movie.forEach{ item ->
                            lstMovie.add(item)
                        }
                        withContext(Main){
                            val post_adapter = context?.let { movieAdapter(it, lstMovie) }
                            RecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            RecyclerView.adapter = post_adapter
                        }
                    }
                }
            } catch (ex: Exception) {
                withContext(Main) {
                    Log.d("rest", ex.printStackTrace().toString())
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}