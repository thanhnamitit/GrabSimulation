package hvktqx.team.grabsimulation.data.usecase

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import hvktqx.team.grabsimulation.data.model.Spot
import io.reactivex.Observable
import javax.inject.Inject

class FetchCurrentPlaceUseCase @Inject constructor(private val placesClient: PlacesClient) {

  operator fun invoke(): Observable<Spot> {
    return Observable.create { emitter ->
      placesClient.findCurrentPlace(
        FindCurrentPlaceRequest.newInstance(
          listOf(
            Place.Field.ID,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
          )
        )
      ).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          task.result?.placeLikelihoods?.first()?.place?.let { place ->
            emitter.onNext(Spot(place.id!!, place.latLng!!, place.address!!))
          }
        } else {
          emitter.onError(task.exception!!)
        }
        emitter.onComplete()
      }
    }
  }
}