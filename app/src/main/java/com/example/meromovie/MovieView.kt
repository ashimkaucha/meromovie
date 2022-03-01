package com.example.meromovie

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.meromovie.Fragment.NotificationChannel
import com.example.meromovie.api.ServiceBuilder
import com.example.meromovie.entity.Movie
import com.example.meromovie.repository.MovieRepository
import com.example.meromovie.repository.ShowTimeRepository
import com.example.meromovie.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieView : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    private lateinit var imageView: ImageView
    private lateinit var movieName: TextView
    private lateinit var movieDesc: TextView
    private lateinit var movieCategory: TextView
    private lateinit var movieReleaseDate: TextView
    private lateinit var movieShowTime: TextView
    private lateinit var moviePrice: TextView
    private lateinit var etSeat: EditText
    private lateinit var btnBook: Button
    private var movieid: String? = null
    private var str: Movie? = null
    private var showTimeId: String? = ""

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_view)
        imageView = findViewById(R.id.imageView)
        movieName = findViewById(R.id.movieName)
        movieDesc = findViewById(R.id.movieDesc)
        movieCategory = findViewById(R.id.movieCategory)
        movieReleaseDate = findViewById(R.id.movieReleaseDate)
        movieShowTime = findViewById(R.id.movieShowTime)
        moviePrice = findViewById(R.id.moviePrice)
        etSeat = findViewById(R.id.etSeat)
        btnBook = findViewById(R.id.btnBook)

        str = intent.getParcelableExtra<Movie>("Movie")
        movieid = str?._id
        getmovie()
        btnBook.setOnClickListener {
            book()
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun book() {
        val seat = etSeat.text.toString()
        if (TextUtils.isEmpty(seat)) {
            Toast.makeText(this@MovieView, "Please enter number of seats", Toast.LENGTH_SHORT)
                .show()
        } else {
            CoroutineScope(IO).launch {
                val repository = MovieRepository()
                val response =
                    repository.booking(showtimeId = showTimeId!!, seat = seat)

                if (response.success == true) {
                    withContext(Main) {
                        Toast.makeText(
                            this@MovieView,
                            "${seat} seats has been booked",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        startActivity(
                            Intent
                                (this@MovieView, MainActivity::class.java)
                        )
                    }
                }
            }
        }
    }

    private fun getmovie() {
        val movieId = movieid.toString()

        CoroutineScope(IO).launch {

            try {
                val repository = ShowTimeRepository()
                val response = repository.getSingleMovie(movieId)

                if (response.success == true) {
                    movieName.setText("${response.data?.mname}")
                    movieDesc.setText("${response.data?.mdesc}")
                    movieReleaseDate.setText("${response.data?.releasedate}")
                    movieCategory.setText("${response.data?.mcategories}")

                    withContext(Dispatchers.Main) {

                            Glide.with(this@MovieView).asBitmap()
                                .load(ServiceBuilder.BASE_URL + response.data?.cover).into(
                                    BitmapImageViewTarget(imageView)
                                )


                    }

                    try {
                        val repository = ShowTimeRepository()
                        val response = repository.showtime(movieId)

                        if (response.success == true) {
                            movieShowTime.setText("${response.data?.datetime}")
                            moviePrice.setText("Rs. ${response.data?.price}")
                            showTimeId = response.data?._id

                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Log.d("Error", ex.toString())
                            Toast.makeText(
                                this@MovieView,
                                ex.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("Error", ex.toString())
                    Toast.makeText(
                        this@MovieView,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
//        CoroutineScope(Dispatchers.Main).launch {
//            str = intent.getParcelableExtra<Movie>("Movie")
//            movieid = str?._id
//            movieName.setText(str?.mname)
//            movieDesc.setText(str?.mdesc)
//            movieReleaseDate.setText(str?.releasedate)
//            movieCategory.setText(str?.mcategories)
//
//            Glide.with(this@MovieView)
//                .load(ServiceBuilder.BASE_URL + str?.cover)
//                .into(imageView)
//        }
    }


    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.values[2] > 0.5f) { // anticlockwise
            Toast.makeText(this@MovieView, "GYro", Toast.LENGTH_SHORT).show()
//            book()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun book1() {
//        val movieReviews = movieReview.text.toString()
//        val reviewTitles = reviewTitle.text.toString()

//        val reviews = Movie(
//            _id = movieid,
////            review = movieReviews,
////            reviewTitle = reviewTitles
//        )
//        Log.d("feel", reviews.toString())
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val repository = MovieRepository()
//                val response =
//                    repository.addReview(ServiceBuilder.token!!, ServiceBuilder.userID!!, reviews)
//                if (response.success == true) {
//                    withContext(Dispatchers.Main) {
//
//                    }
//                }
//            } catch (ex: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@MovieView, ex.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
        //       }
    }

    private fun watchlistNotification() {

        val notificationManager = this?.let { NotificationManagerCompat.from(it) }

        val notificationChannels = this?.let { NotificationChannel(it) }
        notificationChannels?.createNotificationChannels()

        val notification =
            this?.let {
                notificationChannels?.let { it1 ->
                    NotificationCompat.Builder(it, it1.CHANNEL_1)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("Watchlist")
                        .setContentText("Added to Watchlist")
                        .setColor(Color.BLUE)
                        .build()
                }
            }

        if (notificationManager != null) {
            if (notification != null) {
                notificationManager.notify(1, notification)
            }
        }

    }

//    private fun book() {
//        val seat = etSeat.text.toString()
//        val showtimeId = "a"
//        CoroutineScope(IO).launch {
//        try {
//            val repository = MovieRepository()
//            val response = repository.booking(showtimeId, seat)
//            if (response.success == true) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        this@MovieView,
//                        "Successfully registered",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    startActivity(Intent(this@MovieView, LogInActivity::class.java))
//                }
//            }
//        } catch (ex: Exception) {
//            withContext(Dispatchers.Main) {
//                Log.d("errorrrr", ex.printStackTrace().toString())
//                Toast.makeText(
//                    this@MovieView, ex.toString(), Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//        }
//    }
    //  }

}