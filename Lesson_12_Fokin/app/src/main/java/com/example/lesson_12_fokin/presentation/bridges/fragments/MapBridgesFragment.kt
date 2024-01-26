package com.example.lesson_12_fokin.presentation.bridges.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.data.remote.LoadState
import com.example.lesson_12_fokin.databinding.FragmentBridgesMapBinding
import com.example.lesson_12_fokin.extensions.toBitmap
import com.example.lesson_12_fokin.presentation.BoundingBoxBuilder
import com.example.lesson_12_fokin.presentation.NavigationController
import com.example.lesson_12_fokin.presentation.ViewMapPinBindingFactory
import com.example.lesson_12_fokin.data.remote.model.Bridge
import com.example.lesson_12_fokin.presentation.bridges.viewModels.MapBridgesViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import javax.inject.Inject

private const val YANDEX_ZOOM_REDUCTION_COEFFICIENT = 0.8f
private const val YANDEX_CLUSTER_RADIUS = 45.0
private const val YANDEX_CLUSTER_MIN_ZOOM = 20

class MapBridgesFragment : BaseFragment(R.layout.fragment_bridges_map) {

    private val binding by viewBinding(FragmentBridgesMapBinding::bind)

    private val viewModel: MapBridgesViewModel by appViewModels()

    private var collection: ClusterizedPlacemarkCollection? = null

    private val mapPinSize by lazy { resources.getDimensionPixelSize(R.dimen.map_pin_size) }

    private lateinit var boundingBoxBuilder: BoundingBoxBuilder

    private var lastLocation: Location? = null

    private var activityNav: NavigationController? = null

    private var mapObjects: MutableMap<PlacemarkMapObject, Bridge> = mutableMapOf()

    private val locationRequest by lazy { LocationRequest.Builder(10000).build() }

    @Inject
    lateinit var viewMapPinBindingFactory: ViewMapPinBindingFactory

    private val mapPinViewBinding by lazy { viewMapPinBindingFactory.create(layoutInflater) }

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val clusterListener = ClusterListener { cluster ->
        mapPinViewBinding.textViewName.text = cluster.size.toString()
        cluster.addClusterTapListener(
            clusterObjectsTapListener
        )
        cluster.appearance.setIcon(
            ImageProvider.fromBitmap(mapPinViewBinding.root.toBitmap(mapPinSize))
        )
    }

    private val clusterObjectsTapListener = ClusterTapListener { selectedCluster ->
        val points = selectedCluster.placemarks.map { it.geometry }
        boundingBoxBuilder = BoundingBoxBuilder().apply {
            points.forEach { point ->
                include(point)
            }
        }
        moveOnCluster(boundingBoxBuilder)
        true
    }

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
            updateLocation()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
        viewModel.loadBridges()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInsets()

        activityNav = activity as NavigationController

        bridgeLiveDataObserve()

        restoreUiState()

        collection = binding.mapView.mapWindow.map.mapObjects
            .addClusterizedPlacemarkCollection(clusterListener)

        binding.mapView.mapWindow.map.addInputListener(inputListener)

        binding.buttonOnLocation.setOnClickListener {
            lastLocation?.let { it1 -> moveOnLocation(it1) }
        }

        binding.materialButtonRepeat.setOnClickListener {
            binding.layoutError.isVisible = false
        }

        binding.materialToolbar.menu.findItem(R.id.menuButtonToList).setOnMenuItemClickListener {
            val bridgesFragment = BridgesFragment.newInstance()
            activityNav?.navigateWithoutBack(bridgesFragment)
            true
        }

        binding.materialButtonRepeat.setOnClickListener {
            viewModel.loadBridges()
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
        startLocation()
    }

    override fun onResume() {
        super.onResume()
        updateLocation()
    }

    override fun onPause() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onPause()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        activityNav = null
        super.onDestroyView()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        if (isPermissionNotGranted()) {
            binding.buttonOnLocation.isVisible = true
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocation() {
        if (isPermissionNotGranted()) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        } else {
            binding.buttonOnLocation.isVisible = true
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    lastLocation = location
                    updateLocationPlacemark(location)
                }
        }
    }

    private fun isPermissionNotGranted() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED

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

    private fun showItemBridge(bridge: Bridge) {
        binding.cardBridge.setOnClickListener {
            val detailFragment =
                DetailBridgeFragment.newInstance(bridge.id, ArrayList(bridge.divorces))
            viewModel.setBridge(bridge, binding.mapView.mapWindow.map.cameraPosition)
            activityNav?.navigate(detailFragment)
        }
        binding.bridgeRowView.bind(bridge)
        binding.cardBridge.isVisible = true
    }

    private fun initInsets() {
        binding.materialToolbar.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val systemBarsInsets = windowInsets.getInsets(WindowInsets.Type.systemBars())
                view.updatePadding(
                    top = systemBarsInsets.top
                )
            } else {
                view.updatePadding(
                    top = windowInsets.systemWindowInsetTop
                )
            }
            windowInsets
        }
    }

    private fun moveOnLocation(location: Location) {
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(location.latitude, location.longitude), 45f, 0f, 0f
            )
        )
    }

    private fun moveOnCluster(boundingBoxBuilder: BoundingBoxBuilder) {
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
    }

    private fun moveOnCameraPosition(cameraPosition: CameraPosition) {
        binding.mapView.mapWindow.map.move(cameraPosition)
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

    private fun restoreUiState() {
        viewModel.pairCameraToBridgeLiveData.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                moveOnCameraPosition(
                    pair.first
                )
                showItemBridge(pair.second)
            }
        }
    }

    private fun bridgeLiveDataObserve() {
        viewModel.bridgesLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Loading -> {
                    binding.layoutError.isVisible = false
                    binding.frameLayoutMap.isVisible = false
                    binding.appBarLayout.isVisible = false
                    binding.progressBar.isVisible = true
                }

                is LoadState.Data -> {
                    binding.layoutError.isVisible = false
                    val mapBridges = state.data
                    mapBridges.forEach { bridge ->
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
                    collection?.clusterPlacemarks(YANDEX_CLUSTER_RADIUS, YANDEX_CLUSTER_MIN_ZOOM)
                    moveOnCluster(
                        BoundingBoxBuilder().apply {
                            mapObjects.values.forEach { mapBridge ->
                                include(mapBridge.point)
                            }
                        }
                    )
                    binding.frameLayoutMap.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.frameLayoutMap.isVisible = true
                    binding.appBarLayout.isVisible = true
                }

                is LoadState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.frameLayoutMap.isVisible = false
                    binding.textViewError.text = state.toString()
                    binding.layoutError.isVisible = true
                }
            }
        }
    }

    companion object {

        fun newInstance(): MapBridgesFragment {
            return MapBridgesFragment()
        }
    }
}