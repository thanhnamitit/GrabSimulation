package hvktqx.team.grabsimulation.screen.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import hvktqx.team.grabsimulation.R
import hvktqx.team.grabsimulation.base.BaseFragment
import hvktqx.team.grabsimulation.data.model.Spot
import hvktqx.team.grabsimulation.data.model.Trip
import hvktqx.team.grabsimulation.data.remote.CarsProvider
import hvktqx.team.grabsimulation.screen.MainState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MapsFragment : BaseFragment(), OnMapReadyCallback {
  private lateinit var googleMap: GoogleMap
  private val carMarkers = mutableListOf<Marker>()

  @Inject
  lateinit var carsProvider: CarsProvider

  private val currentLocationPublisher = PublishSubject.create<Spot>()
  private val googleMapReadyPublisher = PublishSubject.create<GoogleMap>()
  private val mapReadyPublisher = Observable.zip<Spot, GoogleMap, Pair<Spot, GoogleMap>>(
    currentLocationPublisher,
    googleMapReadyPublisher, BiFunction { spot, map ->
    Pair(spot, map)
  })
  private val disposeOnMapReady = CompositeDisposable()
  private val disposalOnMapReady: Disposable.() -> Unit = {
    disposeOnMapReady.add(this)
  }

  private fun <T> PublishSubject<T>.withValueThenComplete(value: T) {
    onNext(value)
    onComplete()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_maps, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
    parentViewModel.selectSubscribe(this, MainState::currentLocation) {
      if (it.complete) {
        currentLocationPublisher.withValueThenComplete(it())
      }
    }
    mapReadyPublisher.subscribe {
      onEverythingReady(it.second, it.first)
    }.disposalOnMapReady()
  }

  override fun onMapReady(googleMap: GoogleMap) {
    googleMapReadyPublisher.withValueThenComplete(googleMap)
  }

  private fun onEverythingReady(googleMap: GoogleMap, currentSpot: Spot) {
    disposeOnMapReady.dispose()
    this.googleMap = googleMap
    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentSpot.latLng, 16f))
    googleMap.isMyLocationEnabled = true
    parentViewModel.trip.observe(this, Observer { trip ->
      if (trip != null) drawTripRoute(trip) else {
        carsProvider.carsLatLng.removeObservers(this@MapsFragment)
        googleMap.clear()
      }
    })
    googleMap.uiSettings.isRotateGesturesEnabled = false
  }

  fun genMarkerOption(latLng: LatLng, res: Int): MarkerOptions {
    return MarkerOptions().position(latLng).draggable(false).flat(true).anchor(0.5f, 0.5f).icon(bitmapDescriptorFromVector(requireContext(), res))
  }

  private fun drawTripRoute(trip: Trip) {
    val latLngs = PolyUtil.decode(trip.polyline)
    boundRoute(latLngs)
    googleMap.addPolyline(PolylineOptions().width(15.0f).color(Color.BLACK).addAll(latLngs))
    googleMap.addMarker(genMarkerOption(trip.destination, R.drawable.ic_origin))
    googleMap.addMarker(genMarkerOption(trip.origin, R.drawable.ic_destination))

    carsProvider.carsLatLng.observe(this, Observer {
      if (it.first != null && it.second != null) {
        addCarMarker(it.first!!, it.second!!)
      }
    })
  }

  private fun addCarMarker(latLngs: List<LatLng>, ratio: Long) {
    val startLatLng = parentViewModel.trip.value?.origin
    if (startLatLng != null) {
      carMarkers.forEach { it.remove() }
      carMarkers.clear()
      carMarkers.addAll(latLngs.map {
        googleMap.addMarker(genMarkerOption(it, if (location(startLatLng, it) < ratio) R.drawable.ic_car else R.drawable.ic_car_black))
      })
    }
  }

  private fun boundRoute(latLngs: List<LatLng>) {

    val builder = LatLngBounds.Builder()
    latLngs.forEach { builder.include(it) }
    val bounds = builder.build()
    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
  }

  private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
      setBounds(0, 0, intrinsicWidth, intrinsicHeight)
      val bitmap =
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
      draw(Canvas(bitmap))
      BitmapDescriptorFactory.fromBitmap(bitmap)
    }
  }

  fun location(start: LatLng, end: LatLng): Float {
    val result = FloatArray(1)
    Location.distanceBetween(start.latitude, start.longitude,
      end.latitude, end.longitude, result)
    return result[0] / 1000
  }
}