package com.example.meromovie.Fragment

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.meromovie.LogInActivity
import com.example.meromovie.R
import com.example.meromovie.api.ServiceBuilder
import com.example.meromovie.entity.User
import com.example.meromovie.repository.UserRepository
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

class ProfileFragment : Fragment() {
//    sensor
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
//    end of sensor

    private lateinit var UserImage: ImageView
    private lateinit var fname: EditText
    private lateinit var lname: EditText
    private lateinit var etphone : EditText
    private lateinit var btnupdate: Button
    private lateinit var btnLogout: Button
    private lateinit var sharedPreferences: SharedPreferences;
    private lateinit var editor: SharedPreferences.Editor;
    val CHANNEL: String = "Channel"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        UserImage = view.findViewById(R.id.image)
        fname = view.findViewById(R.id.fname)
        lname = view.findViewById(R.id.lname)
        etphone = view.findViewById(R.id.etphone)
        btnupdate = view.findViewById(R.id.btnupdate)
        btnLogout = view.findViewById(R.id.btnLogout)
        getUser()

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        sharedPreferences = activity?.getSharedPreferences("LoginPref", AppCompatActivity.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        if (!CheckRuntimePermission()) {
            Askpermission()
        }

        UserImage.setOnClickListener {
            loadpopupmenu()
        }

        btnupdate.setOnClickListener {
            updateuser()
        }


        btnLogout.setOnClickListener{
            Log_fun()
        }

        return view
    }



    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                Toast.makeText(
                    activity,
                    "shake",
                    Toast.LENGTH_SHORT
                ).show()
                Log_fun()

            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun getUser() {
        val id = ServiceBuilder.userID!!.toString()
        println(id)
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val profile = UserRepository()
                val response = profile.getuser(id)

                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        context?.let {
                            Glide.with(it).asBitmap()
                                .placeholder(R.drawable.profile)
                                .load(ServiceBuilder.BASE_URL + response.data?.profile).into(
                                    BitmapImageViewTarget(UserImage)
                                )

                            etphone.setText(response.data?.phoneNumber)
                            fname.setText(response.data?.fname)
                            lname.setText(response.data?.lname)
                        }
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("error", ex.printStackTrace().toString())
                    Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadpopupmenu() {
        val popMenu = PopupMenu(context, UserImage)
        popMenu.menuInflater.inflate(R.menu.menu, popMenu.menu)
        popMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.camera) {
                openCamera()
                Toast.makeText(context, "Camera clicked", Toast.LENGTH_SHORT)
                    .show()
            } else if (item.itemId == R.id.Gallery) {
                openGallery()
                Toast.makeText(context, "Gallery clicked", Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }
        popMenu.show()
    }

    private val permission = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    private fun CheckRuntimePermission(): Boolean {
        var haspermission = true
        for (permission in permission) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                haspermission = false
                break
            }
        }
        return haspermission
    }

    private fun Askpermission() {
        requestPermissions(permission, 1)
    }

    private val CAMER_CODE = 1
    private val GALLERY_CODE = 0
    private fun openGallery() {
        val galleryOpen = Intent(Intent.ACTION_PICK)
        galleryOpen.type = "image/*"
        startActivityForResult(galleryOpen, GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMER_CODE)
    }

    private var imageUrl = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Gallery ko image Image view ma dekhauni
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filepathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val resolver = requireActivity().contentResolver
                val contentResolver = resolver
                val cursor =
                    contentResolver.query(selectedImage!!, filepathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filepathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                UserImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == CAMER_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapTofile(imageBitmap, "$timeStamp.png")
                imageUrl = file!!.absolutePath
                UserImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
    }

    private fun bitmapTofile(
        bitmap: Bitmap,
        fileNametoSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNametoSave
            )
            file.createNewFile()
            //Convert bitmap to byte Array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bitMapData = bos.toByteArray()
            //write the byte in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file // it will return null
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file
        }
    }

    private fun updateuser() {
        val fname = fname.text.toString()
        val lname = lname.text.toString()
        val phone = etphone.text.toString()

        val data = User(
            fname = fname, lname = lname, phoneNumber = phone
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userrepo = UserRepository()
                val userres = userrepo.updateuser(data)
                if (userres.success == true) {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show()
                    }
                    if(imageUrl != ""){
                        Updateimage()
                    }
                    else{

                        val notificationManager = context?.let { NotificationManagerCompat.from(it) }

                        val notificationChannels = context?.let { NotificationChannel(it) }
                        notificationChannels?.createNotificationChannels()

                        val notification =
                            context?.let {
                                notificationChannels?.let { it1 ->
                                    NotificationCompat.Builder(it, it1.CHANNEL_1)
                                        .setSmallIcon(R.drawable.notification)
                                        .setContentTitle("Update")
                                        .setContentText("Profile Upto date")
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
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("notifications",ex.printStackTrace().toString())
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun Updateimage() {

        val file = File(imageUrl)
        val mimeType = getMimeType(file)
        val reqFile = RequestBody.create(MediaType.parse(mimeType), file)
        val body = MultipartBody.Part.createFormData("file", file.name, reqFile)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userrepo = UserRepository()
                val response = userrepo.updateimage( body)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {

                        Toast.makeText(
                            activity,
                            "Update successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val notificationManager = context?.let { NotificationManagerCompat.from(it) }

                    val notificationChannels = context?.let { NotificationChannel(it) }
                    notificationChannels?.createNotificationChannels()

                    val notification =
                        context?.let {
                            notificationChannels?.let { it1 ->
                                NotificationCompat.Builder(it, it1.CHANNEL_1)
                                    .setSmallIcon(R.drawable.notification)
                                    .setContentTitle("Update")
                                    .setContentText("Profile picture updated")
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
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    println(ex.toString())
                    Toast.makeText(
                        activity,
                        "Image upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

      private fun Log_fun(){
        editor.remove("token")
        editor.remove("firstname")
        editor.remove("lastname")
        editor.remove("password")
        editor.remove("userId")
        editor.apply()
        editor.commit()
          startActivity(Intent(context, LogInActivity::class.java))
          requireActivity().finish()
          Toast.makeText(context,"Logged Out", Toast.LENGTH_SHORT).show()
    }
}