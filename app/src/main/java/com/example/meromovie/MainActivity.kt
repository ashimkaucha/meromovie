package com.example.meromovie

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.meromovie.Fragment.HomeFragment
import com.example.meromovie.Fragment.ProfileFragment
import com.example.meromovie.model.MovieModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    private var mProximitySensor: Sensor? = null
    private var sensorManager: SensorManager? = null

    companion object {
        private var lstMovie: MutableList<MovieModel>? = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//    private fun getRecyclerView() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val movieRepository= MovieRepository(this@MainActivity)
//                val movieResponse = movieRepository.getAllMovieWithAPI()
//                if (movieResponse.success == true) {
//                    val manyMovies = movieResponse.data!!
//                    for (movie in manyMovies) {
//                        movieRepository.addMovieToDB(movie)
//                    }
//
//                    val movieList = movieRepository.getAllMovieWithDB()
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(
//                            this@MainActivity,
//                            movieList!!.size.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        recyclerView.layoutManager =
//                            LinearLayoutManager(
//                                this@MainActivity,
//                                LinearLayoutManager.VERTICAL,
//                                false
//                            )
//                        recyclerView.adapter = movieAdapter(
//                            this@MainActivity,
//                            movieList as MutableList<MovieModel>
//                        )
//                    }
//                }
//            } catch (ex: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@MainActivity, ex.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

        replacefragment(homeFragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.botNav)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replacefragment(homeFragment)

                R.id.profile -> replacefragment(profileFragment)
            }
            true
        }

        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        mProximitySensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
//        gyroScopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (mProximitySensor == null) {
//            proximty.text = "No Proximity Sensor!"
        } else {
            sensorManager!!.registerListener(
                proximitySensorEventListener,
                mProximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun replacefragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.relativeContainer, fragment)
            transaction.commit()
        }
    }

    private var proximitySensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            val params = this@MainActivity.window.attributes
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {

                if (event.values[0] == 0f) {
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    params.screenBrightness = 0f
                    window.attributes = params
                    Log.d("low Fragment", "low brightness")
                } else {
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    params.screenBrightness = -1f
                    window.attributes = params
                }
            }
        }
    }
}
