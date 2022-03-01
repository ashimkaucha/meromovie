package com.example.meromovie
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meromovie.api.ServiceBuilder
import com.example.meromovie.entity.Movie

class movieAdapter(
    private val context: Context,
    private val lstMovie: ArrayList<Movie>,
) : RecyclerView.Adapter<movieAdapter.MovieViewHolder>() {


    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieName: TextView = view.findViewById(R.id.movieName)
        val movieDesc: TextView = view.findViewById(R.id.movieDesc)
        val movieImg: ImageView = view.findViewById(R.id.imageView)
        val btnBook : Button = view.findViewById(R.id.btnBook)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list, parent, false)
        return MovieViewHolder(view)
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = lstMovie[position]
        holder.movieName.text = movie.mname
        holder.movieDesc.text = movie.mdesc.toString()


        Log.d("MovieData", movie.toString())

        Glide.with(context)
            .load(ServiceBuilder.BASE_URL + movie.cover)
            .into(holder.movieImg)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, MovieView::class.java)
            intent.putExtra("Movie",movie)
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return lstMovie.size
    }

}