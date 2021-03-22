package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import java.util.*

class RepresentativeFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding

    private val viewModel: RepresentativeViewModel by viewModels {
        RepresentativeViewModelFactory(
            (requireContext().applicationContext as MyApplication).electionRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.address.value?.state = binding.spinnerState.selectedItem as String
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.address.value?.state = binding.spinnerState.selectedItem as String
            }
        }

        val representativesAdapter = RepresentativeListAdapter()

        binding.listRepresentatives.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = representativesAdapter
        }

        viewModel.representatives.observe(viewLifecycleOwner) {
            representativesAdapter.submitList(it)
        }

        viewModel.showProgress.observe(viewLifecycleOwner) {
            hideKeyboard()
            toggleProgress(show = it)
        }

        viewModel.checkLocation.observe(viewLifecycleOwner) {
            if (true == it) {
                hideKeyboard()
                checkPermissionAndGetLocation()
                viewModel.doneLocationCheck()
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "Permission not granted")
            Snackbar.make(binding.root,
                R.string.location_permission_denied,
                Snackbar.LENGTH_SHORT
            ).show()
        } else {
            Log.d(TAG, "Permission granted!")
            checkDeviceLocationSettings()
        }
    }

    private fun checkPermissionAndGetLocation() {
        if (isPermissionGranted()) {
            checkDeviceLocationSettings()
        } else {
            requestLocationPermission()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
    }

    /*
     * Uses the Location Client to check the current state of location settings, and
     * gives the user the opportunity to turn on location services within the app.
     */
    private fun checkDeviceLocationSettings(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    startIntentSenderForResult(
                        exception.resolution.intentSender,
                        REQUEST_TURN_DEVICE_LOCATION_ON,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.device_location_not_available,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                getLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_TURN_DEVICE_LOCATION_ON == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Log.d(TAG, "Location enabled by user.")
                getLocation()
            } else {
                Log.e(TAG, "Location not enabled, user cancelled.")
                Snackbar.make(
                    binding.root,
                    R.string.device_location_not_available,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Get location from LocationServices
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        toggleProgress(show = true)
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    toggleProgress()
                    val address = geoCodeLocation(location)
                    viewModel.address.value = address
                    viewModel.loadRepresentatives()
                    locationManager.removeUpdates(this)
                }
            }
        )
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }.first()
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun toggleProgress(show: Boolean? = false) {
        if (true == show) {
            binding.progressBackground.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBackground.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "RepresentativeFragment"
        private const val LOCATION_PERMISSION_INDEX = 0
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val MIN_TIME = 400L
        private const val MIN_DISTANCE = 1000F
    }
}