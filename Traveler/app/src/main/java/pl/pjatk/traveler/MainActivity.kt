package pl.pjatk.traveler


import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.*
import android.icu.text.SimpleDateFormat
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import pl.pjatk.traveler.adapter.PhotoAdapter
import pl.pjatk.traveler.database.AppDatabase
import pl.pjatk.traveler.databinding.ActivityMainBinding
import pl.pjatk.traveler.model.Photo
import pl.pjatk.traveler.model.PhotoNoteDto
import pl.pjatk.traveler.model.Shared
import java.io.FileNotFoundException
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread


const val CAMERA_PERMISSIONS_REQUEST = 1
private const val CAMERA_REQUEST = 2
private const val NOTE_INTENT_REQUEST = 3
const val SHOW_INTENT_REQUEST = 5
const val REQ_LOCATION_PERMISSION = 4
private const val REQ_CHECK_SETTINGS = 100

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val photoAdapter by lazy { PhotoAdapter(this) }
    private val locman by lazy { getSystemService(LocationManager::class.java) }
    private val geofencingClient by lazy { LocationServices.getGeofencingClient(this) }
    private var uri = Uri.EMPTY
    private var currLocation = "Empty"

    val locationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations){
                currLocation = """
                        |${location.latitude}, ${location.longitude}
                        |${location.provider}
                    """.trimMargin()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Shared.db = AppDatabase.open(applicationContext)
        updatePhotosList()
        setupPhotoList()
        binding.addButton.setOnClickListener {
           startCamera()
        }
    }

    private fun startCamera() {
        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSIONS_REQUEST)
        }
        else {
            generateUri()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
            startActivityForResult(intent, CAMERA_REQUEST)
        }
    }

    fun checkLocationPermission(){
        val permissionStatus1 = checkSelfPermission(ACCESS_FINE_LOCATION)
        val permissionStatus2 = checkSelfPermission(ACCESS_COARSE_LOCATION)
        if(permissionStatus1 != PackageManager.PERMISSION_GRANTED && permissionStatus2 != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), REQ_LOCATION_PERMISSION)
        }
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    fun getLastLocation() {
        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
            }
        val best = locman.getBestProvider(criteria, true) ?: ""
        val loc = locman.getLastKnownLocation(best)
        currLocation = """
            |${locman.allProviders}
            |${loc}
            """.trimMargin()

        val pi = PendingIntent.getBroadcast(
            applicationContext,
            1,
            Intent(this, BroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        locman.addProximityAlert(loc?.latitude!!, loc?.longitude, 500f, -1, pi)

//        if (Geocoder.isPresent()) {
//            city = Geocoder(this)
//                .getFromLocation(loc?.latitude, loc?.longitude, 1)
//                .first()
//                .locality
//        }
    }

    @SuppressLint("MissingPermission")
    fun registerGeoFence() {
        val geofence = Geofence.Builder()
            .setRequestId("my_id")
            .setCircularRegion(52.262155,20.9855553, 500f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val request = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(geofence))
        }.build()
        val pi = PendingIntent.getBroadcast(
            applicationContext,
            1,
            Intent(this, BroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        geofencingClient.addGeofences(request, pi)
    }

    private fun generateUri() {
        val file = filesDir.resolve(
                SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                        .format(System.currentTimeMillis()) + ".jpg"
        ).also{
            it.writeText("")
        }

        uri = FileProvider.getUriForFile(
                this,
                "pl.pjatk.traveler.FileProvider",
                file
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, uri))
                .copy(Bitmap.Config.ARGB_8888,true)
                    .drawText()

            saveImage(bitmap)
            openAddNoteActivity()
        }

        if(requestCode == NOTE_INTENT_REQUEST && resultCode == RESULT_OK && data != null){
            val note = data.getStringExtra("note")
            val photoNote = PhotoNoteDto(
                note = note.orEmpty(),
                uri = uri.toString(),
                city = "city",
                country = "country"
            )
            thread {
                Shared.db?.photoNote?.insert(photoNote)
            }.let {
                updatePhotosList()
                setupPhotoList()
            }
        }
        super .onActivityResult(requestCode, resultCode, data)
    }


    private fun Bitmap.drawText():Bitmap?{
        val bitmap = copy(config,true)
        val canvas = Canvas(bitmap)

        Paint().apply {
            this.textSize = 60f
            typeface = Typeface.DEFAULT
            setShadowLayer(1f,0f,1f,Color.WHITE)
            canvas.drawText("currentLocation " + LocalDate.now(),20f,height - 20f,this)
        }
        return bitmap
    }

    private fun setupPhotoList() {
        binding.photoListView.apply{
            adapter = photoAdapter
        }
    }

    private fun updatePhotosList() {
        thread {
            Shared.db?.photoNote?.selectAll()?.let { it ->
                val list = mutableListOf<Photo>()
                it.forEach {
                    try{
                        val image = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, it.uri.toUri()))
                        val photo = Photo(
                                it.id,
                                it.note,
                                it.uri,
                                image
                        )
                        list.add(photo)
                    }catch (e: FileNotFoundException) {
                        Shared.db?.photoNote?.delete(it.id)
                    }
                }
                photoAdapter.photoList = list.toMutableList()
                runOnUiThread{ photoAdapter.notifyDataSetChanged() }
            }
        }
    }

    private fun saveImage(bitmap: Bitmap?) {
        if (bitmap != null) {
            contentResolver.openOutputStream(uri).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
    }

    fun openShowActivity(id: Long) {
        startActivityForResult(
            Intent(this, ShowActivity::class.java)
                .putExtra("id",id),
            SHOW_INTENT_REQUEST
        )
    }

    private fun openAddNoteActivity() {
        startActivityForResult(
            Intent(this, AddNoteActivity::class.java)
                .putExtra("name", uri.toString()),
            NOTE_INTENT_REQUEST
        )
    }

}