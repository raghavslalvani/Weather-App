package com.eurofins.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.eurofins.weatherapp.data.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), OutputFragment.iOnBackPressed {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationAccesser = 10001
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        //val gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Wagle", " Permission has been granted")
            getLocation()
        } else {
            Log.d("Wagle", "Welcome to else block of permission")
            Toast.makeText(this, "Permission is not granted for fetching location",
                Toast.LENGTH_LONG).show()
            getPermission()
            //findNavController().navigate(R.id.action_permissionFragment_to_inputFragment)
        }
    }

    override fun mOnBackPressed() {
        onBackPressed()
    }




    private fun getPermission() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                AlertDialog.Builder(this)
                    .setMessage("To continue, we need permission to find your location")
                    .setCancelable(false)
                    .setPositiveButton("Ok"
                    ) { _: DialogInterface, _: Int ->
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            locationAccesser)
                    }
                    .setNegativeButton("Cancel"
                    ) { _, _ ->
                        navController.navigate(R.id.inputFragment)
                    }.show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationAccesser)
            }
        } else {
            Log.d("Wagle", " Permission Granted")
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.d("Wagle", location.toString())
            if (location != null) {
                Log.d("Wagle", location.latitude.toString() + " " + location.longitude.toString())
                viewModel.getWeatherForecast(location.latitude, location.longitude)
                viewModel.getPlace(location.latitude, location.longitude)
                navController.navigate(R.id.outputFragment)
            } else {
                Toast.makeText(this,
                    "We cannot fetch ur location due to some issue please enter ur pincode",
                    Toast.LENGTH_SHORT).show()
                Log.d("Wagle", "you are in get locations else block")
                getPermissionForLoacation()
                //findNavController().navigate(R.id.action_permissionFragment_to_inputFragment)
            }
        }
    }

    private fun getPermissionForLoacation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        AlertDialog.Builder(this)
            .setMessage("Location Service is disabled")
            .setCancelable(true)
            .setPositiveButton("Retry"
            ) { _: DialogInterface, _: Int ->
                getLocation()
            }
            .setNeutralButton("Search Location"
            ) { _, _ ->
                navController.navigate(R.id.inputFragment)
            }
            .setNegativeButton("cancel"
            ) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        if (requestCode == locationAccesser) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Wagle", " Permission Granted")
                getLocation()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                ) {
                    Log.d("Wagle",
                        " Permission Permanently not Granted")
                    navController.navigate(R.id.inputFragment)
                } else {
                    Log.d("Wagle", " Permission not Granted")
                    navController.navigate(R.id.inputFragment)

                }
            }
        }
    }
}




