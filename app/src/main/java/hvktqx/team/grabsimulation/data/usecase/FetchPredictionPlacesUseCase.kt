package hvktqx.team.grabsimulation.data.usecase

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.SphericalUtil
import hvktqx.team.grabsimulation.data.model.Spot
import io.reactivex.Observable
import javax.inject.Inject

class FetchPredictionPlacesUseCase @Inject constructor(
  private val placesClient: PlacesClient
) {
  val TAG = "FetchPredictionPlaces"
  operator fun invoke(address: String, boundLatLng: LatLng): Observable<List<Spot>> {
    return Observable.create { emitter ->
      findPlaces(address, boundLatLng, onSuccess = {
        emitter.onNext(it)
      }, onFail = {
        emitter.onError(it)
      }) {
        emitter.onComplete()
      }
    }
  }

  private fun findPlaces(
    address: String,
    latLng: LatLng,
    onSuccess: (List<Spot>) -> Unit,
    onFail: (Throwable) -> Unit,
    onDone: () -> Unit
  ) {
    if (address.isEmpty()) {
      onSuccess(mutableListOf())
    } else {
      val radius = 200000
      val distanceFromCenterToCorner = radius * Math.sqrt(2.0)
      val south = SphericalUtil.computeOffset(latLng, distanceFromCenterToCorner, 225.0)
      val northeast = SphericalUtil.computeOffset(latLng, distanceFromCenterToCorner, 45.0)

      val token = AutocompleteSessionToken.newInstance()

      val request = FindAutocompletePredictionsRequest
        .builder()
        .setLocationRestriction(
          RectangularBounds.newInstance(
            LatLngBounds(
              south,
              northeast
            )
          )
        )
        .setCountry("vn")
        .setSessionToken(token)
        .setQuery(address)
        .build()

      placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
        onSuccess.invoke(response.autocompletePredictions.map {
          Spot(it.placeId, null, it.getFullText(null).toString())
        })
        onDone.invoke()
      }.addOnFailureListener { exception ->
        if (exception is ApiException) {
          val apiException = exception
          Log.e(TAG, "Place not found: " + apiException.statusCode)
        }
        onFail.invoke(exception)
        onDone.invoke()
      }
    }
  }
}
