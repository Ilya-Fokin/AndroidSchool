package com.example.lesson_10_fokin.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_10_fokin.R
import com.example.lesson_10_fokin.data.ApiClient
import com.example.lesson_10_fokin.data.MapBridgeMapper
import com.example.lesson_10_fokin.data.model.Bridge
import com.example.lesson_10_fokin.data.model.MapBridge
import com.example.lesson_10_fokin.databinding.FragmentMapBinding
import com.example.lesson_10_fokin.databinding.ViewMapPinBinding
import com.example.lesson_10_fokin.extensions.toBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import java.lang.StringBuilder

private const val YANDEX_ZOOM_REDUCTION_COEFFICIENT = 0.8f

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)

    private var collection: ClusterizedPlacemarkCollection? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mapObjects = mutableMapOf<PlacemarkMapObject, MapBridge>()

    private val locationRequest by lazy { LocationRequest.Builder(10000).build() }

    private var mapBridges: List<MapBridge>? = null

    private val mapPinViewBinding by lazy { ViewMapPinBinding.inflate(layoutInflater) }

    private val mapPinSize by lazy { resources.getDimensionPixelSize(R.dimen.map_pin_size) }

    private var lastLocation: Location? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations) {
                lastLocation = location
                updateLocationPlacemark(location)
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.contains(true)) {
            startLocation()
        }
    }

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            binding.cardBridge.isVisible = false
        }

        override fun onMapLongTap(map: Map, point: Point) {

        }
    }

    private val mapObjectTapListener = MapObjectTapListener { p0, _ ->
        mapObjects[p0]?.let { store ->
            showItemBridge(store)
        }
        true
    }

    private val clusterObjectsTapListener = ClusterTapListener { selectedCluster ->
        val points = selectedCluster.placemarks.map { it.geometry }

        val boundingBoxBuilder = BoundingBoxBuilder().apply {
            points.forEach { point ->
                include(point)
            }
        }

        val boundingBoxCameraPosition = binding.mapView.mapWindow.map.cameraPosition(
            Geometry.fromBoundingBox(boundingBoxBuilder.build()),
            0f,
            0f,
            binding.mapView.mapWindow.focusRect
        )

        val targetCameraPosition = CameraPosition(
            boundingBoxCameraPosition.target,
            boundingBoxCameraPosition.zoom - YANDEX_ZOOM_REDUCTION_COEFFICIENT,
            boundingBoxCameraPosition.azimuth,
            boundingBoxCameraPosition.tilt,
        )
        binding.mapView.mapWindow.map.move(targetCameraPosition)
        true
    }

    private val loadCallback = object : BridgeLoadCallback {
        override fun onSuccess(bridges: List<Bridge>) {
            binding.progressBar.isVisible = false
            mapBridges = MapBridgeMapper.toBridge(bridges)

            mapBridges?.forEach { bridge ->
                collection?.addPlacemark()?.apply {
                    geometry = bridge.point
                    setIcon(
                        ImageProvider.fromResource(context, bridge.iconId)
                    )
                    setIconStyle(IconStyle().apply {
                        anchor = PointF(0.5f, 1f)
                    })
                    addTapListener(mapObjectTapListener)
                    mapObjects[this] = bridge
                }
            }
            collection?.clusterPlacemarks(45.0, 20)
            binding.frameLayoutMap.isVisible = true
        }

        override fun onError(error: Throwable) {
            binding.progressBar.isVisible = false
            binding.frameLayoutMap.isVisible = false
            binding.textViewError.text = error.message
            binding.layoutError.isVisible = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
        startLocation()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onPause() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        updateLocation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBridges()
        collection = binding.mapView.mapWindow.map.mapObjects
            .addClusterizedPlacemarkCollection(clusterListener)
        binding.mapView.mapWindow.map.addInputListener(inputListener)

        binding.buttonOnLocation.setOnClickListener {
            lastLocation?.let { it1 -> moveOnLocation(it1) }
        }

        binding.materialButtonRepeat.setOnClickListener {
            binding.layoutError.isVisible = false
            loadBridges()
        }
    }

    private fun updateLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun startLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
            return
        }
        binding.buttonOnLocation.isVisible = true
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                lastLocation = location
                setStartLocationPlacemark(location)
            }

    }

    private fun setStartLocationPlacemark(location: Location) {
        binding.mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(location.latitude, location.longitude)
            setIcon(
                ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.ic_location))
            )
            setIconStyle(IconStyle().apply {
                anchor = PointF(0.5f, 1f)
            })
        }
        moveOnLocation(location)
    }

    private fun updateLocationPlacemark(location: Location) {
        binding.mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(location.latitude, location.longitude)
            setIcon(
                ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.ic_location))
            )
            setIconStyle(IconStyle().apply {
                anchor = PointF(0.5f, 1f)
            })
        }
    }

    private val clusterListener = ClusterListener { cluster ->
        mapPinViewBinding.textViewName.text = cluster.size.toString()
        cluster.addClusterTapListener(clusterObjectsTapListener)
        cluster.appearance.setIcon(
            ImageProvider.fromBitmap(mapPinViewBinding.root.toBitmap(mapPinSize))
        )
    }

    private fun showItemBridge(bridge: MapBridge) {
        binding.cardBridge.setOnClickListener {
            Toast.makeText(context, bridge.name, Toast.LENGTH_SHORT).show()
        }
        binding.textViewNameBridge.text = bridge.name
        val times = StringBuilder()
        if (bridge.divorces?.isEmpty() == false) {
            repeat(bridge.divorces.size) { index ->
                val startTimeStr = bridge.divorces[index].start.orEmpty()
                times.append("$startTimeStr-")
                val endTimeStr = bridge.divorces[index].end.orEmpty()
                times.append("$endTimeStr ")
            }
        }
        binding.textViewTime.text = times
        binding.imageViewBridge.setImageResource(bridge.iconId)
        binding.cardBridge.isVisible = true
    }

    private fun moveOnLocation(location: Location) {
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(location.latitude, location.longitude), 10f, 0f, 0f
            )
        )
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(requireContext(), art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun loadBridges() {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            binding.frameLayoutMap.isVisible = false
            try {
                val bridges = ApiClient.apiService.getBridges()
                loadCallback.onSuccess(bridges)
            } catch (e: Exception) {
                loadCallback.onError(e)
            } finally {
                binding.progressBar.isVisible = false
            }
        }
    }

}